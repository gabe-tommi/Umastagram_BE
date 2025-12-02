package com.c11.umastagram.service;

import com.c11.umastagram.model.Follow;
import com.c11.umastagram.repository.FollowRepository;
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
public class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @InjectMocks
    private FollowService followService;

    @Test
    public void saveFollow_savesWhenUnique() {
        Follow f = new Follow(1L, 2L, LocalDateTime.now());
        when(followRepository.getFollow(1L, 2L)).thenReturn(Optional.empty());
        when(followRepository.save(any(Follow.class))).thenReturn(f);

        Follow result = followService.saveFollow(f);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(2L, result.getFriendId());
        verify(followRepository, times(1)).getFollow(1L, 2L);
        verify(followRepository, times(1)).save(any(Follow.class));
    }

    @Test
    public void saveFollow_throwsWhenDuplicate() {
        Follow f = new Follow(1L, 2L, LocalDateTime.now());
        when(followRepository.getFollow(1L, 2L)).thenReturn(Optional.of(f));

        assertThrows(IllegalArgumentException.class, () -> followService.saveFollow(f));
        verify(followRepository, times(1)).getFollow(1L, 2L);
        verify(followRepository, never()).save(any());
    }

    @Test
    public void deleteFollow_invokesRepository() {
        doNothing().when(followRepository).deleteFollow(1L, 2L);

        followService.deleteFollow(1L, 2L);

        verify(followRepository, times(1)).deleteFollow(1L, 2L);
    }
}
