package com.c11.umastagram.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FriendRequestTest {

    @Test
    public void testFriendRequestEntityCreation() {
        LocalDateTime now = LocalDateTime.now();
        FriendRequest fr = new FriendRequest(3L, 4L, now);

        assertNotNull(fr);
        assertEquals(3L, fr.getUserRequestId());
        assertEquals(4L, fr.getUserTargetId());
        assertNotNull(fr.getRequestTime());
    }

    @Test
    public void testFriendRequestGettersAndSetters() {
        FriendRequest fr = new FriendRequest();
        fr.setUserRequestId(7L);
        fr.setUserTargetId(8L);
        LocalDateTime ts = LocalDateTime.now();
        fr.setRequestTime(ts);

        assertEquals(7L, fr.getUserRequestId());
        assertEquals(8L, fr.getUserTargetId());
        assertEquals(ts, fr.getRequestTime());
    }

    @Test
    public void testEqualsAndHashCode() {
        LocalDateTime ts = LocalDateTime.now();

        FriendRequest a = new FriendRequest();
        a.setUserRequestId(11L);
        a.setUserTargetId(12L);
        a.setRequestTime(ts);

        FriendRequest b = new FriendRequest();
        b.setUserRequestId(11L);
        b.setUserTargetId(12L);
        b.setRequestTime(ts);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
