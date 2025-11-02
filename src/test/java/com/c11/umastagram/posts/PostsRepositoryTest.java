package com.c11.umastagram.posts;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import com.c11.umastagram.entities.Posts;
import com.c11.umastagram.entities.User;


import static org.junit.jupiter.api.Assertions.*;

public class PostsRepositoryTest {

    @Test
    public void testPostsEntityCreation() {
        Posts post = new Posts(
            "user123",
            "This is a test post",
            "https://example.com/image.jpg",
            LocalDateTime.now()
        );
        
        assertNotNull(post);
        assertEquals("user123", post.getUserId());
        assertEquals("This is a test post", post.getText());
        assertEquals("https://example.com/image.jpg", post.getImage());
        assertEquals(0, post.getLikes());
    }

    @Test
    public void testPostsGettersAndSetters() {
        Posts post = new Posts();
        
        post.setId(1L);
        post.setUserId("user456");
        post.setText("Another post");
        post.setImage("https://example.com/image2.jpg");
        LocalDateTime now = LocalDateTime.now();
        post.setDatePosted(now);
        post.setLikes(5);
        
        assertEquals(1L, post.getId());
        assertEquals("user456", post.getUserId());
        assertEquals("Another post", post.getText());
        assertEquals("https://example.com/image2.jpg", post.getImage());
        assertEquals(now, post.getDatePosted());
        assertEquals(5, post.getLikes());
    }

    @Test
    public void testPostsLikesIncrement() {
        Posts post = new Posts(
            "user123",
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
