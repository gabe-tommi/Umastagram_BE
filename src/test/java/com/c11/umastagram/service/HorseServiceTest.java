/*
 * Author: Test Suite
 * Created: 03 December 2025
 * Summary: HorseService unit tests covering all business logic and validation
 */
package com.c11.umastagram.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.c11.umastagram.model.Horse;
import com.c11.umastagram.repository.HorseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@DisplayName("HorseService Tests")
public class HorseServiceTest {
    
    @Mock
    private HorseRepository horseRepository;
    
    private HorseService horseService;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        horseService = new HorseService(horseRepository);
    }
    
    // ============ SaveHorse Tests ============
    
    @Test
    @DisplayName("Should save a new horse with valid data")
    public void testSaveHorseSuccess() {
        Horse horse = new Horse("Thunderbolt", "http://example.com/image.jpg", 
                                LocalDate.of(2015, 5, 10), null);
        Horse savedHorse = new Horse("Thunderbolt", "http://example.com/image.jpg", 
                                      LocalDate.of(2015, 5, 10), null);
        savedHorse.setHorseId(1L);
        
        when(horseRepository.getHorseByName("Thunderbolt")).thenReturn(Optional.empty());
        when(horseRepository.save(any(Horse.class))).thenReturn(savedHorse);
        
        Horse result = horseService.saveHorse(horse);
        
        assertNotNull(result);
        assertEquals(1L, result.getHorseId());
        assertEquals("Thunderbolt", result.getHorseName());
        verify(horseRepository, times(1)).save(any(Horse.class));
    }
    
    @Test
    @DisplayName("Should throw exception when horse is null")
    public void testSaveHorseNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            horseService.saveHorse(null);
        });
        verify(horseRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Should throw exception when horse name is null")
    public void testSaveHorseWithNullName() {
        Horse horse = new Horse(null, "http://example.com/image.jpg", 
                                LocalDate.of(2015, 5, 10), null);
        
        assertThrows(IllegalArgumentException.class, () -> {
            horseService.saveHorse(horse);
        });
        assertEquals("Horse name is required", 
                     assertThrows(IllegalArgumentException.class, () -> {
                         horseService.saveHorse(horse);
                     }).getMessage());
    }
    
    @Test
    @DisplayName("Should throw exception when horse name is empty")
    public void testSaveHorseWithEmptyName() {
        Horse horse = new Horse("", "http://example.com/image.jpg", 
                                LocalDate.of(2015, 5, 10), null);
        
        assertThrows(IllegalArgumentException.class, () -> {
            horseService.saveHorse(horse);
        });
    }
    
    @Test
    @DisplayName("Should throw exception when horse image link is null")
    public void testSaveHorseWithNullImageLink() {
        Horse horse = new Horse("Thunderbolt", null, 
                                LocalDate.of(2015, 5, 10), null);
        
        assertThrows(IllegalArgumentException.class, () -> {
            horseService.saveHorse(horse);
        });
        assertEquals("Horse image link is required", 
                     assertThrows(IllegalArgumentException.class, () -> {
                         horseService.saveHorse(horse);
                     }).getMessage());
    }
    
    @Test
    @DisplayName("Should throw exception when horse image link is empty")
    public void testSaveHorseWithEmptyImageLink() {
        Horse horse = new Horse("Thunderbolt", "", 
                                LocalDate.of(2015, 5, 10), null);
        
        assertThrows(IllegalArgumentException.class, () -> {
            horseService.saveHorse(horse);
        });
    }
    
    @Test
    @DisplayName("Should throw exception when horse name already exists")
    public void testSaveHorseWithDuplicateName() {
        Horse existingHorse = new Horse("Spirit", "http://example.com/spirit.jpg", 
                                        LocalDate.of(2010, 3, 15), null);
        existingHorse.setHorseId(1L);
        
        Horse newHorse = new Horse("Spirit", "http://example.com/newspirit.jpg", 
                                   LocalDate.of(2015, 5, 10), null);
        
        when(horseRepository.getHorseByName("Spirit")).thenReturn(Optional.of(existingHorse));
        
        assertThrows(IllegalArgumentException.class, () -> {
            horseService.saveHorse(newHorse);
        });
        assertEquals("Horse name already exists", 
                     assertThrows(IllegalArgumentException.class, () -> {
                         horseService.saveHorse(newHorse);
                     }).getMessage());
    }
    
    @Test
    @DisplayName("Should trim whitespace from horse name and image link")
    public void testSaveHorsesTrimsWhitespace() {
        Horse horse = new Horse("  Thunderbolt  ", "  http://example.com/image.jpg  ", 
                                LocalDate.of(2015, 5, 10), null);
        Horse savedHorse = new Horse("Thunderbolt", "http://example.com/image.jpg", 
                                      LocalDate.of(2015, 5, 10), null);
        savedHorse.setHorseId(1L);
        
        when(horseRepository.getHorseByName("Thunderbolt")).thenReturn(Optional.empty());
        when(horseRepository.save(any(Horse.class))).thenReturn(savedHorse);
        
        Horse result = horseService.saveHorse(horse);
        
        assertEquals("Thunderbolt", result.getHorseName());
        assertEquals("http://example.com/image.jpg", result.getHorseImageLink());
    }
    
    @Test
    @DisplayName("Should allow updating existing horse without checking duplicate name")
    public void testUpdateExistingHorse() {
        Horse horse = new Horse("Thunderbolt", "http://example.com/image.jpg", 
                                LocalDate.of(2015, 5, 10), null);
        horse.setHorseId(1L);
        Horse savedHorse = new Horse("Thunderbolt", "http://example.com/image.jpg", 
                                      LocalDate.of(2015, 5, 10), null);
        savedHorse.setHorseId(1L);
        
        when(horseRepository.save(any(Horse.class))).thenReturn(savedHorse);
        
        Horse result = horseService.saveHorse(horse);
        
        assertNotNull(result);
        assertEquals(1L, result.getHorseId());
        verify(horseRepository, never()).getHorseByName(any());
        verify(horseRepository, times(1)).save(any(Horse.class));
    }
    
    // ============ FindHorseById Tests ============
    
    @Test
    @DisplayName("Should find horse by valid ID")
    public void testFindHorseByIdSuccess() {
        Horse horse = new Horse("Thunderbolt", "http://example.com/image.jpg", 
                                LocalDate.of(2015, 5, 10), null);
        horse.setHorseId(1L);
        
        when(horseRepository.getHorseById(1L)).thenReturn(Optional.of(horse));
        
        Optional<Horse> result = horseService.findHorseById(1L);
        
        assertTrue(result.isPresent());
        assertEquals("Thunderbolt", result.get().getHorseName());
        verify(horseRepository, times(1)).getHorseById(1L);
    }
    
    @Test
    @DisplayName("Should return empty when horse ID not found")
    public void testFindHorseByIdNotFound() {
        when(horseRepository.getHorseById(999L)).thenReturn(Optional.empty());
        
        Optional<Horse> result = horseService.findHorseById(999L);
        
        assertFalse(result.isPresent());
        verify(horseRepository, times(1)).getHorseById(999L);
    }
    
    @Test
    @DisplayName("Should return empty when horse ID is null")
    public void testFindHorseByIdNull() {
        Optional<Horse> result = horseService.findHorseById(null);
        
        assertFalse(result.isPresent());
        verify(horseRepository, never()).getHorseById(any());
    }
    
    // ============ FindAllHorses Tests ============
    
    @Test
    @DisplayName("Should retrieve all horses when available")
    public void testFindAllHorsesSuccess() {
        Horse horse1 = new Horse("Thunderbolt", "http://example.com/image1.jpg", 
                                 LocalDate.of(2015, 5, 10), null);
        horse1.setHorseId(1L);
        
        Horse horse2 = new Horse("Spirit", "http://example.com/image2.jpg", 
                                 LocalDate.of(2010, 3, 15), null);
        horse2.setHorseId(2L);
        
        List<Horse> horses = Arrays.asList(horse1, horse2);
        
        when(horseRepository.getAllHorses()).thenReturn(Optional.of(horses));
        
        Optional<List<Horse>> result = horseService.findAllHorses();
        
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        verify(horseRepository, times(1)).getAllHorses();
    }
    
    @Test
    @DisplayName("Should return empty when no horses exist")
    public void testFindAllHorsesEmpty() {
        when(horseRepository.getAllHorses()).thenReturn(Optional.empty());
        
        Optional<List<Horse>> result = horseService.findAllHorses();
        
        assertFalse(result.isPresent());
        verify(horseRepository, times(1)).getAllHorses();
    }
    
    @Test
    @DisplayName("Should return empty when horse list is empty")
    public void testFindAllHorsesEmptyList() {
        when(horseRepository.getAllHorses()).thenReturn(Optional.of(Arrays.asList()));
        
        Optional<List<Horse>> result = horseService.findAllHorses();
        
        assertFalse(result.isPresent());
        verify(horseRepository, times(1)).getAllHorses();
    }
    
    // ============ FindHorseByName Tests ============
    
    @Test
    @DisplayName("Should find horse by valid name")
    public void testFindHorseByNameSuccess() {
        Horse horse = new Horse("Thunderbolt", "http://example.com/image.jpg", 
                                LocalDate.of(2015, 5, 10), null);
        horse.setHorseId(1L);
        
        when(horseRepository.getHorseByName("Thunderbolt")).thenReturn(Optional.of(horse));
        
        Optional<Horse> result = horseService.findHorseByName("Thunderbolt");
        
        assertTrue(result.isPresent());
        assertEquals("Thunderbolt", result.get().getHorseName());
        verify(horseRepository, times(1)).getHorseByName("Thunderbolt");
    }
    
    @Test
    @DisplayName("Should return empty when horse name not found")
    public void testFindHorseByNameNotFound() {
        when(horseRepository.getHorseByName("NonExistent")).thenReturn(Optional.empty());
        
        Optional<Horse> result = horseService.findHorseByName("NonExistent");
        
        assertFalse(result.isPresent());
        verify(horseRepository, times(1)).getHorseByName("NonExistent");
    }
    
    @Test
    @DisplayName("Should return empty when horse name is null")
    public void testFindHorseByNameNull() {
        Optional<Horse> result = horseService.findHorseByName(null);
        
        assertFalse(result.isPresent());
        verify(horseRepository, never()).getHorseByName(any());
    }
    
    @Test
    @DisplayName("Should return empty when horse name is empty")
    public void testFindHorseByNameEmpty() {
        Optional<Horse> result = horseService.findHorseByName("");
        
        assertFalse(result.isPresent());
        verify(horseRepository, never()).getHorseByName(any());
    }
    
    @Test
    @DisplayName("Should trim whitespace from horse name when searching")
    public void testFindHorseByNameTrimsWhitespace() {
        Horse horse = new Horse("Thunderbolt", "http://example.com/image.jpg", 
                                LocalDate.of(2015, 5, 10), null);
        horse.setHorseId(1L);
        
        when(horseRepository.getHorseByName("Thunderbolt")).thenReturn(Optional.of(horse));
        
        Optional<Horse> result = horseService.findHorseByName("  Thunderbolt  ");
        
        assertTrue(result.isPresent());
        verify(horseRepository, times(1)).getHorseByName("Thunderbolt");
    }
    
    // ============ GetHorseImageLinkById Tests ============
    
    @Test
    @DisplayName("Should retrieve horse image link by valid ID")
    public void testGetHorseImageLinkByIdSuccess() {
        when(horseRepository.getHorseImageLinkById(1L))
                .thenReturn(Optional.of("http://example.com/image.jpg"));
        
        Optional<String> result = horseService.getHorseImageLinkById(1L);
        
        assertTrue(result.isPresent());
        assertEquals("http://example.com/image.jpg", result.get());
        verify(horseRepository, times(1)).getHorseImageLinkById(1L);
    }
    
    @Test
    @DisplayName("Should return empty when horse image link not found")
    public void testGetHorseImageLinkByIdNotFound() {
        when(horseRepository.getHorseImageLinkById(999L)).thenReturn(Optional.empty());
        
        Optional<String> result = horseService.getHorseImageLinkById(999L);
        
        assertFalse(result.isPresent());
        verify(horseRepository, times(1)).getHorseImageLinkById(999L);
    }
    
    @Test
    @DisplayName("Should return empty when horse ID is null")
    public void testGetHorseImageLinkByIdNull() {
        Optional<String> result = horseService.getHorseImageLinkById(null);
        
        assertFalse(result.isPresent());
        verify(horseRepository, never()).getHorseImageLinkById(any());
    }
    
    // ============ GetHorseDescriptionById Tests ============
    
    @Test
    @DisplayName("Should retrieve horse description by valid ID")
    public void testGetHorseDescriptionByIdSuccess() {
        when(horseRepository.getHorseDescriptionById(1L))
                .thenReturn(Optional.of("A majestic and noble steed"));
        
        Optional<String> result = horseService.getHorseDescriptionById(1L);
        
        assertTrue(result.isPresent());
        assertEquals("A majestic and noble steed", result.get());
        verify(horseRepository, times(1)).getHorseDescriptionById(1L);
    }
    
    @Test
    @DisplayName("Should return empty when horse description not found")
    public void testGetHorseDescriptionByIdNotFound() {
        when(horseRepository.getHorseDescriptionById(999L)).thenReturn(Optional.empty());
        
        Optional<String> result = horseService.getHorseDescriptionById(999L);
        
        assertFalse(result.isPresent());
        verify(horseRepository, times(1)).getHorseDescriptionById(999L);
    }
    
    @Test
    @DisplayName("Should return empty when horse ID is null")
    public void testGetHorseDescriptionByIdNull() {
        Optional<String> result = horseService.getHorseDescriptionById(null);
        
        assertFalse(result.isPresent());
        verify(horseRepository, never()).getHorseDescriptionById(any());
    }
    
    // ============ GetHorseBirthdayById Tests ============
    
    @Test
    @DisplayName("Should retrieve horse birthday by valid ID")
    public void testGetHorseBirthdayByIdSuccess() {
        LocalDate birthday = LocalDate.of(2015, 5, 10);
        when(horseRepository.getHorseBirthdayById(1L))
                .thenReturn(Optional.of(birthday));
        
        Optional<LocalDate> result = horseService.getHorseBirthdayById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(birthday, result.get());
        verify(horseRepository, times(1)).getHorseBirthdayById(1L);
    }
    
    @Test
    @DisplayName("Should return empty when horse birthday not found")
    public void testGetHorseBirthdayByIdNotFound() {
        when(horseRepository.getHorseBirthdayById(999L)).thenReturn(Optional.empty());
        
        Optional<LocalDate> result = horseService.getHorseBirthdayById(999L);
        
        assertFalse(result.isPresent());
        verify(horseRepository, times(1)).getHorseBirthdayById(999L);
    }
    
    @Test
    @DisplayName("Should return empty when horse ID is null")
    public void testGetHorseBirthdayByIdNull() {
        Optional<LocalDate> result = horseService.getHorseBirthdayById(null);
        
        assertFalse(result.isPresent());
        verify(horseRepository, never()).getHorseBirthdayById(any());
    }
    
    // ============ GetHorseDeathdayById Tests ============
    
    @Test
    @DisplayName("Should retrieve horse deathday by valid ID")
    public void testGetHorseDeathdayByIdSuccess() {
        LocalDate deathday = LocalDate.of(2020, 12, 25);
        when(horseRepository.getHorseDeathdayById(1L))
                .thenReturn(Optional.of(deathday));
        
        Optional<LocalDate> result = horseService.getHorseDeathdayById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(deathday, result.get());
        verify(horseRepository, times(1)).getHorseDeathdayById(1L);
    }
    
    @Test
    @DisplayName("Should return empty when horse deathday not found")
    public void testGetHorseDeathdayByIdNotFound() {
        when(horseRepository.getHorseDeathdayById(999L)).thenReturn(Optional.empty());
        
        Optional<LocalDate> result = horseService.getHorseDeathdayById(999L);
        
        assertFalse(result.isPresent());
        verify(horseRepository, times(1)).getHorseDeathdayById(999L);
    }
    
    @Test
    @DisplayName("Should return empty when horse ID is null")
    public void testGetHorseDeathdayByIdNull() {
        Optional<LocalDate> result = horseService.getHorseDeathdayById(null);
        
        assertFalse(result.isPresent());
        verify(horseRepository, never()).getHorseDeathdayById(any());
    }
}
