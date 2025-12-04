/*
Author: Alexangelo Orozco Gutierrez
Date: December 4, 2025
Summary: Contains the API functions for the Uma entity
*/

package com.c11.umastagram.controller;

import com.c11.umastagram.model.Uma;
import com.c11.umastagram.repository.UmaRepository;
import com.c11.umastagram.dto.UmaListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/uma")
public class UmaController {

    @Autowired
    private UmaRepository umaRepository;

    /**
     * Get all Umas with limited info (id, name, image_link)
     * GET /api/uma
     */
    @GetMapping
    public ResponseEntity<List<UmaListResponse>> getAllUmas() {
        List<Uma> umas = umaRepository.findAll();
        List<UmaListResponse> response = umas.stream()
                .map(uma -> new UmaListResponse(uma.getUmaId(), uma.getUmaName(), uma.getUmaIconLink()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Get Uma by ID with all information
     * GET /api/uma/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Uma> getUmaById(@PathVariable Long id) {
        Optional<Uma> uma = umaRepository.findById(id);
        if (uma.isPresent()) {
            return ResponseEntity.ok(uma.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
