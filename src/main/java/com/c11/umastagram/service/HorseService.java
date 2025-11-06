/*
 * Author: Gabe Gallagher
 * Created: 06 November 2025
 * Date Last Modified: 06 November 2025
 * Last Modified By: Gabe Gallagher
 * Summary: Horse Service Class; Business Logic for User Entity
 */

package com.c11.umastagram.service;

import com.c11.umastagram.model.Horse;
import com.c11.umastagram.repository.HorseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import for transaction management

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true) // Default all read methods to read-only
public class HorseService {

    @Autowired
    private HorseRepository horseRepository;

    /**
     * Saves a Horse entity to the repository after basic validation and normalization.
     * Ensures required fields are present and unique constraints are met (if any).
     * Trims whitespace from string fields.
     * @param horse The Horse to be saved
     * @return The saved Horse
     * @throws IllegalArgumentException if required fields are missing or invalid
     */
    @Transactional // Override read-only for write operation
    public Horse saveHorse(Horse horse) {
        
        // --- Input Normalization and Validation ---

        String horseName = horse.getHorseName() == null ? null : horse.getHorseName().trim();
        String horseImageLink = horse.getHorseImageLink() == null ? null : horse.getHorseImageLink().trim();
        String horseBreed = horse.getHorseBreed() == null ? null : horse.getHorseBreed().trim();

        // Persist trimmed values back to the horse object
        horse.setHorseName(horseName);
        horse.setHorseImageLink(horseImageLink);
        horse.setHorseBreed(horseBreed);

        // Required Field Check
        if (horseName == null || horseName.isEmpty()) {
            throw new IllegalArgumentException("Horse Name cannot be null or empty.");
        }
        if (horseImageLink == null || horseImageLink.isEmpty()) {
            // Note: Assuming image link is a required field based on column definition not being nullable
            throw new IllegalArgumentException("Horse Image Link cannot be null or empty.");
        }
        if (horse.getHorseBirthday() == null) {
             throw new IllegalArgumentException("Horse Birthday cannot be null.");
        }
        if (horse.getHorseBirthday().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Horse Birthday cannot be in the future.");
        }

        // Unique Constraint Check (Example: Assuming image link should be unique)
        Optional<Horse> existingHorseByLink = horseRepository.findByHorseImageLink(horseImageLink);
        if (existingHorseByLink.isPresent() && !existingHorseByLink.get().getHorseId().equals(horse.getHorseId())) {
            throw new IllegalArgumentException("Horse Image Link must be unique.");
        }

        return horseRepository.save(horse);
    }

    // ----------------------------------------
    // --- Basic CRUD Operations (from JpaRepository) ---
    // ----------------------------------------

    /** Find a Horse by its primary key (ID) */
    public Optional<Horse> findHorseById(Long horseId) {
        return horseRepository.findHorseById(horseId);
    }

    /** Retrieve all Horses from the repository */
    public List<Horse> findAllHorses() {
        return horseRepository.findAll();
    }

    /** Deletes a Horse by its ID */
    @Transactional
    public void deleteHorse(Long horseId) {
        if (!horseRepository.existsById(horseId)) {
            throw new IllegalArgumentException("Horse with ID " + horseId + " not found for deletion.");
        }
        horseRepository.deleteById(horseId);
    }
    
    // ----------------------------------------
    // --- Business Logic / Custom Repository Methods ---
    // ----------------------------------------

    /**
     * Retrieves all Horses whose name contains a certain string (case-insensitive).
     */
    public List<Horse> searchHorsesByName(String partialName) {
        if (partialName == null || partialName.trim().isEmpty()) {
            return List.of(); // Return empty list for empty search
        }
        return horseRepository.findByHorseNameContainingIgnoreCase(partialName.trim());
    }

    /**
     * Retrieves all living Horses (where death day is null).
     */
    public List<Horse> findLivingHorses() {
        return horseRepository.findByHorseDeathdayIsNull();
    }

    /**
     * Retrieves all Horses of a specific breed, ignoring case.
     */
    public List<Horse> findHorsesByBreed(String breed) {
        if (breed == null || breed.trim().isEmpty()) {
            return List.of();
        }
        return horseRepository.findByHorseBreedIgnoreCase(breed.trim());
    }

    /**
     * Retrieves the count of horses belonging to a specific breed.
     */
    public long countHorsesByBreed(String breed) {
        if (breed == null || breed.trim().isEmpty()) {
            return 0;
        }
        return horseRepository.countByHorseBreed(breed.trim());
    }

    /**
     * Finds horses born between two dates.
     */
    public List<Horse> findHorsesBornBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Invalid date range provided.");
        }
        return horseRepository.findByHorseBirthdayBetween(startDate, endDate);
    }
}