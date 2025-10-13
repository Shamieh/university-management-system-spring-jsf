package com.ats.project.repository;

import com.ats.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u from User u LEFT JOIN FETCH u.student where u.username = :username ")
    Optional<User> findByUsernameWithStudent(@Param("username")String username);


}
