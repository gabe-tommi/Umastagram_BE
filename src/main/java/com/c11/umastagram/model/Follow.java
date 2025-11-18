package com.c11.umastagram.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.Objects;

// Stores accepted followings between users
// Each record represents a user following another user
// The structure of this class is not to store the status of a follow request,
// (For that see the FriendRequest class)
// but individual instances of accepted follows
// (User 1 might follow User 2, but User 2 might not follow User 1 back)
// In this case, only one record would exist: (User 1, User 2)

public class Follow {
    // Ignore warnings about multiple @Id annotations for this class
    // This is intentional to create a composite primary key

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long userId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long friendId;

    private LocalDateTime requestTime;

    public Follow(Long userId, Long friendId, LocalDateTime requestTime) {
        this.userId = userId;
        this.friendId = friendId;
        this.requestTime = LocalDateTime.now();
    }

    public Follow() {
        this.requestTime = LocalDateTime.now();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    @Override
    public String toString() {
        return "Follow{" +
                "userId=" + userId +
                ", friendId=" + friendId +
                ", requestTime=" + requestTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Follow follow = (Follow) o;
        return Objects.equals(userId, follow.userId) && Objects.equals(friendId, follow.friendId) && Objects.equals(requestTime, follow.requestTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, friendId, requestTime);
    }
}
