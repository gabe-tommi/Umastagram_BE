pacakage com.c11.umastagram.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import com.c11.umastagram.model.Follow;
import com.c11.umastagram.model.FollowId;
import com.c11.umastagram.service.FollowService;
import com.c11.umastagram.model.FriendRequest;
import com.c11.umastagram.model.FriendRequestId;
import com.c11.umastagram.repository.FriendRequestService;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FollowController {
    @GetMapping("/getUserFollowers/{userId}")
    public List<String> getUserFollowers(Long userId) {
        return followService.getUserFollowers(userId);
    }

    @GetMapping("/sendFriendRequest/{userId}/{friendId}")
    public FriendRequest sendFriendRequest(Long userId, Long friendId) {
        FriendRequest fr = new FriendRequest(userId, friendId);
        return friendRequestService.saveFriendRequest(fr);
    }

    @GetMapping("/acceptFriendRequest/{userId}/{friendId}")
    public Follow acceptFriendRequest(Long userId, Long friendId) {
        // delete the friend request
        friendRequestService.deleteFriendRequest(userId, friendId);
        // create the follow record
        Follow follow = new Follow(userId, friendId);
        return followService.saveFollow(follow);
}
