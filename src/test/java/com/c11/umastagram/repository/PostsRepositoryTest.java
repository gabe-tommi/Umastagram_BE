package com.c11.umastagram.repository;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import com.c11.umastagram.model.Posts;
import com.c11.umastagram.model.User;


import static org.junit.jupiter.api.Assertions.*;

public class PostsRepositoryTest {

    @Test
    public void testPostsEntityCreation() {
        Posts post = new Posts(
            123L,
            "This is a test post",
            "https://example.com/image.jpg",
            LocalDateTime.now()
        );
        
        assertNotNull(post);
        assertEquals(123L, post.getUserId());
        assertEquals("This is a test post", post.getText());
        assertEquals("https://example.com/image.jpg", post.getImage());
        assertEquals(0, post.getLikes());
    }

    @Test
    public void testPostsGettersAndSetters() {
        Posts post = new Posts();
        
        post.setId(1L);
        post.setUserId(456L);
        post.setText("Another post");
        post.setImage("https://example.com/image2.jpg");
        LocalDateTime now = LocalDateTime.now();
        post.setDatePosted(now);
        post.setLikes(5);
        
        assertEquals(1L, post.getId());
        assertEquals(456L, post.getUserId());
        assertEquals("Another post", post.getText());
        assertEquals("https://example.com/image2.jpg", post.getImage());
        assertEquals(now, post.getDatePosted());
        assertEquals(5, post.getLikes());
    }

    @Test
    public void testPostsLikesIncrement() {
        Posts post = new Posts(
            123L,
            "Test post",
            null,
            LocalDateTime.now()
        );
        
        assertEquals(0, post.getLikes());
        post.setLikes(post.getLikes() + 1);
        assertEquals(1, post.getLikes());
        post.setLikes(post.getLikes() + 1);
        assertEquals(2, post.getLikes());
    }
}
