package com.viajar.blog.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.viajar.blog.entity.Role;
import com.viajar.blog.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Set<Role> getAllRoles(List<Integer> rolesIds) {
        Set<Role> roles = roleRepository.findAllById(rolesIds).stream().collect(Collectors.toSet());
        return roles;
    }

}
