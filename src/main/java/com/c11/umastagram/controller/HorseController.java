/*
Author: Alexangelo Orozco Gutierrez
Date: December 4, 2025
Summary: Contains the API functions for the Horse entity
*/

package com.c11.umastagram.controller;

import com.c11.umastagram.model.Horse;
import com.c11.umastagram.repository.HorseRepository;
import com.c11.umastagram.dto.HorseListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/horse")
public class HorseController {

    @Autowired
    private HorseRepository horseRepository;

    /**
     * Get all Horses with limited info (id, name, image_link)
     * GET /api/horse
     */
    @GetMapping
    public ResponseEntity<List<HorseListResponse>> getAllHorses() {
        List<Horse> horses = horseRepository.findAll();
        List<HorseListResponse> response = horses.stream()
                .map(horse -> new HorseListResponse(horse.getHorseId(), horse.getHorseName(), horse.getHorseImageLink(), horse.getHorseDescription()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Get Horse by ID with all information
     * GET /api/horse/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Horse> getHorseById(@PathVariable Long id) {
        Optional<Horse> horse = horseRepository.findById(id);
        if (horse.isPresent()) {
            return ResponseEntity.ok(horse.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
