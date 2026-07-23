package com.viajar.blog.service;

import java.util.List;
import java.util.Set;

import com.viajar.blog.entity.Role;

public interface RoleService {

    public Role createRole(Role role);

    public Set<Role> getAllRoles(List<Integer> rolesIds);
}
