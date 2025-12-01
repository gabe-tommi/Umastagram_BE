package com.c11.umastagram.service;

import com.c11.umastagram.model.Follow;
import com.c11.umastagram.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.c11.umastagram.model.User;
import com.c11.umastagram.service.UserService;
import java.util.List;
import java.util.ArrayList;

@Service
public class FollowService {
    @Autowired
    private FollowRepository followRepository;

    public Follow saveFollow(Follow follow) {
        // no data formatting is really necessary here,
        // all this table stores are ids and a timestamp
        // and the ids in the composite key can be duplicates
        // only the combination has to be unique

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
        for(int i = 0; i < followers.size(); i++) {
            UserService.getUserById(followers.get(i).getUserId()).ifPresent(user -> {
                followerNames.add(user.getUsername());
            });
        }
    }
}
