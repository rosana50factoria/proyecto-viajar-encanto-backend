package com.viajar.blog.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


//import com.femcoders.pet.entity.Role;
import com.viajar.blog.entity.User;
//import com.femcoders.pet.mapper.UserMapper;
import com.viajar.blog.repository.UserRepository;
import com.viajar.blog.security.UserDetail;


@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    //private final RoleService roleService;
    //private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository,  BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //@Override
    // public User createUser(User user, List<Integer> rolesIds) {
    //     Set<Role> roles = roleService.getAllRoles(rolesIds);
    //     user.setRoles(roles);
    //     user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    //     return userRepository.save(user);
    // }

    @Override
    public User getUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new RuntimeException("No existe ese usuario");
        }
        return user.get();
    }

    @Override
    public User getUserByName(String name) {
        User user = userRepository.findByName(name);
        return user;
    }

    @Override
    public List<User> getAllUsersNames() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //return userRepository.findByUsername(username)
        return userRepository.findByEmail(email)
        .map(user -> new UserDetail(user))
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
    }

}
