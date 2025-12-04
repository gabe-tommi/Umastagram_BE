package com.c11.umastagram.model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;



public class CommentTest {
    @Test
    void testGettersAndSetters() {
        Comment comment = new Comment();

        // Set values
        comment.setCommentId(1);
        comment.setUserId(100);
        comment.setPostId(200);
        comment.setCommentText("This is a test comment");

        // Verify values
        assertEquals(1, comment.getCommentId());
        assertEquals(100, comment.getUserId());
        assertEquals(200, comment.getPostId());
        assertEquals("This is a test comment", comment.getCommentText());
    }

    @Test
    void testDefaultValues() {
        Comment comment = new Comment();

        // By default, fields should be uninitialized (0 for int, null for String)
        assertEquals(0, comment.getCommentId());
    }
}