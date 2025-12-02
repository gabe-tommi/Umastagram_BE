package com.c11.umastagram.service;

import com.c11.umastagram.model.Follow;
import com.c11.umastagram.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.c11.umastagram.model.User;
import com.c11.umastagram.service.UserService;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class FollowService {
    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserService userService;

    public Follow saveFollow(Follow follow) {
        if (follow.getUserId() != null &&
                follow.getFriendId() != null &&
                followRepository.getFollow(follow.getUserId(), follow.getFriendId()).isPresent()){
            throw new IllegalArgumentException("Unique Follow Record Already Exists");
        }
        return followRepository.save(follow);
    }

    public void deleteFollow(Long userId, Long friendId) {
        followRepository.deleteFollow(userId, friendId);
    }

    public List<String> getUserFollowers(Long userId) {
        List<Follow> followers = followRepository.findAllFollowersByUserId(userId);
        List<String> followerNames = new ArrayList<>();

        for (Follow follower : followers) {
            Long currentId = follower.getUserId();
            Optional<User> currentUser = userService.findUserById(currentId);
            if (currentUser.isPresent()) {
                followerNames.add(currentUser.get().getUsername());
            } else {
                throw new IllegalArgumentException("User Not Found, getUserFollowers FAIL");
            }
        }
        return followerNames;
    }
}