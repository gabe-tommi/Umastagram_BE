/*
    * Author: Joceline Cortez-Arellano
    * Date: 03 December 2025
    * Date Last Modified: 03 December 2025
    * Last Modified By: Joceline Cortez-Arellano
    * Summary: Horse Repository Unit Test
 */
package com.c11.umastagram.repository;

import com.c11.umastagram.model.Horse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EntityScan(basePackageClasses = Horse.class)
public class HorseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HorseRepository horseRepository;

    @BeforeEach
    public void createHorseTableIfNeeded() {
        // Create the `horse` table in the H2 in-memory DB so tests can run independent of Hibernate DDL
        try {
                entityManager.getEntityManager()
                    .createNativeQuery("CREATE TABLE IF NOT EXISTS \"horse\" (\"horse_id\" BIGINT AUTO_INCREMENT PRIMARY KEY, \"horse_name\" VARCHAR(255), \"horse_image_link\" CLOB NOT NULL, \"horse_birthday\" DATE, \"horse_deathday\" DATE, \"horse_desc\" VARCHAR(255))")
                    .executeUpdate();
        } catch (Exception ignored) {
            // If creation fails (table already exists or DDL not supported), ignore and let tests proceed
        }
    }

    @Test
    public void whenGetAllHorses_thenReturnAllHorses() {
        // Given
        Horse horse1 = new Horse();
        horse1.setHorseName("Spirit");
        horse1.setHorseImageLink("http://example.com/spirit.jpg");
        horse1.setHorseDescription("A wild mustang");
        horse1.setHorseBirthday(LocalDate.of(2015, 3, 10));
        horse1.setHorseDeathday(LocalDate.of(2023, 7, 20));

        Horse horse2 = new Horse();
        horse2.setHorseName("Trigger");
        horse2.setHorseImageLink("http://example.com/trigger.jpg");
        horse2.setHorseDescription("Roy Rogers' faithful companion");
        horse2.setHorseBirthday(LocalDate.of(2005, 4, 15));

        entityManager.persist(horse1);
        entityManager.persist(horse2);
        entityManager.flush();

        // When
        Optional<List<Horse>> foundHorses = horseRepository.getAllHorses();

        // Then
        assertThat(foundHorses).isPresent();
        assertThat(foundHorses.get()).hasSize(2);
        assertThat(foundHorses.get()).extracting(Horse::getHorseName)
                .containsExactlyInAnyOrder("Spirit", "Trigger");
    }

    @Test
    public void whenGetHorseById_thenReturnHorse() {
        // Given
        Horse horse = new Horse();
        horse.setHorseName("Shadowfax");
        horse.setHorseImageLink("http://example.com/shadowfax.jpg");
        horse.setHorseDescription("A magnificent steed");
        horse.setHorseBirthday(LocalDate.of(2010, 6, 1));
        horse.setHorseDeathday(LocalDate.of(2020, 12, 31));

        entityManager.persist(horse);
        entityManager.flush();

        // When
        Optional<Horse> found = horseRepository.getHorseById(horse.getHorseId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getHorseName()).isEqualTo("Shadowfax");
        assertThat(found.get().getHorseDescription()).isEqualTo("A magnificent steed");
    }

    @Test
    public void whenGetHorseByName_thenReturnHorse() {
        // Given
        Horse horse = new Horse();
        horse.setHorseName("Black Beauty");
        horse.setHorseImageLink("http://example.com/blackbeauty.jpg");
        horse.setHorseDescription("A noble and kind horse");
        horse.setHorseBirthday(LocalDate.of(2008, 2, 14));

        entityManager.persist(horse);
        entityManager.flush();

        // When
        Optional<Horse> found = horseRepository.getHorseByName("Black Beauty");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getHorseImageLink()).isEqualTo("http://example.com/blackbeauty.jpg");
    }

    @Test
    public void whenGetHorseImageLinkById_thenReturnImageLink() {
        // Given
        Horse horse = new Horse();
        horse.setHorseName("Seabiscuit");
        horse.setHorseImageLink("http://example.com/seabiscuit.jpg");
        horse.setHorseDescription("An underdog champion");
        horse.setHorseBirthday(LocalDate.of(1933, 5, 23));
        horse.setHorseDeathday(LocalDate.of(1947, 5, 17));

        entityManager.persist(horse);
        entityManager.flush();

        // When
        Optional<String> imageLink = horseRepository.getHorseImageLinkById(horse.getHorseId());

        // Then
        assertThat(imageLink).isPresent();
        assertThat(imageLink.get()).isEqualTo("http://example.com/seabiscuit.jpg");
    }

    @Test
    public void whenGetHorseDescriptionById_thenReturnDescription() {
        // Given
        Horse horse = new Horse();
        horse.setHorseName("Bucephalus");
        horse.setHorseImageLink("http://example.com/bucephalus.jpg");
        horse.setHorseDescription("Alexander the Great's legendary war horse");
        horse.setHorseBirthday(LocalDate.of(2001, 1, 1));

        entityManager.persist(horse);
        entityManager.flush();

        // When
        Optional<String> description = horseRepository.getHorseDescriptionById(horse.getHorseId());

        // Then
        assertThat(description).isPresent();
        assertThat(description.get()).isEqualTo("Alexander the Great's legendary war horse");
    }

    @Test
    public void whenGetHorseBirthdayById_thenReturnBirthday() {
        // Given
        Horse horse = new Horse();
        horse.setHorseName("Man O' War");
        horse.setHorseImageLink("http://example.com/manowar.jpg");
        horse.setHorseDescription("One of the greatest racehorses ever");
        LocalDate birthday = LocalDate.of(1917, 3, 29);
        horse.setHorseBirthday(birthday);
        horse.setHorseDeathday(LocalDate.of(1947, 11, 1));

        entityManager.persist(horse);
        entityManager.flush();

        // When
        Optional<LocalDate> foundBirthday = horseRepository.getHorseBirthdayById(horse.getHorseId());

        // Then
        assertThat(foundBirthday).isPresent();
        assertThat(foundBirthday.get()).isEqualTo(birthday);
    }

    @Test
    public void whenGetHorseDeathdayById_thenReturnDeathday() {
        // Given
        Horse horse = new Horse();
        horse.setHorseName("Citation");
        horse.setHorseImageLink("http://example.com/citation.jpg");
        horse.setHorseDescription("Triple Crown winner");
        horse.setHorseBirthday(LocalDate.of(1945, 4, 11));
        LocalDate deathday = LocalDate.of(1970, 8, 8);
        horse.setHorseDeathday(deathday);

        entityManager.persist(horse);
        entityManager.flush();

        // When
        Optional<LocalDate> foundDeathday = horseRepository.getHorseDeathdayById(horse.getHorseId());

        // Then
        assertThat(foundDeathday).isPresent();
        assertThat(foundDeathday.get()).isEqualTo(deathday);
    }

    @Test
    public void whenGetHorseById_withNonExistentId_thenReturnEmpty() {
        // When
        Optional<Horse> notFound = horseRepository.getHorseById(999L);

        // Then
        assertThat(notFound).isEmpty();
    }

    @Test
    public void whenGetHorseByName_withNonExistentName_thenReturnEmpty() {
        // When
        Optional<Horse> notFound = horseRepository.getHorseByName("Non Existent Horse");

        // Then
        assertThat(notFound).isEmpty();
    }

    @Test
    public void whenGetHorseDescriptionById_withNullDescription_thenReturnEmpty() {
        // Given
        Horse horse = new Horse();
        horse.setHorseName("Unnamed Horse");
        horse.setHorseImageLink("http://example.com/unnamed.jpg");
        // description intentionally not set (null)
        horse.setHorseBirthday(LocalDate.of(2020, 1, 1));

        entityManager.persist(horse);
        entityManager.flush();

        // When
        Optional<String> description = horseRepository.getHorseDescriptionById(horse.getHorseId());

        // Then
        assertThat(description).isEmpty();
    }
}
