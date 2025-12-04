    /*
Author: Joceline Cortez-Arellano
Date Last Modified: 29 October 2025
Summary: User Model POJO Class Unit Test
*/

package com.c11.umastagram.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "uma")
public class Uma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uma_id", nullable = false, unique = true)
    private Long umaId;

    @Column(name = "uma_name", length = 255)
    private String umaName;

    @Column(name = "uma_image_link", columnDefinition = "TEXT", nullable = false)
    private String umaImageLink;

    @Column(name = "uma_birthday")
    private LocalDate umaBirthday;

    @Column(name = "fun_fact", length = 255)
    private String funFact;

    @Column(name = "uma_icon", length = 255)
    private String umaIconLink;

    @Column(name = "uma_bio", length = 255)
    private String umaBio;

    // Constructors
    public Uma() {}

    public Uma(String umaName, String umaImageLink, LocalDate umaBirthday, String funFact) {
        this.umaName = umaName;
        this.umaImageLink = umaImageLink;
        this.umaBirthday = umaBirthday;
        this.funFact = funFact;
    }

    // Getters and Setters
    public Long getUmaId() {
        return umaId;
    }

    public void setUmaId(Long umaId) {
        this.umaId = umaId;
    }

    public String getUmaName() {
        return umaName;
    }

    public void setUmaName(String umaName) {
        this.umaName = umaName;
    }

    public String getUmaImageLink() {
        return umaImageLink;
    }

    public void setUmaImageLink(String umaImageLink) {
        this.umaImageLink = umaImageLink;
    }

    public LocalDate getUmaBirthday() {
        return umaBirthday;
    }

    public void setUmaBirthday(LocalDate umaBirthday) {
        this.umaBirthday = umaBirthday;
    }

    public String getFunFact() {
        return funFact;
    }

    public void setFunFact(String funFact) {
        this.funFact = funFact;
    }
    public String getUmaIconLink() {
        return umaIconLink;
    }
    public void setUmaIconLink(String umaIconLink) {
        this.umaIconLink = umaIconLink;
    }

    public String getUmaBio() {
        return umaBio;
    }
    public void setUmaBio(String umaBio) {
        this.umaBio = umaBio;
    }
}


