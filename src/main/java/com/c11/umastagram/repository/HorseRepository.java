/*
    * Author: Joceline Cortez-Arellano
    * Date: 03 December 2025
    * Date Last Modified: 03 December 2025
    * Last Modified By: Joceline Cortez-Arellano
    * Summary: Repository Interface for Horse Entity
 */

package com.c11.umastagram.repository;


import com.c11.umastagram.model.Horse;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HorseRepository extends JpaRepository<Horse, Long> {

    /** Retrieves all Horse entities from DB
     * JPQL: SELECT h FROM Horse h
     * @return List<Horse> containing all Horse entities
     */
    @Query("SELECT h FROM Horse h")
    Optional<List<Horse>> getAllHorses();

    /** Retrieves Horse entity by its ID
     * JPQL: SELECT h FROM Horse h WHERE h.horseId = :horseId
     * @param horseId the ID of the Horse to retrieve
     * @return Optional<Horse> containing the Horse entity if found, or empty otherwise
     */
    @Query("SELECT h FROM Horse h WHERE h.horseId = :horseId")
    Optional<Horse> getHorseById(@Param("horseId") Long horseId);


    /** Retrieves Horse entity by its name
     * JPQL: SELECT h FROM Horse h WHERE h.horseName = :horseName
     * @param horseName the name of the Horse to retrieve
     * @return Optional<Horse> containing the Horse entity if found, or empty otherwise
     */
    @Query("SELECT h FROM Horse h WHERE h.horseName = :horseName")
    Optional<Horse> getHorseByName(@Param("horseName") String horseName);

    /** Retrieves Horse associated image link by Horse ID
     * JPQL: SELECT h.horseImageLink FROM Horse h WHERE h.horseId
     * @param horseId the ID of the Horse
     * @return Optional<String> containing the image link if found, or empty otherwise
     * 
    */
    @Query("SELECT h.horseImageLink FROM Horse h WHERE h.horseId = :horseId")
    Optional<String> getHorseImageLinkById(@Param("horseId") Long horseId);

    /** Retrieves Horse description by Horse ID
     * JPQL: SELECT h.horseDescription FROM Horse h WHERE h.horseId = :horseId
     * @param horseId the ID of the Horse
     * @return Optional<String> containing the description if found, or empty otherwise
     */    
    @Query("SELECT h.horseDescription FROM Horse h WHERE h.horseId = :horseId")
    Optional<String> getHorseDescriptionById(@Param("horseId") Long horseId);

    /** Retrieves Horse birthday by Horse ID
     * JPQL: SELECT h.horseBirthday FROM Horse h WHERE h.horseId = :horseId
     * @param horseId the ID of the Horse
     * @return Optional<LocalDate> containing the birthday if found, or empty otherwise
     */

    @Query("SELECT h.horseBirthday FROM Horse h WHERE h.horseId = :horseId")
    Optional<java.time.LocalDate> getHorseBirthdayById(@Param("horseId") Long horseId);  
    
    /** Retrieves Horse deathday by Horse ID
     * JPQL: SELECT h.horseDeathday FROM Horse h WHERE h.horseId = :horseId
     * @param horseId the ID of the Horse
     * @return Optional<LocalDate> containing the deathday if found, or empty otherwise
     */
    @Query("SELECT h.horseDeathday FROM Horse h WHERE h.horseId =:horseId")
    Optional<java.time.LocalDate> getHorseDeathdayById(@Param("horseId") Long horseId);

}
