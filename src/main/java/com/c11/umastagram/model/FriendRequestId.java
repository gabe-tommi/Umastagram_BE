package com.c11.umastagram.model;

import java.io.Serializable;
import java.util.Objects;

public class FriendRequestId implements Serializable {
    private Long userRequestId;
    private Long userTargetId;

    public FriendRequestId() {}

    public FriendRequestId(Long userRequestId, Long userTargetId) {
        this.userRequestId = userRequestId;
        this.userTargetId = userTargetId;
    }

    // ADD THESE GETTERS AND SETTERS
    public Long getUserRequestId() {
        return userRequestId;
    }

    public void setUserRequestId(Long userRequestId) {
        this.userRequestId = userRequestId;
    }

    public Long getUserTargetId() {
        return userTargetId;
    }

    public void setUserTargetId(Long userTargetId) {
        this.userTargetId = userTargetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendRequestId that = (FriendRequestId) o;
        return Objects.equals(userRequestId, that.userRequestId) &&
            Objects.equals(userTargetId, that.userTargetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userRequestId, userTargetId);
    }
}