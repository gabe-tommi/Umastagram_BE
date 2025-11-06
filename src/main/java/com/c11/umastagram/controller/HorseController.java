/*
Author: Gabe Gallagher
Date Last Modified: 6 November 2025
Summary: Controller Class that maps routes to service functions
*/

package com.c11.umastagram.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.c11.umastagram.model.Horse;
import com.c11.umastagram.service.HorseService;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/horse/")
public class HorseController {

	@GetMapping("/getAllHorses")
	public ResponseEntity<List<Horse>> getAllHorses() {
		try {
            List<Horse> horses = HorseService.findAllHorses();
            return ResponseEntity.ok(horses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
	}

	@PostMapping("/saveHorse/{horseId}")
	public ResponseEntity saveHorse(@PathVariable Long horseId) {
		try {
			Horse inputHorse = HorseService.findHorseById(horseId);
            HorseService.save(inputHorse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
	}

}