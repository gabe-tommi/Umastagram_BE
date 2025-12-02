// src/test/java/com/c11/umastagram/repository/FriendRequestRepositoryTest.java
package com.c11.umastagram.repository;

import com.c11.umastagram.model.FriendRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class FriendRequestRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Test
    public void saveAndFindByCompositeKey() {
        LocalDateTime now = LocalDateTime.now();
        FriendRequest fr = new FriendRequest(100L, 200L, now);

        entityManager.persistAndFlush(fr);

        Optional<FriendRequest> found = friendRequestRepository.getFriendRequest(100L, 200L);
        assertTrue(found.isPresent(), "FriendRequest should be found by composite key");
        assertEquals(100L, found.get().getUserRequestId());
        assertEquals(200L, found.get().getUserTargetId());
    }

    @Test
    @Transactional
    public void deleteFriendRequest_executesAndRemovesEntity() {
        LocalDateTime now = LocalDateTime.now();
        FriendRequest fr = new FriendRequest(300L, 400L, now);

        entityManager.persistAndFlush(fr);

        // call the repository method that performs JPQL DELETE
        friendRequestRepository.deleteFriendRequest(300L, 400L);

        // flush to ensure query executed; then verify no entity remains
        entityManager.flush();
        Optional<FriendRequest> after = friendRequestRepository.getFriendRequest(300L, 400L);
        assertFalse(after.isPresent(), "FriendRequest should be deleted by repository method");
    }
}
