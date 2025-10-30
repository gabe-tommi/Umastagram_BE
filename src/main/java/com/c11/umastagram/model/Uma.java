/*
Author: Joceline Cortez-Arellano
Date: October 29, 2025
Summary: Contains the uma table and getters/setters for all information
*/

package com.c11.umastagram.uma;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "uma_table")
public class Uma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long umaId;

    @Column(length = 255)
    private String umaName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String umaImageLink;

    @Column
    private LocalDate umaBirthday;

    @Column(length = 255)
    private String funFact;

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
}


