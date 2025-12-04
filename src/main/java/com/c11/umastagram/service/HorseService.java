/*
 * Author: Joceline Cortez-Arellano
 * Created: 03 December 2025
 * Date Last Modified: 03 December 2025
 * Last Modified By: Joceline Cortez-Arellano
 * Summary: Uma Service Class; Business Logic for Horse Entity
 */

package com.c11.umastagram.service;

import com.c11.umastagram.model.Horse;
import com.c11.umastagram.repository.HorseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HorseService {

    @Autowired
    private HorseRepository horseRepository;

    // Constructor for easier unit testing and explicit wiring
    public HorseService(HorseRepository horseRepository) {
        this.horseRepository = horseRepository;
    }

    /**
     * Save or update a Horse entity after lightweight validation and normalization.
     * @param horse Horse to save
     * @return persisted Horse
     * @throws IllegalArgumentException when required fields are missing or name already exists
     */
    public Horse saveHorse(Horse horse) {
        if (horse == null) {
            throw new IllegalArgumentException("Horse cannot be null");
        }

        String name = horse.getHorseName() == null ? null : horse.getHorseName().trim();
        String imageLink = horse.getHorseImageLink() == null ? null : horse.getHorseImageLink().trim();
        String description = horse.getHorseDescription() == null ? null : horse.getHorseDescription().trim();

        horse.setHorseName(name);
        horse.setHorseImageLink(imageLink);
        horse.setHorseDescription(description);

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Horse name is required");
        }
        if (imageLink == null || imageLink.isEmpty()) {
            throw new IllegalArgumentException("Horse image link is required");
        }   

        // prevent duplicate names
        if (horse.getHorseId() == null) {
            Optional<Horse> existingHorse = horseRepository.getHorseByName(name);
            if (existingHorse.isPresent()) {
                throw new IllegalArgumentException("Horse name already exists");
            }
        }
        return horseRepository.save(horse);
    }   

    /**
     * Find a Horse by its ID.
     * @param horseId ID of the Horse to find
     * @return Optional<Horse> containing the Horse if found, or empty otherwise
     */
    public Optional<Horse> findHorseById(Long horseId) {
        if (horseId == null) return Optional.empty();
        return horseRepository.getHorseById(horseId);
    }

    /**
     * Retrieve all Horses.
     * @return Optional<List<Horse>> containing all Horses, or empty if none found
     */
    public Optional<List<Horse>> findAllHorses() {
        Optional<List<Horse>> horsesOptional = horseRepository.getAllHorses();
        if (horsesOptional.isEmpty() || horsesOptional.get().isEmpty()) {
            return Optional.empty();
        }
        return horsesOptional;
    }

    /**
     * Find a Horse by its name.
     * @param horseName name of the Horse to find
     * @return Optional<Horse> containing the Horse if found, or empty otherwise
     */
    public Optional<Horse> findHorseByName(String horseName) {
        if (horseName == null || horseName.trim().isEmpty()) {
            return Optional.empty();
        }
        return horseRepository.getHorseByName(horseName.trim());
    }

    /**
     * Retrieve a Horse's image link by its ID.
     * @param horseId ID of the Horse
     * @return Optional<String> containing the image link if found, or empty otherwise
     */
    public Optional<String> getHorseImageLinkById(Long horseId) {
        if (horseId == null) return Optional.empty();
        return horseRepository.getHorseImageLinkById(horseId);
    }

    /**
     * Retrieve a Horse's description by its ID.
     * @param horseId ID of the Horse
     * @return Optional<String> containing the description if found, or empty otherwise
     */    
    public Optional<String> getHorseDescriptionById(Long horseId) {
        if (horseId == null) return Optional.empty();
        return horseRepository.getHorseDescriptionById(horseId);
    }
    /**
     * Retrieve a Horse's birthday by its ID.
     * @param horseId ID of the Horse
     * @return Optional<LocalDate> containing the birthday if found, or empty otherwise
     */
    public Optional<java.time.LocalDate> getHorseBirthdayById(Long horseId) {
        if (horseId == null) return Optional.empty();
        return horseRepository.getHorseBirthdayById(horseId);
       }

    /** 
     * Retrieve a Horse's deathday by its ID.
     * @param horseId ID of the Horse
     * @return Optional<LocalDate> containing the deathday if found, or empty otherwise
     */
    public Optional<java.time.LocalDate> getHorseDeathdayById(Long horseId) {
        if (horseId == null) return Optional.empty();
        return horseRepository.getHorseDeathdayById(horseId);
    }
}