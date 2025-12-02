package com.c11.umastagram.repository;

import com.c11.umastagram.model.Follow;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class FollowRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FollowRepository followRepository;

    @Test
    public void saveAndGetFollowByUserAndFriend() {
        LocalDateTime now = LocalDateTime.now();
        Follow f = new Follow(10L, 20L, now);

        entityManager.persistAndFlush(f);

        Optional<Follow> found = followRepository.getFollow(10L, 20L);
        assertTrue(found.isPresent(), "Follow should be found by userId and friendId");
        assertEquals(10L, found.get().getUserId());
        assertEquals(20L, found.get().getFriendId());
    }

    @Test
    @Transactional
    public void deleteFollow_executesAndRemovesEntity() {
        LocalDateTime now = LocalDateTime.now();
        Follow f = new Follow(30L, 40L, now);

        entityManager.persistAndFlush(f);

        int deleted = followRepository.deleteFollow(30L, 40L);

        entityManager.flush();
        entityManager.clear();

        assertEquals(1, deleted, "Repository should report 1 row deleted");

        Optional<Follow> after = followRepository.getFollow(30L, 40L);
        assertFalse(after.isPresent(), "Follow should be deleted by repository method");
    }

    @Test
    public void findAllFollowersByUserId_returnsFollowers() {
        LocalDateTime now = LocalDateTime.now();
        Follow f1 = new Follow(1L, 50L, now);
        Follow f2 = new Follow(2L, 50L, now);
        Follow f3 = new Follow(3L, 60L, now);

        entityManager.persist(f1);
        entityManager.persist(f2);
        entityManager.persist(f3);
        entityManager.flush();

        List<Follow> followers = followRepository.findAllFollowersByUserId(50L);
        assertNotNull(followers);
        assertEquals(2, followers.size(), "Should return two followers for user 50");
        assertTrue(followers.stream().anyMatch(f -> f.getUserId().equals(1L)));
        assertTrue(followers.stream().anyMatch(f -> f.getUserId().equals(2L)));
    }
}
