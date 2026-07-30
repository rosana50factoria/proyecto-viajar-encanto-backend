package com.viajar.blog.security.filter;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viajar.blog.entity.User;
import com.viajar.blog.security.CustomAuthenticationManager;
import com.viajar.blog.security.UserDetail;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthentication extends UsernamePasswordAuthenticationFilter {

    private final CustomAuthenticationManager customAuthenticationManager;
    private final String secret;

    //  public JWTAuthentication(CustomAuthenticationManager customAuthenticationManager){
    //     this.customAuthenticationManager = customAuthenticationManager;
       
    // }

     public JWTAuthentication(CustomAuthenticationManager customAuthenticationManager, String secret){
         this.customAuthenticationManager = customAuthenticationManager;
         this.secret = secret;
     }

    @Override
     public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            //Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            //¿se le pasa el email desde front end? porque se autentifica por username y password
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

            //SecurityContextHolder.getContext().setAuthentication(authentication);
            return customAuthenticationManager.authenticate(authentication);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
     }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        List<String> roles = authResult.getAuthorities().stream()
        .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
        .collect(Collectors.toList());

        UserDetail userDetail = (UserDetail) authResult.getPrincipal();
        String name = userDetail.getUser().getName();

        String token = JWT.create()
        .withSubject(authResult.getName())
        .withClaim("roles", roles)
        .withClaim("name", name)
        .withExpiresAt(new Date(System.currentTimeMillis() + (5 * 60000))) // 5 min
        //.sign(Algorithm.HMAC512("asdffg"));
        .sign(Algorithm.HMAC512(secret));

        //he tenido que añadir esto porque no llegaba el header con el token
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        response.addHeader("Authorization", "Bearer " + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401
        response.getWriter().write(failed.getMessage());
        response.getWriter().flush();
    }







}
