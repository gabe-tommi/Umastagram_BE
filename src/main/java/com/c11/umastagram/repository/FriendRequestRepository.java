package com.c11.umastagram.repository;

import com.c11.umastagram.model.Follow;
import com.c11.umastagram.model.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    /**
     * Retrieves a FriendRequest by userRequestId and userTargetId.
     * JPQL: SELECT fr FROM FriendRequest fr WHERE fr.userRequestId = :userRequestId AND fr.userTargetId = :userTargetId
     *
     * @param userRequestId the ID of the user who sent the request
     * @param userTargetId the ID of the user who received the request
     * @return Optional<FriendRequest> containing the FriendRequest if found, or empty otherwise
     */
    @Query("SELECT fr FROM FriendRequest fr WHERE fr.userRequestId = :userRequestId AND fr.userTargetId = :userTargetId")
    Optional<FriendRequest> getFriendRequest(Long userRequestId, Long userTargetId);

    /**
     * Deletes a FriendRequest by userRequestId and userTargetId.
     * JPQL: DELETE FROM FriendRequest fr WHERE fr.userRequestId = :userRequestId AND fr.userTargetId = :userTargetId
     * @param userRequestId the ID of the user who sent the request
     * @param userTargetId the ID of the user who received the request
     * @return int number of rows affected
     */
    @Modifying
    @Query("DELETE FROM FriendRequest fr WHERE fr.userRequestId = :userRequestId AND fr.userTargetId = :userTargetId")
    int deleteFriendRequest(Long userRequestId, Long userTargetId);

    @Query("SELECT f FROM FriendRequest f WHERE f.userTargetId = :userId")
    java.util.List<FriendRequest> findAllFriendRequestsByUserId(Long userId);
}
