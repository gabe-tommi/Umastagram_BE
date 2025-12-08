package com.c11.umastagram.service;

import com.c11.umastagram.model.Follow;
import com.c11.umastagram.model.FriendRequest;
import com.c11.umastagram.model.User;
import com.c11.umastagram.repository.FriendRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestService {
    private static final Logger logger = LoggerFactory.getLogger(FriendRequestService.class);

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserService userService;

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

    public List<String> getUserFriendRequests(Long userId) {
        List<FriendRequest> friendRequests = friendRequestRepository.findAllFriendRequestsByUserId(userId);
        List<String> friendRequestNames = new ArrayList<>();

        for (FriendRequest friendRequest : friendRequests) {
            Long currentId = friendRequest.getUserRequestId();
            Optional<User> currentUser = userService.findUserById(currentId);
            if (currentUser.isPresent()) {
                friendRequestNames.add(currentUser.get().getUsername());
            } else {
                throw new IllegalArgumentException("User Not Found, getUserFriendRequest FAIL");
            }
        }
        return friendRequestNames;
    }

}
