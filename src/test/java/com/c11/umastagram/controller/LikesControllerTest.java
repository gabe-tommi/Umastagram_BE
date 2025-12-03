package com.c11.umastagram.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.c11.umastagram.controller.LikesController;
import com.c11.umastagram.repository.LikesRepository;
import com.c11.umastagram.repository.PostsRepository;
import com.c11.umastagram.repository.UserRepository;
import com.c11.umastagram.model.Likes;
import com.c11.umastagram.model.Posts;
import com.c11.umastagram.model.User;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LikesController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class LikesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikesRepository likesRepository;

    @MockBean
    private PostsRepository postsRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private Posts testPost;
    private Likes testLike;

    @BeforeEach
    public void setUp() {
        testUser = new User("testuser", "test@example.com", "password");
        testUser.setUserId(1L);

        testPost = new Posts(1L, "Test post", "https://example.com/image.jpg", LocalDateTime.now());
        testPost.setId(1L);

        testLike = new Likes(testUser, testPost);
        testLike.setId(1L);
    }

    //tests a good run of a like
    @Test
    public void testAddLikeSuccess() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(postsRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(likesRepository.existsByUserIdAndPostId(1L, 1L)).thenReturn(false);
        when(likesRepository.save(any(Likes.class))).thenReturn(testLike);

        mockMvc.perform(post("/api/likes?userId=1&postId=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)));

        verify(userRepository, times(1)).findById(1L);
        verify(postsRepository, times(1)).findById(1L);
        verify(likesRepository, times(1)).save(any(Likes.class));
        //needs to verify that the posts like count was incremented
        verify(postsRepository, times(1)).save(any(Posts.class));
    }

    @Test
    public void testAddLikeUserNotFound() throws Exception {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/likes?userId=999&postId=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("User not found")));

        verify(userRepository, times(1)).findById(999L);
        verify(likesRepository, never()).save(any());
    }

    @Test
    public void testAddLikePostNotFound() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(postsRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/likes?userId=1&postId=999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Post not found")));

        verify(userRepository, times(1)).findById(1L);
        verify(postsRepository, times(1)).findById(999L);
        verify(likesRepository, never()).save(any());
    }

    @Test
    public void testAddLikeDuplicate() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(postsRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(likesRepository.existsByUserIdAndPostId(1L, 1L)).thenReturn(true);

        mockMvc.perform(post("/api/likes?userId=1&postId=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("User already liked this post")));

        verify(likesRepository, never()).save(any());
    }

    @Test
    public void testRemoveLikeSuccess() throws Exception {
        when(likesRepository.findByUserIdAndPostId(1L, 1L)).thenReturn(testLike);
        when(postsRepository.findById(1L)).thenReturn(Optional.of(testPost));

        mockMvc.perform(delete("/api/likes/user/1/post/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(likesRepository, times(1)).findByUserIdAndPostId(1L, 1L);
        verify(likesRepository, times(1)).delete(testLike);
        verify(postsRepository, times(1)).findById(1L);
        verify(postsRepository, times(1)).save(any(Posts.class));
    }

    @Test
    public void testRemoveLikeNotFound() throws Exception {
        when(likesRepository.findByUserIdAndPostId(999L, 999L)).thenReturn(null);

        mockMvc.perform(delete("/api/likes/user/999/post/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(likesRepository, times(1)).findByUserIdAndPostId(999L, 999L);
        verify(likesRepository, never()).delete(any());
    }

    @Test
    public void testGetLikesForPost() throws Exception {
        User user2 = new User("testuser2", "test2@example.com", "password");
        user2.setUserId(2L);
        Likes like2 = new Likes(user2, testPost);
        like2.setId(2L);

        when(likesRepository.findByPostId(1L)).thenReturn(Arrays.asList(testLike, like2));

        mockMvc.perform(get("/api/likes/post/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));

        verify(likesRepository, times(1)).findByPostId(1L);
    }

    @Test
    public void testGetLikesForPostEmpty() throws Exception {
        when(likesRepository.findByPostId(999L)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/likes/post/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(likesRepository, times(1)).findByPostId(999L);
    }

    @Test
    public void testGetLikesByUser() throws Exception {
        Posts post2 = new Posts(1L, "Another post", "https://example.com/image2.jpg", LocalDateTime.now());
        post2.setId(2L);
        Likes like2 = new Likes(testUser, post2);
        like2.setId(2L);

        when(likesRepository.findByUserId(1L)).thenReturn(Arrays.asList(testLike, like2));

        mockMvc.perform(get("/api/likes/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));

        verify(likesRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void testGetLikesByUserEmpty() throws Exception {
        when(likesRepository.findByUserId(999L)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/likes/user/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(likesRepository, times(1)).findByUserId(999L);
    }
}
