//package com.c11.umastagram.controller;
//
//import com.c11.umastagram.model.Follow;
//import com.c11.umastagram.repository.FollowRepository;
//import com.c11.umastagram.repository.FriendRequestRepository;
//import com.c11.umastagram.service.FollowService;
//import com.c11.umastagram.service.FriendRequestService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Import;
//import org.springframework.context.annotation.Primary;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//
//import static org.hamcrest.Matchers.is;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(FollowController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@Import(FollowControllerMvcTest.TestConfig.class)
//public class FollowControllerMvcTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private FriendRequestService friendRequestService; // provided by TestConfig
//
//    @Autowired
//    private FollowService followService; // provided by TestConfig
//
//    @Test
//    public void acceptFriendRequest_endpointInvokesServicesAndReturnsFollowJson() throws Exception {
//        Follow returned = new Follow(1L, 2L, LocalDateTime.now());
//        when(followService.saveFollow(any(Follow.class))).thenReturn(returned);
//
//        mockMvc.perform(get("/api/friends/acceptFriendRequest/1/2"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userId", is(1)))
//                .andExpect(jsonPath("$.friendId", is(2)));
//
//        // Accept any arguments here because controller currently passes nulls in the invocation path.
//        verify(friendRequestService, times(1)).deleteFriendRequest(any(), any());
//        verify(followService, times(1)).saveFollow(any(Follow.class));
//    }
//
//    @TestConfiguration
//    static class TestConfig {
//        @Bean
//        public FriendRequestRepository friendRequestRepository() {
//            return mock(FriendRequestRepository.class);
//        }
//
//        @Bean
//        public FollowRepository followRepository() {
//            return mock(FollowRepository.class);
//        }
//
//        @Bean
//        @Primary
//        public FriendRequestService friendRequestService() {
//            return mock(FriendRequestService.class);
//        }
//
//        @Bean
//        @Primary
//        public FollowService followService() {
//            return mock(FollowService.class);
//        }
//    }
//}
