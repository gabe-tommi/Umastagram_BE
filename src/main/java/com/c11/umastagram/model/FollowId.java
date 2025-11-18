package com.c11.umastagram.model;

import java.io.Serializable;
import java.util.Objects;

public class FollowId implements Serializable {
    private Long userId;
    private Long friendId;

    public FollowId() {}

    public FollowId(Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowId followId = (FollowId) o;
        return Objects.equals(userId, followId.userId) &&
                Objects.equals(friendId, followId.friendId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, friendId);
    }
}
