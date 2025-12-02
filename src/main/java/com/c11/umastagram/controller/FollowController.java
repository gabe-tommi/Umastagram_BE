package com.c11.umastagram.controller;

import com.c11.umastagram.service.FriendRequestService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import com.c11.umastagram.model.Follow;
import com.c11.umastagram.model.FollowId;
import com.c11.umastagram.service.FollowService;
import com.c11.umastagram.model.FriendRequest;
import com.c11.umastagram.model.FriendRequestId;
import com.c11.umastagram.service.FriendRequestService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FollowController {
    private final FriendRequestService friendRequestService;
    private final FollowService followService;

    public FollowController(FriendRequestService friendRequestService, FollowService followService) {
        this.friendRequestService = friendRequestService;
        this.followService = followService;
    }

    @GetMapping("/getUserFollowers/{userId}")
    public List<String> getUserFollowers(@PathVariable Long userId) {
        FollowService followService = new FollowService();
        return followService.getUserFollowers(userId);
    }

    @GetMapping("/sendFriendRequest/{userId}/{friendId}")
    public FriendRequest sendFriendRequest(@PathVariable Long userId, @PathVariable Long friendId) {
        FriendRequestService friendReqServ = new FriendRequestService();
        LocalDateTime now = LocalDateTime.now();
        FriendRequest fr = new FriendRequest(userId, friendId, now);
        return friendReqServ.saveFriendRequest(fr);
    }

    @GetMapping("/acceptFriendRequest/{userId}/{friendId}")
    public Follow acceptFriendRequest(Long userId, Long friendId) {
        LocalDateTime now = LocalDateTime.now();
        Follow follow = new Follow(userId, friendId, now);

        friendRequestService.deleteFriendRequest(userId, friendId);
        return followService.saveFollow(follow);
    }
}

/**
 * flamingo
 *             __
 *            /(`o
 *      ,-,  //  \\
 *     (,,,) ||   V
 *    (,,,,)\//
 *    (,,,/w)-'
 *    \,,/w)
 *    `V/uu
 *      / |
 *      | |
 *      o o
 *      \ |
 * \,/  ,\|,.  \,/
 *
 * https://www.asciiart.eu/animals/birds-water
 */
