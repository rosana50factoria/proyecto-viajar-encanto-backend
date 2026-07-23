package com.viajar.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viajar.blog.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    public Role findByName(String name);
}
