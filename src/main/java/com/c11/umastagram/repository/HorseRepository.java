/*
Author: Gabe Gallagher
Date: Nov 6, 2025
Summary: DAO for Horse entity
*/

package com.c11.umastagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.c11.umastagram.model.Horse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface HorseRepository extends JpaRepository<Horse, Long> {

    // --- 1. horseId (Primary Key) ---

    /**
     * Retrieves Horse from table by ID
     * JPQL: SELECT h FROM Horse h WHERE h.horseId = :horseId
     */
    @Query("SELECT h FROM Horse h WHERE h.horseId = :horseId")
    Optional<Horse> findHorseById(@Param("horseId") Long horseId);

    // --- 2. horseName (String) ---

    /** Find all Horses with a specific name (exact match) */
    @Query("SELECT h FROM Horse h WHERE h.horseName = :horseName")
    List<Horse> findByHorseName(@Param("horseName") String horseName);

    /** Find all Horses whose name contains a certain string (case-insensitive) */
    @Query("SELECT h FROM Horse h WHERE LOWER(h.horseName) LIKE LOWER(CONCAT('%', :partialName, '%'))")
    List<Horse> findByHorseNameContainingIgnoreCase(@Param("partialName") String partialName);

    /** Find all Horses with names starting with a specific prefix */
    @Query("SELECT h FROM Horse h WHERE LOWER(h.horseName) LIKE LOWER(CONCAT(:prefix, '%'))")
    List<Horse> findByHorseNameStartingWithIgnoreCase(@Param("prefix") String prefix);

    // --- 3. horseImageLink (String) ---

    /** Find a Horse by its unique image link */
    @Query("SELECT h FROM Horse h WHERE h.horseImageLink = :link")
    Optional<Horse> findByHorseImageLink(@Param("link") String horseImageLink);

    // --- 4. horseBirthday (LocalDate) ---

    /** Find all Horses born on a specific date (exact match) */
    @Query("SELECT h FROM Horse h WHERE h.horseBirthday = :birthday")
    List<Horse> findByHorseBirthday(@Param("birthday") LocalDate horseBirthday);

    /** Find all Horses born after a specific date */
    @Query("SELECT h FROM Horse h WHERE h.horseBirthday > :date")
    List<Horse> findByHorseBirthdayAfter(@Param("date") LocalDate date);

    /** Find all Horses born between two dates */
    @Query("SELECT h FROM Horse h WHERE h.horseBirthday BETWEEN :startDate AND :endDate")
    List<Horse> findByHorseBirthdayBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // --- 5. horseDeathday (LocalDate) ---

    /** Find all Horses with a recorded death day */
    @Query("SELECT h FROM Horse h WHERE h.horseDeathday IS NOT NULL")
    List<Horse> findByHorseDeathdayIsNotNull();
    
    /** Find all currently living Horses (where death day is null) */
    @Query("SELECT h FROM Horse h WHERE h.horseDeathday IS NULL")
    List<Horse> findByHorseDeathdayIsNull();

    /** Find all Horses that died before a specific date */
    @Query("SELECT h FROM Horse h WHERE h.horseDeathday < :date")
    List<Horse> findByHorseDeathdayBefore(@Param("date") LocalDate date);
    
    // --- 6. horseBreed (String) ---

    // /** Find all Horses of a specific breed (exact match) */
    // @Query("SELECT h FROM Horse h WHERE h.horseBreed = :breed")
    // List<Horse> findByHorseBreed(@Param("breed") String horseBreed);

    // /** Find all Horses of a specific breed, ignoring case */
    // @Query("SELECT h FROM Horse h WHERE LOWER(h.horseBreed) = LOWER(:breed)")
    // List<Horse> findByHorseBreedIgnoreCase(@Param("breed") String horseBreed);

    // // /** Find all Horses whose breed name contains a certain string (case-insensitive) */
    // @Query("SELECT h FROM Horse h WHERE LOWER(h.horseBreed) LIKE LOWER(CONCAT('%', :partialBreed, '%'))")
    // List<Horse> findByHorseBreedContainingIgnoreCase(@Param("partialBreed") String partialBreed);

    // // --- 7. Combined Queries (Examples) ---

    // /** Find all Horses of a specific breed born after a certain date */
    // @Query("SELECT h FROM Horse h WHERE h.horseBreed = :breed AND h.horseBirthday > :date")
    // List<Horse> findByHorseBreedAndHorseBirthdayAfter(@Param("breed") String horseBreed, @Param("date") LocalDate horseBirthday);

    /** Find all Horses with a name containing a string OR of a specific breed */
    // @Query("SELECT h FROM Horse h WHERE LOWER(h.horseName) LIKE LOWER(CONCAT('%', :partialName, '%')) OR h.horseBreed = :breed")






}
    // List<Horse> findByHorseNameContainingOrHorseBreed(@Param("partialName")