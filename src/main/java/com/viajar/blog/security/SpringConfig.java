package com.viajar.blog.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;

import com.viajar.blog.security.filter.JWTAuthentication;
import com.viajar.blog.security.filter.JWTAuthorization;

//import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.http.HttpMethod;

@Configuration
public class SpringConfig {

    private final CustomAuthenticationManager customAuthenticationManager;

    @Value("${JWT_SECRET:secreto}")
    private String secret;

    public SpringConfig(CustomAuthenticationManager customAuthenticationManager){
        this.customAuthenticationManager = customAuthenticationManager;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        JWTAuthentication jwtAuthentication = new JWTAuthentication(customAuthenticationManager,secret);
        jwtAuthentication.setFilterProcessesUrl("/login");

        http
        //para permitir desarrollar 
        .csrf(csrf -> csrf.disable())
        //habilitar la consola h2
        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
        //Le doy permisos a mis endpoints
        .authorizeHttpRequests(request -> request
            //.requestMatchers("/error").permitAll()
            .requestMatchers("/h2/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/publicacion").permitAll()
            .anyRequest().authenticated()
        )
        //autentificacion básica
        //.httpBasic(Customizer.withDefaults())
        .addFilter(jwtAuthentication)
        .addFilterAfter(new JWTAuthorization(secret), JWTAuthentication.class)
        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

}
