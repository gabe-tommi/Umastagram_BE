/*
Author: Joceline Cortez-Arellano
Date Last Modified: 29 October 2025
Summary: Contains the horse table and getters/setters for all information
*/
package com.c11.umastagram.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "horse")
public class Horse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "horse_id", nullable = false, unique = true)
    private Long horseId;

    @Column(name = "horse_name", length = 255)
    private String horseName;

    @Column(name = "horse_image_link", columnDefinition = "TEXT", nullable = false)
    private String horseImageLink;

    @Column(name = "horse_birthday")
    private LocalDate horseBirthday;

    @Column(name = "horse_deathday")    
    private LocalDate horseDeathday;

    @Column(name = "horse_desc")
    private String horseDescription;


    // Constructors
    public Horse() {}

    public Horse(String horseName, String horseImageLink, LocalDate horseBirthday, LocalDate horseDeathday) {
        this.horseName = horseName;
        this.horseImageLink = horseImageLink;
        this.horseBirthday = horseBirthday;
        this.horseDeathday = horseDeathday;
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

    public String getHorseDescription() {
        return horseDescription;
    }

    public void setHorseDescription(String horseDescription) {
        this.horseDescription = horseDescription;
    }
}
