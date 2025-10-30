/*
Author: Joceline Cortez-Arellano
Date Last Modified: 29 October 2025
Summary: Contains the uma table and getters/setters for all information
*/
package com.c11.umastagram.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class UmaTest {
    @Test
    void testUmaConstructorAndGetters() {
        LocalDate birthday = LocalDate.of(2020, 5, 15);
        Uma uma = new Uma("UmaName", "http://image.link", birthday, "Loves carrots");

        assertEquals("UmaName", uma.getUmaName());
        assertEquals("http://image.link", uma.getUmaImageLink());
        assertEquals(birthday, uma.getUmaBirthday());
        assertEquals("Loves carrots", uma.getFunFact());
    }

    @Test
    void testUmaSetters() {
        Uma uma = new Uma();
        uma.setUmaId(1L);
        uma.setUmaName("UmaTest");
        uma.setUmaImageLink("http://test.link");
        uma.setUmaBirthday(LocalDate.of(2021, 1, 1));
        uma.setFunFact("Sleeps standing up");

        assertEquals(1L, uma.getUmaId());
        assertEquals("UmaTest", uma.getUmaName());
        assertEquals("http://test.link", uma.getUmaImageLink());
        assertEquals(LocalDate.of(2021, 1, 1), uma.getUmaBirthday());
        assertEquals("Sleeps standing up", uma.getFunFact());
    
        }
};
