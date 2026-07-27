package com.viajar.blog.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

    public SpringConfig(CustomAuthenticationManager customAuthenticationManager) {
        this.customAuthenticationManager = customAuthenticationManager;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // cubre /login y /api/** a la vez
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JWTAuthentication jwtAuthentication = new JWTAuthentication(customAuthenticationManager, secret);
        jwtAuthentication.setFilterProcessesUrl("/login");

        
        http
                // para permitir desarrollar
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                // habilitar la consola h2
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                // Le doy permisos a mis endpoints
                .authorizeHttpRequests(request -> request
                        // .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()   // <-- clave para el preflight
                        .requestMatchers("/h2/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/publicacion").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/register").permitAll()
                        .anyRequest().authenticated())
                // autentificacion básica
                // .httpBasic(Customizer.withDefaults())
                .addFilter(jwtAuthentication)
                .addFilterAfter(new JWTAuthorization(secret), JWTAuthentication.class)
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

}
