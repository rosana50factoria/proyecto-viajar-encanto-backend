package com.viajar.blog.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.viajar.blog.dto.RegisterRequest;
import com.viajar.blog.dto.RegisterResponse;
import com.viajar.blog.entity.User;

public interface UserService {

    //public User createUser(User user, List<Integer> rolesIds);
    RegisterResponse createUser(RegisterRequest request);

    public User getUserById(int id);

    public User getUserByName(String name);

    public List<User> getAllUsersNames();

    public UserDetails loadUserByUsername(String username);

}
