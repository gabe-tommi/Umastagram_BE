package com.c11.umastagram.model;

import java.io.Serializable;
import java.util.Objects;

// This class represents a composite key for the FriendRequest entity
// consisting of userRequestId and userTargetId.

public class FriendRequestId implements Serializable {
    private Long userRequestId;
    private Long userTargetId;

    public FriendRequestId() {}

    public FriendRequestId(Long userRequestId, Long userTargetId) {
        this.userRequestId = userRequestId;
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
