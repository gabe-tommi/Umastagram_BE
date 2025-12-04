/*
    * Author: Joceline Cortez-Arellano
    * Date: 6 November 2025
    * Date Last Modified: 06 Novermber 2025
    * Last Modified By: Joceline Cortez-Arellano
    * Summary: Repository Interface for Uma Entity
 */
package com.c11.umastagram.repository;

import com.c11.umastagram.model.Uma;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UmaRepository extends JpaRepository<Uma, Long> {

    /** Retrieves all Uma entities from DB
     * JPQL: SELECT u FROM Uma u
     * @return List<Uma> containing all Uma entities
     */
    @Query("SELECT u FROM Uma u")
    Optional<List<Uma>> getAllUmas();


    /** Retrieves Uma entity by its ID
     * JPQL: SELECT u FROM Uma u WHERE u.umaId = :umaId
     * @param umaId the ID of the Uma to retrieve
     * @return Optional<Uma> containing the Uma entity if found, or empty otherwise
     */
    @Query("SELECT u FROM Uma u WHERE u.umaId = :umaId")
    Optional<Uma> getUmaById(@Param("umaId") Long umaId);


    /** Retrieves Uma entity by its name
     * JPQL: SELECT u FROM Uma u WHERE u.umaName = :umaName
     * @param umaName the name of the Uma to retrieve
     * @return Optional<Uma> containing the Uma entity if found, or empty otherwise
     */

    @Query("SELECT u FROM Uma u WHERE u.umaName = :umaName")
    Optional<Uma> getUmaByName(@Param("umaName") String umaName);


    /** Retrieves Uma associated image link by Uma ID
     * JPQL: SELECT u.umaImageLink FROM Uma u WHERE u.umaId = :umaId
     * @param umaId the ID of the Uma
     * @return Optional<String> containing the image link if found, or empty otherwise
     */
    @Query("SELECT u.umaImageLink FROM Uma u WHERE u.umaId = :umaId")
    Optional<String> getUmaImageLinkById(@Param("umaId") Long umaId);


    /** Retrieves Uma fun fact by Uma ID
     * JPQL: SELECT u.funFact FROM Uma u WHERE u.umaId = :umaId
     * @param umaId the ID of the Uma
     * @return Optional<String> containing the fun fact if found, or empty otherwise
     */
    @Query("SELECT u.funFact FROM Uma u WHERE u.umaId = :umaId")
    Optional<String> getUmaFunFactById(@Param("umaId") Long umaId);


    /** Retrieves Uma Birthday by Uma ID
     * JPQL: SELECT u.umaBirthday FROM Uma u WHERE u.umaId = :umaId
     * @param umaId the ID of the Uma
     * @return Optional<LocalDate> containing the birthday if found, or empty otherwise
     */
    @Query("SELECT u.umaBirthday FROM Uma u WHERE u.umaId = :umaId")
    Optional<java.time.LocalDate> getUmaBirthdayById(@Param("umaId") Long umaId);

    /** Retrieves Uma Icon Link by Uma ID
     * JPQL: SELECT u.umaIconLink FROM Uma u WHERE u.umaId = :umaId
     * @param umaId the ID of the Uma
     * @return Optional<String> containing the icon link if found, or empty otherwise
     */    
    @Query("SELECT u.umaIconLink FROM Uma u WHERE u.umaId = :umaId")
    Optional<String> getUmaIconLinkById(@Param("umaId") Long umaId);

    /** Retrieves Uma Bio by Uma ID
     * JPQL: SELECT u.umaBio FROM Uma u WHERE u.umaId = :umaId
     * @param umaId the ID of the Uma
     * @return Optional<String> containing the bio if found, or empty otherwise
     */    
    @Query("SELECT u.umaBio FROM Uma u WHERE u.umaId = :umaId")
    Optional<String> getUmaBioById(@Param("umaId") Long umaId);

    /** Searches for Uma entities by keyword in name, bio, or fun fact
     * JPQL: SELECT u FROM Uma u WHERE LOWER(u.umaName) LIKE LOWER(CONCAT('%', :keyword, '%'))
     *       OR LOWER(u.umaBio) LIKE LOWER(CONCAT('%', :keyword, '%'))
     *       OR LOWER(u.funFact) LIKE LOWER(CONCAT('%', :keyword, '%'))
     * @param keyword the search keyword
     * @return List<Uma> containing matching Uma entities
     */
    @Query("SELECT u FROM Uma u WHERE LOWER(u.umaName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.umaBio) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.funFact) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Uma> searchUmasByKeyword(@Param("keyword") String keyword);

}
