package com.c11.umastagram.repository;

import com.c11.umastagram.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    /**
     * Retrieves a Follow by userId and friendId.
     * JPQL: SELECT f FROM Follow f WHERE f.userId = :userId AND
     * @param userId
     * @param friendId
     * @return Optional<Follow> containing the Follow if found, or empty otherwise
     */
    @Query("SELECT f FROM Follow f WHERE f.userId = :userId AND f.friendId = :friendId")
    Optional<Follow> getFollow(Long userId, Long friendId);

    
    /**
     * Deletes a Follow by userId and friendId.
     * JPQL: DELETE FROM Follow f WHERE f.userId = :userId AND f.friendId = :friendId
     * @param userId
     * @param friendId
     * @return int number of rows affected
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Follow f WHERE f.userId = :userId AND f.friendId = :friendId")
    int deleteFollow(Long userId, Long friendId);

    @Query("SELECT f FROM Follow f WHERE f.friendId = :userId")
    java.util.List<Follow> findAllFollowersByUserId(Long userId);


}
