package com.c11.umastagram.service;

import com.c11.umastagram.model.FriendRequest;
import com.c11.umastagram.repository.FriendRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendRequestService {
    private static final Logger logger = LoggerFactory.getLogger(FriendRequestService.class);

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    public FriendRequest saveFriendRequest(FriendRequest fr) {
        // no data formatting is really necessary here,
        // all this table stores are ids and a timestamp
        // and the ids in the composite key can be duplicates
        // only the combination has to be unique

        if (fr.getUserRequestId() != null &&
                fr.getUserTargetId() != null &&
                friendRequestRepository.getFriendRequest(fr.getUserRequestId(), fr.getUserTargetId()).isPresent()){
            throw new IllegalArgumentException("Unique Friend Request Already Exists");
        }
        return friendRequestRepository.save(fr);
    }

    public void deleteFriendRequest(Long userRequestId, Long userTargetId) {
        friendRequestRepository.deleteFriendRequest(userRequestId, userTargetId);
        logger.info("Deleted Friend Request from user {} to user {}", userRequestId, userTargetId);
    }

}
