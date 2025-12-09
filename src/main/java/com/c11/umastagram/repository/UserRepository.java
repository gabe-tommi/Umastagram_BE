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
     * Retrieves a user by their email.
     * JPQL: SELECT u FROM User u WHERE u.email = :email
     *
     * @param email the email of the user to retrieve
     * @return Optional<User> containing the user if found, or empty otherwise
     */
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> getUserByEmail(@Param("email") String email);

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

    /**
     * Deletes a user by their userId.
     * JPQL: DELETE FROM User u WHERE u.userId = :userId
     *
     * @param userId the ID of the user to delete
     * @return number of rows affected
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.userId = :userId")
    int deleteUserByUserId(@Param("userId") Long userId);

    /**
     * Retrieves a user by their GitHub ID.
     * JPQL: SELECT u FROM User u WHERE u.githubId = :githubId
     *
     * @param githubId the GitHub ID of the user to retrieve
     * @return Optional<User> containing the user if found, or empty otherwise
     */    
    @Query("SELECT u FROM User u WHERE u.githubId = :githubId")
    Optional<User> getUserByGitHubId(@Param("githubId") String githubId);

    /**
     * Retrieves a user by their GitHub username.
     * JPQL: SELECT u FROM User u WHERE u.githubUsername = :githubUsername
     *
     * @param githubUsername the GitHub username of the user to retrieve
     * @return Optional<User> containing the user if found, or empty otherwise
     */
    @Query("SELECT u FROM User u WHERE u.githubUsername = :githubUsername")
    Optional<User> getUserByGitHubUsername(@Param("githubUsername") String githubUsername);

    /**
     * Retrieves a user by their Google ID.
     * JPQL: SELECT u FROM User u WHERE u.googleId = :googleId
     *
     * @param googleId the Google ID of the user to retrieve
     * @return Optional<User> containing the user if found, or empty otherwise
     */    
    @Query("SELECT u FROM User u WHERE u.googleId = :googleId")
    Optional<User> getUserByGoogleId(@Param("googleId") String googleId);

    /**
     * Retrieves a user by their Google username.
     * JPQL: SELECT u FROM User u WHERE u.googleUsername = :googleUsername
     *
     * @param googleUsername the Google username of the user to retrieve
     * @return Optional<User> containing the user if found, or empty otherwise
     */
    @Query("SELECT u FROM User u WHERE u.googleUsername = :googleUsername")
    Optional<User> getUserByGoogleUsername(@Param("googleUsername") String googleUsername);

    /**
     * Sets the email of a user by their userId.
     * JPQL: UPDATE User u SET u.email = :email WHERE u.userId = :userId
     * @param email the new email to set
     * @param userId the ID of the user
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.email = :email WHERE u.userId = :userId")
    void setEmailByUserId(@Param("email") String email, @Param("userId") Long userId);

    /**
     * Sets the GitHub ID of a user by their userId.
     * JPQL: UPDATE User u SET u.githubId = :githubId WHERE u.userId = :userId
     * @param githubId the new GitHub ID to set
     * @param userId the ID of the user
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.githubId = :githubId WHERE u.userId = :userId")
    void setGitHubIdByUserId(@Param("githubId") String githubId, @Param("userId") Long userId);

    /**
     * Sets the GitHub username of a user by their userId.
     * JPQL: UPDATE User u SET u.githubUsername = :githubUsername WHERE u.userId = :userId
     * @param githubUsername the new GitHub username to set
     * @param userId the ID of the user
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.githubUsername = :githubUsername WHERE u.userId = :userId")
    void setGitHubUsernameByUserId(@Param("githubUsername") String githubUsername, @Param("userId") Long userId);

    /**
     * Sets the username of a user by their userId.
     * JPQL: UPDATE User u SET u.username = :username WHERE u.userId = :userId
     * @param username the new username to set
     * @param userId the ID of the user
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.username = :username WHERE u.userId = :userId")
    void setUsernameByUserId(@Param("username") String username, @Param("userId") Long userId);

    /**
     * Sets the password of a user by their userId.
     * JPQL: UPDATE User u SET u.password = :password WHERE u.userId = :userId
     * @param password the new password to set
     * @param userId the ID of the user
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password WHERE u.userId = :userId")
    void setPasswordByUserId(@Param("password") String password, @Param("userId") Long userId);

    /**
     * Retrieves a user by their username.
     * JPQL: SELECT u FROM User u WHERE u.username = :username
     *
     * @param username the username of the user to retrieve
     * @return Optional<User> containing the user if found, or empty otherwise
     */
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> getUserByUsername(@Param("username") String username);

    /**
     * Finds a user by their provider and provider ID (GitHub or Google).
     * @param provider The OAuth provider (e.g., "github", "google")
     * @param providerId The provider-specific user ID
     * @return Optional<User> containing the user if found, or empty otherwise
     */
    @Query("SELECT u FROM User u WHERE u.provider = :provider AND (u.googleId = :providerId OR u.githubId = :providerId)")
    Optional<User> findByProviderAndProviderId(@Param("provider") String provider, @Param("providerId") String providerId);

    List<User> findUserByUsernameContaining(String username);
}
