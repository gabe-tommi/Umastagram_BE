/*
 * Author: Armando Vega
 * Created: 28 October 2025
 * Date Last Modified: 28 October 2025
 * Last Modified By: Armando Vega
 * Summary: Repository Interface for User Entity
 */
package com.c11.umastagram.repository;

import com.c11.umastagram.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Retrieves all users from the database.
     * Native SQL: SELECT * FROM user
     *
     * @return Optional<List<User>> containing a list of all users, or empty if none found
     */
    @Query(value = "SELECT * FROM user", nativeQuery = true)
    Optional<List<User>> getAllUsers();

    /**
     * Retrieves a user by their userId.
     * Native SQL: SELECT * FROM user WHERE userId = :userId
     *
     * @param userId the ID of the user to retrieve
     * @return Optional<User> containing the user if found, or empty otherwise
     */
    @Query(value = "SELECT * FROM user WHERE userId = :userId", nativeQuery = true)
    Optional<User> getUserByUserId(@Param("userId") Long userId);

    /**
     * Retrieves the GitHub ID of a user by their userId.
     * Native SQL: SELECT githubId FROM user WHERE userId = :userId
     *
     * @param userId the ID of the user
     * @return Optional<String> containing the GitHub ID if found, or empty otherwise
     */
    @Query(value = "SELECT githubId FROM user WHERE userId = :userId", nativeQuery = true)
    Optional<String> getGitHubIdByUserId(@Param("userId") String userId);

    /**
     * Retrieves the GitHub username of a user by their userId.
     * Native SQL: SELECT githubUsername FROM user WHERE userId = :userId
     *
     * @param userId the ID of the user
     * @return Optional<String> containing the GitHub username if found, or empty otherwise
     */
    @Query(value = "SELECT githubUsername FROM user WHERE userId = :userId", nativeQuery = true)
    Optional<String> getGitHubUsernameByUserId(@Param("userId") Long userId);

    /**
     * Retrieves the email of a user by their userId.
     * Native SQL: SELECT email FROM user WHERE userId = :userId
     *
     * @param userId the ID of the user
     * @return Optional<String> containing the email if found, or empty otherwise
     */
    @Query(value = "SELECT email FROM user WHERE userId = :userId", nativeQuery = true)
    Optional<String> getEmailByUserId(@Param("userId") Long userId);

    /**
     * Retrieves the username of a user by their userId.
     * Native SQL: SELECT username FROM user WHERE userId = :userId
     *
     * @param userId the ID of the user
     * @return Optional<String> containing the username if found, or empty otherwise
     */
    @Query(value = "SELECT username FROM user WHERE userId = :userId", nativeQuery = true)
    Optional<String> getUsernameByUserId(@Param("userId") Long userId);

    /**
     * Retrieves the password of a user by their userId.
     * Native SQL: SELECT password FROM user WHERE userId = :userId
     *
     * @param userId the ID of the user
     * @return Optional<String> containing the password if found, or empty otherwise
     */
    @Query(value = "SELECT password FROM user WHERE userId = :userId", nativeQuery = true)
    Optional<String> getPasswordByUserId(@Param("userId") Long userId);

    /**
     * Inserts a new user into the database and returns the generated userId.
     * Native SQL: INSERT INTO user (email, username, password, githubId, githubUsername) VALUES (:email, :username, :password, :githubId, :githubUsername) RETURNING userId
     *
     * @param email the user's email
     * @param username the user's username
     * @param password the user's password
     * @param githubId the user's GitHub ID (optional)
     * @param githubUsername the user's GitHub username (optional)
     * @return Long the generated userId of the inserted user
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user (email, username, password, githubId, githubUsername) " +
                   "VALUES (:email, :username, :password, :githubId, :githubUsername) RETURNING userId", nativeQuery = true)
    Long insertUser(@Param("email") String email,
                    @Param("username") String username,
                    @Param("password") String password,
                    @Param("githubId") String githubId,
                    @Param("githubUsername") String githubUsername);


}
