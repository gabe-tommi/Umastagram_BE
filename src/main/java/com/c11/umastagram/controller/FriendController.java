pacakage com.c11.umastagram.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {
    @GetMapping("/getUserFriends/{userId}")
    public List<String> getFriends() {
        List<String> friends;
        return friends;
    }
}
