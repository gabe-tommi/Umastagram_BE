/*
 * Author: Armando Vega
 * Created: 28 October 2025
 * Date Last Modified: 28 October 2025
 * Last Modified By: Armando Vega
 * Summary: Repository Interface for User Entity
 */
package com.c11.umastagram.repository;

import com.c11.umastagram.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Retrieves all users from the database.
     * JPQL: SELECT u FROM User u
     *
     * @return Optional<List<User>> containing a list of all users, or empty if none found
     */
    @Query("SELECT u FROM User u")
    Optional<List<User>> getAllUsers();

    /**
     * Retrieves a user by their userId.
     * JPQL: SELECT u FROM User u WHERE u.userId = :userId
     *
     * @param userId the ID of the user to retrieve
     * @return Optional<User> containing the user if found, or empty otherwise
     */
    @Query("SELECT u FROM User u WHERE u.userId = :userId")
    Optional<User> getUserByUserId(@Param("userId") Long userId);

    /**
     * Retrieves the GitHub ID of a user by their userId.
     * JPQL: SELECT u.githubId FROM User u WHERE u.userId = :userId
     *
     * @param userId the ID of the user
     * @return Optional<String> containing the GitHub ID if found, or empty otherwise
     */
    @Query("SELECT u.githubId FROM User u WHERE u.userId = :userId")
    Optional<String> getGitHubIdByUserId(@Param("userId") String userId);

    /**
     * Retrieves the GitHub username of a user by their userId.
     * JPQL: SELECT u.githubUsername FROM User u WHERE u.userId = :userId
     *
     * @param userId the ID of the user
     * @return Optional<String> containing the GitHub username if found, or empty otherwise
     */
    @Query("SELECT u.githubUsername FROM User u WHERE u.userId = :userId")
    Optional<String> getGitHubUsernameByUserId(@Param("userId") Long userId);

    /**
     * Retrieves the email of a user by their userId.
     * JPQL: SELECT u.email FROM User u WHERE u.userId = :userId
     *
     * @param userId the ID of the user
     * @return Optional<String> containing the email if found, or empty otherwise
     */
    @Query("SELECT u.email FROM User u WHERE u.userId = :userId")
    Optional<String> getEmailByUserId(@Param("userId") Long userId);

    /**
     * Retrieves the username of a user by their userId.
     * JPQL: SELECT u.username FROM User u WHERE u.userId = :userId
     *
     * @param userId the ID of the user
     * @return Optional<String> containing the username if found, or empty otherwise
     */
    @Query("SELECT u.username FROM User u WHERE u.userId = :userId")
    Optional<String> getUsernameByUserId(@Param("userId") Long userId);

    /**
     * Retrieves the password of a user by their userId.
     * JPQL: SELECT u.password FROM User u WHERE u.userId = :userId
     *
     * @param userId the ID of the user
     * @return Optional<String> containing the password if found, or empty otherwise
     */
    @Query("SELECT u.password FROM User u WHERE u.userId = :userId")
    Optional<String> getPasswordByUserId(@Param("userId") Long userId);


}
