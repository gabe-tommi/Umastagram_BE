package com.c11.umastagram.posts;

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
import com.c11.umastagram.controller.PostsController;
import com.c11.umastagram.repository.PostsRepository;
import com.c11.umastagram.entities.Posts;
import com.c11.umastagram.entities.CreatePostRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostsController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class PostsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostsRepository postsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Posts testPost;
    private CreatePostRequest createPostRequest;

    @BeforeEach
    public void setUp() {
        testPost = new Posts(
            "user123",
            "This is a test post",
            "https://example.com/image.jpg",
            LocalDateTime.now()
        );
        testPost.setId(1L);

        createPostRequest = new CreatePostRequest(
            "user123",
            "This is a test post",
            "https://example.com/image.jpg"
        );
    }

    @Test
    public void testCreatePostSuccess() throws Exception {
        when(postsRepository.save(any())).thenReturn(testPost);

        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPostRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", is("user123")))
                .andExpect(jsonPath("$.text", is("This is a test post")))
                .andExpect(jsonPath("$.likes", is(0)));

        verify(postsRepository, times(1)).save(any());
    }

    @Test
    public void testCreatePostMissingUserId() throws Exception {
        CreatePostRequest invalidRequest = new CreatePostRequest(
            "",
            "This is a test post",
            "https://example.com/image.jpg"
        );

        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(postsRepository, never()).save(any());
    }

    @Test
    public void testCreatePostMissingText() throws Exception {
        CreatePostRequest invalidRequest = new CreatePostRequest(
            "user123",
            "",
            "https://example.com/image.jpg"
        );

        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(postsRepository, never()).save(any());
    }

    @Test
    public void testGetAllPosts() throws Exception {
        Posts post2 = new Posts(
            "user456",
            "Another post",
            "https://example.com/image2.jpg",
            LocalDateTime.now()
        );
        post2.setId(2L);

        when(postsRepository.findAll()).thenReturn(Arrays.asList(testPost, post2));

        mockMvc.perform(get("/api/posts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId", is("user123")))
                .andExpect(jsonPath("$[1].userId", is("user456")));

        verify(postsRepository, times(1)).findAll();
    }

    @Test
    public void testGetPostsByUser() throws Exception {
        when(postsRepository.findByUserId("user123")).thenReturn(Arrays.asList(testPost));

        mockMvc.perform(get("/api/posts/user/user123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId", is("user123")));

        verify(postsRepository, times(1)).findByUserId("user123");
    }

    @Test
    public void testGetPostById() throws Exception {
        when(postsRepository.findById(1L)).thenReturn(Optional.of(testPost));

        mockMvc.perform(get("/api/posts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is("user123")))
                .andExpect(jsonPath("$.text", is("This is a test post")));

        verify(postsRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetPostByIdNotFound() throws Exception {
        when(postsRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/posts/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(postsRepository, times(1)).findById(999L);
    }

    @Test
    public void testDeletePost() throws Exception {
        when(postsRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/posts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(postsRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeletePostNotFound() throws Exception {
        when(postsRepository.existsById(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/posts/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(postsRepository, never()).deleteById(anyLong());
    }
}
