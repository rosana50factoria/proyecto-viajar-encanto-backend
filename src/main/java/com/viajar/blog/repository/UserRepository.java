package com.viajar.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viajar.blog.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

    public User findByName(String name);
    public Optional<User> findByUsername(String name);

}
