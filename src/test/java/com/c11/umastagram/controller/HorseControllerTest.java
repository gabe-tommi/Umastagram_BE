/*
Author: Alexangelo Orozco Gutierrez
Date: December 4, 2025
Summary: Unit tests for HorseController
*/

package com.c11.umastagram.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.c11.umastagram.repository.HorseRepository;
import com.c11.umastagram.model.Horse;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HorseController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class HorseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HorseRepository horseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Horse testHorse1;
    private Horse testHorse2;

    @BeforeEach
    public void setUp() {
        testHorse1 = new Horse("Horse One", "https://example.com/horse1.jpg", LocalDate.of(2015, 3, 10), null);
        testHorse1.setHorseId(1L);
        testHorse1.setHorseDescription("A beautiful horse");

        testHorse2 = new Horse("Horse Two", "https://example.com/horse2.jpg", LocalDate.of(2018, 7, 22), LocalDate.of(2023, 6, 15));
        testHorse2.setHorseId(2L);
        testHorse2.setHorseDescription("Historic horse");
    }

    @Test
    public void testGetAllHorsesSuccess() throws Exception {
        when(horseRepository.findAll()).thenReturn(Arrays.asList(testHorse1, testHorse2));

        mockMvc.perform(get("/api/horse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Horse One")))
                .andExpect(jsonPath("$[0].imagePath", is("https://example.com/horse1.jpg")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Horse Two")))
                .andExpect(jsonPath("$[1].imagePath", is("https://example.com/horse2.jpg")));

        verify(horseRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllHorsesEmpty() throws Exception {
        when(horseRepository.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/horse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(horseRepository, times(1)).findAll();
    }

    @Test
    public void testGetHorseByIdSuccess() throws Exception {
        when(horseRepository.findById(1L)).thenReturn(Optional.of(testHorse1));

        mockMvc.perform(get("/api/horse/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.horseId", is(1)))
                .andExpect(jsonPath("$.horseName", is("Horse One")))
                .andExpect(jsonPath("$.horseImageLink", is("https://example.com/horse1.jpg")))
                .andExpect(jsonPath("$.horseBirthday", is("2015-03-10")))
                .andExpect(jsonPath("$.horseDeathday", nullValue()))
                .andExpect(jsonPath("$.horseDescription", is("A beautiful horse")));

        verify(horseRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetHorseByIdWithDeathday() throws Exception {
        when(horseRepository.findById(2L)).thenReturn(Optional.of(testHorse2));

        mockMvc.perform(get("/api/horse/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.horseId", is(2)))
                .andExpect(jsonPath("$.horseName", is("Horse Two")))
                .andExpect(jsonPath("$.horseImageLink", is("https://example.com/horse2.jpg")))
                .andExpect(jsonPath("$.horseBirthday", is("2018-07-22")))
                .andExpect(jsonPath("$.horseDeathday", is("2023-06-15")))
                .andExpect(jsonPath("$.horseDescription", is("Historic horse")));

        verify(horseRepository, times(1)).findById(2L);
    }

    @Test
    public void testGetHorseByIdNotFound() throws Exception {
        when(horseRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/horse/999"))
                .andExpect(status().isNotFound());

        verify(horseRepository, times(1)).findById(999L);
    }
}
