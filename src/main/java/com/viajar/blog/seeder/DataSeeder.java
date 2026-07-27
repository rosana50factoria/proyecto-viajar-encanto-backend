package com.viajar.blog.seeder;


import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.viajar.blog.entity.PaisFilter;
import com.viajar.blog.entity.Publicacion;
import com.viajar.blog.entity.Role;
import com.viajar.blog.entity.User;
import com.viajar.blog.repository.PublicacionRepository;
import com.viajar.blog.repository.RoleRepository;
import com.viajar.blog.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final PublicacionRepository publicacionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public DataSeeder(RoleRepository roleRepository,UserRepository userRepository,  BCryptPasswordEncoder bCryptPasswordEncoder, PublicacionRepository publicacionRepository){
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.publicacionRepository = publicacionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
         if(roleRepository.count() == 0){
            
             Role user = Role.builder()
             .name("user")
             .build();

             roleRepository.saveAll(List.of(user));
         }

        if(userRepository.count() == 0){
            User admin = User.builder()
            .name("admin")
            .email("rosa@gmail.com")
            //.username("admin")
            .password(bCryptPasswordEncoder.encode("admin"))
            .roles(Set.of(roleRepository.findByName("user")))
            .build();

            User lola = User.builder()
            .name("user1")
            .email("lola@gmail.com")
            //.username("lola")
            .password(bCryptPasswordEncoder.encode("123456"))
            .roles(Set.of(roleRepository.findByName("user")))
            .build();

            User ana = User.builder()
            .name("user2")
            .email("ana@gmail.com")
            //.username("ana")
            .password(bCryptPasswordEncoder.encode("123456"))
            .roles(Set.of(roleRepository.findByName("user")))
            .build();

            userRepository.saveAll(List.of(admin, lola, ana));
        }

        if (publicacionRepository.count()==0){
            Publicacion p1 = Publicacion.builder()
            .title("El susurro de Cudillero al amanecer")
            .content("Hay lugares que parecen atrapados en un\n" + //
                                "tiempo más amable. Caminar por las\n" + //
                                "callejuelas vacías mientras el mar…")
            .publishDate(new Date())
            .status(PaisFilter.ESPAÑA)
            .image("https://images.unsplash.com/photo-1545411845-09d0638b8563?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
            .user(userRepository.findByName("user1"))
            .build();

            Publicacion p2 = Publicacion.builder()
            .title("La melancolía dulce de la\n" + //
                                "Bretaña")
            .content("Caminar bajo la lluvia fina por Saint-Suliac\n" + //
                                "es descubrir que el gris puede ser el color\n" + //
                                "más acogedor del mundo si se acompaña")
            .publishDate(new Date())
            .status(PaisFilter.PARIS)
            .image("https://images.unsplash.com/photo-1703178132715-f9e5c0e26934?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
            .user(userRepository.findByName("user2"))
            .build();

            publicacionRepository.saveAll(List.of(p1,p2));
        }


        // if (dogRepository.count() == 0) {
        //     Dog firulais = Dog.builder()
        //     .name("Firulais")
        //     .age(2)
        //     .isAdopted(false)
        //     .user(userRepository.findByName("user_employee"))
        //     .build();

        //     Dog pipo = Dog.builder()
        //     .name("Pipo")
        //     .age(3)
        //     .isAdopted(false)
        //     .user(userRepository.findByName("user_employee"))
        //     .build();

        //     dogRepository.saveAll(List.of(firulais, pipo));

        // }

    }

}
