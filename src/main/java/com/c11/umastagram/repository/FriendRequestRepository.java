package com.c11.umastagram.repository;

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
    Optional<FriendRequest> getFriendRequestByUserRequestIdAndUserTargetId(Long userRequestId, Long userTargetId);

    /**
     * Deletes a FriendRequest by userRequestId and userTargetId.
     * JPQL: DELETE FROM FriendRequest fr WHERE fr.userRequestId = :userRequestId AND fr.userTargetId = :userTargetId
     * @param userRequestId the ID of the user who sent the request
     * @param userTargetId the ID of the user who received the request
     */
    @Modifying
    @Query("DELETE FROM FriendRequest fr WHERE fr.userRequestId = :userRequestId AND fr.userTargetId = :userTargetId")
    Optional<FriendRequest> deleteFriendRequest(Long userRequestId, Long userTargetId);
}
