package com.viajar.blog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viajar.blog.dto.RegisterRequest;
import com.viajar.blog.dto.RegisterResponse;
import com.viajar.blog.entity.User;
import com.viajar.blog.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        RegisterResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id){
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/user/{name}")
    public ResponseEntity<User> getUserByName(@PathVariable String name){
        return new ResponseEntity<>(userService.getUserByName(name), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsersNames(){
        return new ResponseEntity<>(userService.getAllUsersNames(), HttpStatus.OK);
    }

    // @GetMapping("/user-role/{name}")
    // public ResponseEntity<UserWithRoleDto> getUserbyNameWithRole(@PathVariable String name){
    //     return new ResponseEntity<>(userService.getUserByNameWithRole(name), HttpStatus.OK);
    // }

}
