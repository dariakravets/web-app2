package com.example.webapp2;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.webapp2.User;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query( value = "select u from User u")
    List<User> findByName(String name);
    //List<User> findByPosition(String position);
}
