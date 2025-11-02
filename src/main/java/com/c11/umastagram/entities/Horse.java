/*
Author: Joceline Cortez-Arellano
Date Last Modified: 29 October 2025
Summary: Contains the horse table and getters/setters for all information
*/
package com.c11.umastagram.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "horse")
public class Horse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long horseId;

    @Column(length = 255)
    private String horseName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String horseImageLink;

    @Column
    private LocalDate horseBirthday;

    @Column
    private LocalDate horseDeathday;

    @Column(length = 255)
    private String horseBreed;

    // Constructors
    public Horse() {}

    public Horse(String horseName, String horseImageLink, LocalDate horseBirthday, LocalDate horseDeathday, String horseBreed) {
        this.horseName = horseName;
        this.horseImageLink = horseImageLink;
        this.horseBirthday = horseBirthday;
        this.horseDeathday = horseDeathday;
        this.horseBreed = horseBreed;
    }

    // Getters and Setters
    public Long getHorseId() {
        return horseId;
    }

    public void setHorseId(Long horseId) {
        this.horseId = horseId;
    }

    public String getHorseName() {
        return horseName;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public String getHorseImageLink() {
        return horseImageLink;
    }

    public void setHorseImageLink(String horseImageLink) {
        this.horseImageLink = horseImageLink;
    }

    public LocalDate getHorseBirthday() {
        return horseBirthday;
    }

    public void setHorseBirthday(LocalDate horseBirthday) {
        this.horseBirthday = horseBirthday;
    }

    public LocalDate getHorseDeathday() {
        return horseDeathday;
    }

    public void setHorseDeathday(LocalDate horseDeathday) {
        this.horseDeathday = horseDeathday;
    }

    public String getHorseBreed() {
        return horseBreed;
    }

    public void setHorseBreed(String horseBreed) {
        this.horseBreed = horseBreed;
    } 
}
