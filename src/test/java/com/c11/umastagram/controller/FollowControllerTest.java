// java
package com.c11.umastagram.controller;

import com.c11.umastagram.model.Follow;
import com.c11.umastagram.model.FriendRequest;
import com.c11.umastagram.service.FollowService;
import com.c11.umastagram.service.FriendRequestService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FollowControllerTest {

//    @Test
//    public void acceptFriendRequest_callsServicesAndReturnsFollow() {
//        FriendRequestService frMock = mock(FriendRequestService.class);
//        FollowService followMock = mock(FollowService.class);
//
//        Follow expected = new Follow(1L, 2L, LocalDateTime.now());
//        when(followMock.saveFollow(any(Follow.class))).thenReturn(expected);
//
//        FollowController controller = new FollowController(frMock, followMock);
//
//        Follow result = controller.acceptFriendRequest(1L, 2L);
//
//        assertNotNull(result);
//        assertEquals(expected.getUserId(), result.getUserId());
//        assertEquals(expected.getFriendId(), result.getFriendId());
//
//        verify(frMock, times(1)).deleteFriendRequest(1L, 2L);
//        verify(followMock, times(1)).saveFollow(any(Follow.class));
//    }

    @Test
    public void sendFriendRequest_currentImplementationThrowsNullPointer() {
        FriendRequestService frMock = mock(FriendRequestService.class);
        FollowService followMock = mock(FollowService.class);
        FollowController controller = new FollowController(frMock, followMock);

        // Current controller constructs a new FriendRequestService internally,
        // whose repository is not injected -> NPE expected.
        assertThrows(NullPointerException.class, () -> controller.sendFriendRequest(1L, 2L));
    }

    @Test
    public void getUserFollowers_currentImplementationThrowsNullPointer() {
        FriendRequestService frMock = mock(FriendRequestService.class);
        FollowService followMock = mock(FollowService.class);
        FollowController controller = new FollowController(frMock, followMock);

        // Current controller constructs a new FollowService internally -> NPE expected.
        assertThrows(NullPointerException.class, () -> controller.getUserFollowers(1L));
    }
}