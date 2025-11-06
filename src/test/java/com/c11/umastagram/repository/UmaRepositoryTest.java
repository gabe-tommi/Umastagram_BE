/*
    * Author: Joceline Cortez-Arellano
    * Date: 6 November 2025
    * Date Last Modified: 06 November 2025
    * Last Modified By: Joceline Cortez-Arellano
    * Summary: Uma Repository Unit Test
 */

package com.c11.umastagram.repository;

import com.c11.umastagram.model.Uma;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UmaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UmaRepository umaRepository;

    @Test
    public void whenGetAllUmas_thenReturnAllUmas() {
        // Given
        Uma uma1 = new Uma();
        uma1.setUmaName("Special Week");
        uma1.setUmaImageLink("http://example.com/special-week.jpg");
        uma1.setFunFact("Won the Japan Cup!");
        uma1.setUmaBirthday(LocalDate.of(2015, 5, 2));

        Uma uma2 = new Uma();
        uma2.setUmaName("Silence Suzuka");
        uma2.setUmaImageLink("http://example.com/suzuka.jpg");
        uma2.setFunFact("Known for incredible speed!");
        uma2.setUmaBirthday(LocalDate.of(2014, 4, 15));

        entityManager.persist(uma1);
        entityManager.persist(uma2);
        entityManager.flush();

        // When
        Optional<List<Uma>> foundUmas = umaRepository.getAllUmas();

        // Then
        assertThat(foundUmas).isPresent();
        assertThat(foundUmas.get()).hasSize(2);
        assertThat(foundUmas.get()).extracting(Uma::getUmaName)
                .containsExactlyInAnyOrder("Special Week", "Silence Suzuka");
    }

    @Test
    public void whenGetUmaById_thenReturnUma() {
        // Given
        Uma uma = new Uma();
        uma.setUmaName("Tokai Teio");
        uma.setUmaImageLink("http://example.com/teio.jpg");
        uma.setFunFact("Triple Crown winner!");
        uma.setUmaBirthday(LocalDate.of(2015, 3, 21));

        entityManager.persist(uma);
        entityManager.flush();

        // When
        Optional<Uma> found = umaRepository.getUmaById(uma.getUmaId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getUmaName()).isEqualTo("Tokai Teio");
    }

    @Test
    public void whenGetUmaByName_thenReturnUma() {
        // Given
        Uma uma = new Uma();
        uma.setUmaName("Gold Ship");
        uma.setUmaImageLink("http://example.com/goldship.jpg");
        uma.setFunFact("Known for unpredictable behavior!");
        uma.setUmaBirthday(LocalDate.of(2014, 3, 6));

        entityManager.persist(uma);
        entityManager.flush();

        // When
        Optional<Uma> found = umaRepository.getUmaByName("Gold Ship");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getUmaImageLink()).isEqualTo("http://example.com/goldship.jpg");
    }

    @Test
    public void whenGetUmaImageLinkById_thenReturnImageLink() {
        // Given
        Uma uma = new Uma();
        uma.setUmaName("Mejiro McQueen");
        uma.setUmaImageLink("http://example.com/mcqueen.jpg");
        uma.setFunFact("Speed and elegance combined!");
        uma.setUmaBirthday(LocalDate.of(2015, 1, 15));

        entityManager.persist(uma);
        entityManager.flush();

        // When
        Optional<String> imageLink = umaRepository.getUmaImageLinkById(uma.getUmaId());

        // Then
        assertThat(imageLink).isPresent();
        assertThat(imageLink.get()).isEqualTo("http://example.com/mcqueen.jpg");
    }

    @Test
    public void whenGetUmaFunFactById_thenReturnFunFact() {
        // Given
        Uma uma = new Uma();
        uma.setUmaName("Rice Shower");
        uma.setUmaImageLink("http://example.com/rice-shower.jpg");
        uma.setFunFact("Overcame all odds to become a champion!");
        uma.setUmaBirthday(LocalDate.of(2014, 2, 28));

        entityManager.persist(uma);
        entityManager.flush();

        // When
        Optional<String> funFact = umaRepository.getUmaFunFactById(uma.getUmaId());

        // Then
        assertThat(funFact).isPresent();
        assertThat(funFact.get()).isEqualTo("Overcame all odds to become a champion!");
    }

    @Test
    public void whenGetUmaBirthdayById_thenReturnBirthday() {
        // Given
        Uma uma = new Uma();
        uma.setUmaName("Vodka");
        uma.setUmaImageLink("http://example.com/vodka.jpg");
        uma.setFunFact("First female horse to win the Japan Cup!");
        LocalDate birthday = LocalDate.of(2014, 5, 1);
        uma.setUmaBirthday(birthday);

        entityManager.persist(uma);
        entityManager.flush();

        // When
        Optional<LocalDate> foundBirthday = umaRepository.getUmaBirthdayById(uma.getUmaId());

        // Then
        assertThat(foundBirthday).isPresent();
        assertThat(foundBirthday.get()).isEqualTo(birthday);
    }

    @Test
    public void whenGetUmaById_withNonExistentId_thenReturnEmpty() {
        // When
        Optional<Uma> notFound = umaRepository.getUmaById(999L);

        // Then
        assertThat(notFound).isEmpty();
    }

    @Test
    public void whenGetUmaByName_withNonExistentName_thenReturnEmpty() {
        // When
        Optional<Uma> notFound = umaRepository.getUmaByName("Non Existent Uma");

        // Then
        assertThat(notFound).isEmpty();
    }
}
