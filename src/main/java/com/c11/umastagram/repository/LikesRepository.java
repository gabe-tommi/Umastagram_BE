/*
Author: Alexangelo Orozco Gutierrez
Date: December 2, 2025
Summary: Repository for the Likes entity
*/

package com.c11.umastagram.repository;

import com.c11.umastagram.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    
    Likes findByUserIdAndPostId(Long userId, Long postId);
    
    List<Likes> findByPostId(Long postId);
    
    List<Likes> findByUserId(Long userId);
}
