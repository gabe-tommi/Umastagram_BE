package com.c11.umastagram.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "friend_request")
public class FriendRequest {
    // Ignore warnings about multiple @Id annotations for this class
    // This is intentional to create a composite primary key

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long userRequestId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long userTargetId;

    private LocalDateTime requestTime;

    public FriendRequest(Long userRequestId, Long userTargetId, LocalDateTime requestTime) {
        this.userRequestId = userRequestId;
        this.userTargetId = userTargetId;
        this.requestTime = LocalDateTime.now();
    }

    public FriendRequest() {
        this.requestTime = LocalDateTime.now();
    }

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

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "userRequestId=" + userRequestId +
                ", userTargetId=" + userTargetId +
                ", requestTime=" + requestTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FriendRequest that = (FriendRequest) o;
        return Objects.equals(userRequestId, that.userRequestId) && Objects.equals(userTargetId, that.userTargetId) && Objects.equals(requestTime, that.requestTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userRequestId, userTargetId, requestTime);
    }
}
