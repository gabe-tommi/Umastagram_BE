/*
Author: Alexangelo Orozco Gutierrez
Date: December 4, 2025
Summary: Unit tests for UmaController
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
import com.c11.umastagram.repository.UmaRepository;
import com.c11.umastagram.model.Uma;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UmaController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class UmaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UmaRepository umaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Uma testUma1;
    private Uma testUma2;

    @BeforeEach
    public void setUp() {
        testUma1 = new Uma("Uma One", "https://example.com/uma1.jpg", LocalDate.of(2020, 1, 15), "Fun fact 1");
        testUma1.setUmaId(1L);
        testUma1.setUmaIconLink("https://example.com/icon1.jpg");
        testUma1.setUmaBio("Bio for Uma One");

        testUma2 = new Uma("Uma Two", "https://example.com/uma2.jpg", LocalDate.of(2019, 5, 20), "Fun fact 2");
        testUma2.setUmaId(2L);
        testUma2.setUmaIconLink("https://example.com/icon2.jpg");
        testUma2.setUmaBio("Bio for Uma Two");
    }

    @Test
    public void testGetAllUmasSuccess() throws Exception {
        when(umaRepository.findAll()).thenReturn(Arrays.asList(testUma1, testUma2));

        mockMvc.perform(get("/api/uma"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Uma One")))
                .andExpect(jsonPath("$[0].imagePath", is("https://example.com/uma1.jpg")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Uma Two")))
                .andExpect(jsonPath("$[1].imagePath", is("https://example.com/uma2.jpg")));

        verify(umaRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllUmasEmpty() throws Exception {
        when(umaRepository.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/uma"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(umaRepository, times(1)).findAll();
    }

    @Test
    public void testGetUmaByIdSuccess() throws Exception {
        when(umaRepository.findById(1L)).thenReturn(Optional.of(testUma1));

        mockMvc.perform(get("/api/uma/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.umaId", is(1)))
                .andExpect(jsonPath("$.umaName", is("Uma One")))
                .andExpect(jsonPath("$.umaImageLink", is("https://example.com/uma1.jpg")))
                .andExpect(jsonPath("$.umaBirthday", is("2020-01-15")))
                .andExpect(jsonPath("$.funFact", is("Fun fact 1")))
                .andExpect(jsonPath("$.umaIconLink", is("https://example.com/icon1.jpg")))
                .andExpect(jsonPath("$.umaBio", is("Bio for Uma One")));

        verify(umaRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetUmaByIdNotFound() throws Exception {
        when(umaRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/uma/999"))
                .andExpect(status().isNotFound());

        verify(umaRepository, times(1)).findById(999L);
    }
}
