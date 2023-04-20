package com.example.demo.repo;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface UserRepo  extends JpaRepository<User,Long> {

    User getByLogin(String login);
    User findByLogin(String login);
    List<User> findByNameContaining(String name, Pageable pageable);
    long countByNameContaining(String name);

}
