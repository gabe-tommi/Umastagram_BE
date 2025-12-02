/*
Author: Alexangelo Orozco Gutierrez
Date: October 29, 2025
Summary: DAO for Posts entity
*/

package com.c11.umastagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.c11.umastagram.model.Posts;
import java.util.List;

/**
 * Retrieves all Posts by the specified user.
 *
 * Searches the underlying data store for Posts entities whose {@code userId}
 * field matches the provided {@code userId} value.
 *
 * @param userId the unique identifier of the user whose posts are to be returned; must not be {@code null}
 * @return a {@code List<Posts>} containing all posts authored by the specified user;
 *         returns an empty list if the user has no posts or if no matching user is found
 * @since 2025-10-29
 * @author Alexangelo Orozco
 */
@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
    List<Posts> findByUserId(Long userId);
}
