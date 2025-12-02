package com.c11.umastagram.service;

import com.c11.umastagram.model.FriendRequest;
import com.c11.umastagram.repository.FriendRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FriendRequestServiceTest {

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @InjectMocks
    private FriendRequestService friendRequestService;

    @Test
    public void saveFriendRequest_savesWhenUnique() {
        FriendRequest fr = new FriendRequest(1L, 2L, LocalDateTime.now());
        when(friendRequestRepository.getFriendRequest(1L, 2L)).thenReturn(Optional.empty());
        when(friendRequestRepository.save(any(FriendRequest.class))).thenReturn(fr);

        FriendRequest result = friendRequestService.saveFriendRequest(fr);

        assertNotNull(result);
        assertEquals(1L, result.getUserRequestId());
        assertEquals(2L, result.getUserTargetId());
        verify(friendRequestRepository, times(1)).getFriendRequest(1L, 2L);
        verify(friendRequestRepository, times(1)).save(any(FriendRequest.class));
    }

    @Test
    public void saveFriendRequest_throwsWhenDuplicate() {
        FriendRequest fr = new FriendRequest(1L, 2L, LocalDateTime.now());
        when(friendRequestRepository.getFriendRequest(1L, 2L)).thenReturn(Optional.of(fr));

        assertThrows(IllegalArgumentException.class, () -> friendRequestService.saveFriendRequest(fr));

        verify(friendRequestRepository, times(1)).getFriendRequest(1L, 2L);
        verify(friendRequestRepository, never()).save(any());
    }

    @Test
    public void deleteFriendRequest_invokesRepository() {
        doNothing().when(friendRequestRepository).deleteFriendRequest(1L, 2L);

        friendRequestService.deleteFriendRequest(1L, 2L);

        verify(friendRequestRepository, times(1)).deleteFriendRequest(1L, 2L);
    }
}
