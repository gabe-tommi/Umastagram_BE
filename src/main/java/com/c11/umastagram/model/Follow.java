package com.c11.umastagram.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "follow")
@IdClass(FollowId.class)
public class Follow {
    @Id
    @Column(nullable = false)
    private Long userId;

    @Id
    @Column(nullable = false)
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
