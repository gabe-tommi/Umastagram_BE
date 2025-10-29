package com.c11.umastagram.repository;

import com.c11.umastagram.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    User findByGithubId(String githubId); // finds the particular user with githubId
    

}
