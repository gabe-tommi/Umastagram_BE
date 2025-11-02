/*
Author: Joceline Cortez-Arellano
Date Last Modified: 29 October 2025
Summary: User Model POJO Class Unit Test
*/
package com.c11.umastagram.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.c11.umastagram.entities.Horse;


import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class HorseTest {
    @Test
    void testHorseConstructorAndGetters() {
        LocalDate birthday = LocalDate.of(2015, 3, 10);
        LocalDate deathday = LocalDate.of(2023, 7, 20);
        Horse horse = new Horse("Spirit", "http://horse.link", birthday, deathday, "Mustang");

        assertEquals("Spirit", horse.getHorseName());
        assertEquals("http://horse.link", horse.getHorseImageLink());
        assertEquals(birthday, horse.getHorseBirthday());
        assertEquals(deathday, horse.getHorseDeathday());
        assertEquals("Mustang", horse.getHorseBreed());
    }

    @Test
    void testHorseSetters() {
        Horse horse = new Horse();
        horse.setHorseId(10L);
        horse.setHorseName("Shadowfax");
        horse.setHorseImageLink("http://shadowfax.link");
        horse.setHorseBirthday(LocalDate.of(2010, 6, 1));
        horse.setHorseDeathday(LocalDate.of(2020, 12, 31));
        horse.setHorseBreed("Andalusian");

        assertEquals(10L, horse.getHorseId());
        assertEquals("Shadowfax", horse.getHorseName());
        assertEquals("http://shadowfax.link", horse.getHorseImageLink());
        assertEquals(LocalDate.of(2010, 6, 1), horse.getHorseBirthday());
        assertEquals(LocalDate.of(2020, 12, 31), horse.getHorseDeathday());
        assertEquals("Andalusian", horse.getHorseBreed());
    }
    
}
