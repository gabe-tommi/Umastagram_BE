package com.c11.umastagram.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FollowTest {

    @Test
    public void testFollowEntityCreation() {
        LocalDateTime now = LocalDateTime.now();
        Follow f = new Follow(1L, 2L, now);

        assertNotNull(f);
        assertEquals(1L, f.getUserId());
        assertEquals(2L, f.getFriendId());
        assertNotNull(f.getRequestTime());
    }

    @Test
    public void testFollowGettersAndSetters() {
        Follow f = new Follow();
        f.setUserId(10L);
        f.setFriendId(20L);
        LocalDateTime ts = LocalDateTime.now();
        f.setRequestTime(ts);

        assertEquals(10L, f.getUserId());
        assertEquals(20L, f.getFriendId());
        assertEquals(ts, f.getRequestTime());
    }

    @Test
    public void testEqualsAndHashCode() {
        LocalDateTime ts = LocalDateTime.now();

        Follow a = new Follow();
        a.setUserId(5L);
        a.setFriendId(6L);
        a.setRequestTime(ts);

        Follow b = new Follow();
        b.setUserId(5L);
        b.setFriendId(6L);
        b.setRequestTime(ts);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
