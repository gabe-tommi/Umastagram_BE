package com.c11.umastagram.controller;

import com.c11.umastagram.model.Uma;
import com.c11.umastagram.service.UmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/uma")
public class UmaController {
    
    @Autowired
    private UmaService umaService;

    /**
     * Get all Umas
     * GET /uma/all
     * @return List of all Uma entities
     */
    @GetMapping("/all")
    public ResponseEntity<List<Uma>> getAllUmas() {
        return umaService.getAllUmas()
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.ok(List.of()));
    }

    /**
     * Search for Umas by keyword
     * GET /uma/search?keyword={keyword}
     * @param keyword search term to match against name, bio, or fun fact
     * @return List of matching Uma entities
     */
    @GetMapping("/search")
    public ResponseEntity<List<Uma>> searchUmas(@RequestParam String keyword) {
        List<Uma> results = umaService.searchUmasByKeyword(keyword);
        return ResponseEntity.ok(results);
    }
    
}
