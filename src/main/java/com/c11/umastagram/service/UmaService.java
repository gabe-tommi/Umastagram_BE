/*
 * Author: Joceline Cortez-Arellano
 * Created: 13 November 2025
 * Date Last Modified: 13 November 2025
 * Last Modified By: Joceline Cortez-Arellano
 * Summary: Uma Service Class; Business Logic for Uma Entity
 */

 /* This service calls methods defined in the User repository to indirectly execute SQL on the DB safely. 
 It might have a similarly named method, like GetUmaByRace(string eventName), which might call FindUmaById(id) defined in the service to function correctly.

Out of the two functions, 
ONLY the function defined in the repo should directly modify the DB. Sending queries directly to the DB from the service would violate RESTfulness, and make code harder to read.

The purpose of these functions is to be called in the User Controller for whatever it needs to do for each route defined there. All functions in the service should never query the DB directly, 
and only do so by calling repository functions.
 */
package com.c11.umastagram.service;

import com.c11.umastagram.model.Uma;
import com.c11.umastagram.repository.UmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Uma-related business logic.
 *
 * Responsibilities:
 * - Validate incoming Uma payloads
 * - Delegate all DB access to the UmaRepository
 * - Keep controllers thin and focused on HTTP concerns
 */
@Service
public class UmaService {

    @Autowired
    private UmaRepository umaRepository;

    // Constructor for easier unit testing and explicit wiring
    public UmaService(UmaRepository umaRepository) {
        this.umaRepository = umaRepository;
    }

    /**
     * Save or update a Uma entity after lightweight validation and normalization.
     * @param uma Uma to save
     * @return persisted Uma
     * @throws IllegalArgumentException when required fields are missing or name already exists
     */
    public Uma saveUma(Uma uma) {
        if (uma == null) {
            throw new IllegalArgumentException("Uma cannot be null");
        }

        String name = uma.getUmaName() == null ? null : uma.getUmaName().trim();
        String imageLink = uma.getUmaImageLink() == null ? null : uma.getUmaImageLink().trim();
        String funFact = uma.getFunFact() == null ? null : uma.getFunFact().trim();
        String iconLink = uma.getUmaIconLink() == null ? null : uma.getUmaIconLink().trim();

        uma.setUmaName(name);
        uma.setUmaImageLink(imageLink);
        uma.setFunFact(funFact);
        uma.setUmaIconLink(iconLink);
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Uma name is required");
        }
        if (imageLink == null || imageLink.isEmpty()) {
            throw new IllegalArgumentException("Uma image link is required");
        }
        if (iconLink == null || iconLink.isEmpty()) {
            throw new IllegalArgumentException("Uma icon link is required");
        }

        // prevent duplicate names for new entities
        if (uma.getUmaId() == null) {
            Optional<Uma> existing = umaRepository.getUmaByName(name);
            if (existing.isPresent()) {
                throw new IllegalArgumentException("Uma with the same name already exists");
            }
        }

        return umaRepository.save(uma);
    }

    /**
     * Find an Uma by id.
     * @param id Uma ID
     * @return Optional containing Uma if found, else empty 
     */
    public Optional<Uma> findUmaById(Long id) {
        if (id == null) return Optional.empty();
        return umaRepository.getUmaById(id);
    }

    /**
     * Retrieve all Umas.
     * @return Optional containing list of Umas if any exist, else empty
     */
    public Optional<List<Uma>> getAllUmas() {
        return umaRepository.getAllUmas().filter(list -> list != null && !list.isEmpty());
    }

    /**
     * Find an Uma by name.
     * @param name Uma name
     * @return Optional containing Uma if found, else empty
     */
    public Optional<Uma> findUmaByName(String name) {
        if (name == null) return Optional.empty();
        return umaRepository.getUmaByName(name.trim());
    }

    /**
     * Find an Uma's image link by id.
     * @param id Uma ID
     * @return Optional containing Uma if found, else empty
     * Retrieve an Uma's image link by id.
     */
    public Optional<String> getUmaImageLinkById(Long id) {
        if (id == null) return Optional.empty();
        return umaRepository.getUmaImageLinkById(id);
    }

    /**
     * Retrieve an Uma's fun fact by id.
     * @param id Uma ID
     * @return Optional containing Uma if found, else empty
     */
    public Optional<String> getUmaFunFactById(Long id) {
        if (id == null) return Optional.empty();
        return umaRepository.getUmaFunFactById(id);
    }

    /**
     * Retrieve an Uma's birthday by id.
     * @param id Uma ID
     * @return Optional containing Uma if found, else empty
     */
    public Optional<java.time.LocalDate> getUmaBirthdayById(Long id) {
        if (id == null) return Optional.empty();
        return umaRepository.getUmaBirthdayById(id);
    }

    /**
     * Delete an Uma by id.
     * Returns a human-friendly message for controllers to return.
     */
    @Transactional
    public String deleteUmaById(Long id) {
        if (id == null) return "Invalid id";
        Optional<Uma> umaOpt = umaRepository.getUmaById(id);
        if (umaOpt.isPresent()) {
            umaRepository.delete(umaOpt.get());
            return "Uma '" + umaOpt.get().getUmaName() + "' deleted successfully";
        }
        return "Uma not found";
    }

    /**
     * @param id Uma ID
     * @return Optional containing Uma if found, else empty
     * Retrieve an Uma's icon link by id.
     */
    public Optional<String> getUmaIconLinkById(Long id) {
        if (id == null) return Optional.empty();
        return umaRepository.getUmaIconLinkById(id);
    }

    /** Retrieve an Uma's bio by id. 
     * @param id Uma ID
     * @return Optional containing Uma if found, else empty
    */
    public Optional<String> getUmaBioById(Long id) {
        if (id == null) return Optional.empty();
        return umaRepository.getUmaBioById(id);
    }

}