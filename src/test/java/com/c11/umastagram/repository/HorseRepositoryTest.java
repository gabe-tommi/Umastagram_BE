package com.c11.umastagram.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.c11.umastagram.model.Horse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Tests for HorseRepository, validating all custom JPQL and derived queries
 * using an in-memory database with @DataJpaTest.
 */
@DataJpaTest
@ActiveProfiles("test")
public class HorseRepositoryTest {

    @Autowired
    private HorseRepository horseRepository;
    
    // Test Data Setup
    private final LocalDate DATE_2000 = LocalDate.of(2000, 1, 1);
    private final LocalDate DATE_2010 = LocalDate.of(2010, 5, 15);
    private final LocalDate DATE_2020 = LocalDate.of(2020, 12, 31);
    private final LocalDate DATE_DEATH_2024 = LocalDate.of(2024, 6, 1);

    private Horse livingThoroughbred;
    private Horse deceasedArabian;
    private Horse anotherThoroughbred;

    @BeforeEach
    public void setupTestData() {
        // Setup standard test horses
        livingThoroughbred = new Horse("Secretariat", "link-sec", DATE_2010, null, "Thoroughbred");
        deceasedArabian = new Horse("Man o' War", "link-man", DATE_2000, DATE_DEATH_2024, "Arabian");
        anotherThoroughbred = new Horse("Seattle Slew", "link-sea", DATE_2020, null, "Thoroughbred");

        horseRepository.saveAll(List.of(livingThoroughbred, deceasedArabian, anotherThoroughbred));
    }

    @AfterEach
    public void tearDown() {
        // Clear the repository after each test
        horseRepository.deleteAll();
    }

    // --- 1. horseId (Primary Key) ---

    @Test
    public void testFindHorseById_Success() {
        Long id = livingThoroughbred.getHorseId();
        
        Optional<Horse> result = horseRepository.findHorseById(id);
        
        assertTrue(result.isPresent());
        assertEquals("Secretariat", result.get().getHorseName());
    }

    @Test
    public void testFindHorseById_NotFound() {
        Optional<Horse> result = horseRepository.findHorseById(999L);
        assertFalse(result.isPresent());
    }

    // --- 2. horseName (String) ---

    @Test
    public void testFindByHorseName_ExactMatch() {
        List<Horse> results = horseRepository.findByHorseName("Secretariat");
        assertEquals(1, results.size());
        assertEquals(livingThoroughbred.getHorseId(), results.get(0).getHorseId());
    }
    
    @Test
    public void testFindByHorseNameContainingIgnoreCase_PartialMatch() {
        // Search for "retar" in "Secretariat"
        List<Horse> results = horseRepository.findByHorseNameContainingIgnoreCase("retar");
        assertEquals(1, results.size());
        
        // Search for "WAR" in "Man o' War" (case-insensitive)
        results = horseRepository.findByHorseNameContainingIgnoreCase("WAR");
        assertEquals(1, results.size());
    }
    
    @Test
    public void testFindByHorseNameStartingWithIgnoreCase_PrefixMatch() {
        // Search for "Se" in "Secretariat" and "Seattle Slew"
        List<Horse> results = horseRepository.findByHorseNameStartingWithIgnoreCase("Se");
        assertEquals(2, results.size());
        
        // Search for "man" (case-insensitive)
        results = horseRepository.findByHorseNameStartingWithIgnoreCase("man");
        assertEquals(1, results.size());
    }

    // --- 3. horseImageLink (String) ---
    
    @Test
    public void testFindByHorseImageLink_UniqueLink() {
        Optional<Horse> result = horseRepository.findByHorseImageLink("link-sea");
        assertTrue(result.isPresent());
        assertEquals("Seattle Slew", result.get().getHorseName());
    }

    // --- 4. horseBirthday (LocalDate) ---

    @Test
    public void testFindByHorseBirthday_ExactDate() {
        List<Horse> results = horseRepository.findByHorseBirthday(DATE_2010);
        assertEquals(1, results.size());
        assertEquals("Secretariat", results.get(0).getHorseName());
    }

    @Test
    public void testFindByHorseBirthdayAfter() {
        // Should find Secretariat (2010) and Seattle Slew (2020), but not Man o' War (2000)
        List<Horse> results = horseRepository.findByHorseBirthdayAfter(DATE_2000.plusDays(1));
        assertEquals(2, results.size());
        
        // Should only find Seattle Slew (2020)
        results = horseRepository.findByHorseBirthdayAfter(DATE_2010);
        assertEquals(1, results.size());
        assertEquals("Seattle Slew", results.get(0).getHorseName());
    }

    @Test
    public void testFindByHorseBirthdayBetween() {
        // Between 2005-01-01 (exclusive) and 2015-01-01 (inclusive)
        List<Horse> results = horseRepository.findByHorseBirthdayBetween(
            LocalDate.of(2005, 1, 1), 
            LocalDate.of(2015, 1, 1)
        );
        // Should find only Secretariat (2010)
        assertEquals(1, results.size());
        assertEquals("Secretariat", results.get(0).getHorseName());
    }

    // --- 5. horseDeathday (LocalDate) ---

    @Test
    public void testFindByHorseDeathdayIsNotNull() {
        // Should find only Man o' War
        List<Horse> results = horseRepository.findByHorseDeathdayIsNotNull();
        assertEquals(1, results.size());
        assertEquals("Man o' War", results.get(0).getHorseName());
    }
    
    @Test
    public void testFindByHorseDeathdayIsNull() {
        // Should find Secretariat and Seattle Slew
        List<Horse> results = horseRepository.findByHorseDeathdayIsNull();
        assertEquals(2, results.size());
        
        boolean hasSecretariat = results.stream().anyMatch(h -> "Secretariat".equals(h.getHorseName()));
        boolean hasSeattleSlew = results.stream().anyMatch(h -> "Seattle Slew".equals(h.getHorseName()));
        
        assertTrue(hasSecretariat && hasSeattleSlew);
    }

    @Test
    public void testFindByHorseDeathdayBefore() {
        // Death day is 2024-06-01. Check for horses that died before 2025-01-01
        List<Horse> results = horseRepository.findByHorseDeathdayBefore(LocalDate.of(2025, 1, 1));
        assertEquals(1, results.size());
        assertEquals("Man o' War", results.get(0).getHorseName());
        
        // Check for horses that died before 2024-01-01 (should be none)
        results = horseRepository.findByHorseDeathdayBefore(LocalDate.of(2024, 1, 1));
        assertTrue(results.isEmpty());
    }
    
    // --- 6. horseBreed (String) ---

    @Test
    public void testFindByHorseBreed_ExactMatch() {
        List<Horse> results = horseRepository.findByHorseBreed("Thoroughbred");
        // Secretariat and Seattle Slew
        assertEquals(2, results.size()); 
    }

    @Test
    public void testFindByHorseBreedIgnoreCase() {
        // Find "arabian" (case-insensitive)
        List<Horse> results = horseRepository.findByHorseBreedIgnoreCase("arabian");
        assertEquals(1, results.size());
        assertEquals("Man o' War", results.get(0).getHorseName());
    }

    @Test
    public void testFindByHorseBreedContainingIgnoreCase_PartialMatch() {
        // Search for "ough" in "Thoroughbred"
        List<Horse> results = horseRepository.findByHorseBreedContainingIgnoreCase("ough");
        assertEquals(2, results.size()); // Secretariat and Seattle Slew
    }

    // --- 7. Combined Queries ---

    @Test
    public void testFindByHorseBreedAndHorseBirthdayAfter() {
        // Find Thoroughbreds born after 2015-01-01
        List<Horse> results = horseRepository.findByHorseBreedAndHorseBirthdayAfter(
            "Thoroughbred", 
            LocalDate.of(2015, 1, 1)
        );
        // Should only find Seattle Slew (born 2020)
        assertEquals(1, results.size());
        assertEquals("Seattle Slew", results.get(0).getHorseName());
    }

    @Test
    public void testFindByHorseNameContainingOrHorseBreed() {
        // Find horses with name containing 'tle' OR breed is 'Arabian'
        List<Horse> results = horseRepository.findByHorseNameContainingOrHorseBreed("tle", "Arabian");
        
        // Should find:
        // 1. Man o' War (Arabian)
        // 2. Seattle Slew (contains 'tle')
        assertEquals(2, results.size());
        
        boolean hasManOWar = results.stream().anyMatch(h -> "Man o' War".equals(h.getHorseName()));
        boolean hasSeattleSlew = results.stream().anyMatch(h -> "Seattle Slew".equals(h.getHorseName()));
        
        assertTrue(hasManOWar && hasSeattleSlew);
    }
}