package com.viajar.blog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.ServletException;

import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.viajar.blog.entity.Publicacion;
import com.viajar.blog.entity.PaisFilter;
import com.viajar.blog.entity.User;
import com.viajar.blog.repository.PublicacionRepository;
import com.viajar.blog.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class PublicacionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final String PASSWORD_EN_CLARO = "1234";

    private User autor;

    @BeforeEach
    void setup() {
        publicacionRepository.deleteAll();
        userRepository.deleteAll();

        autor = new User();
        autor.setName("Autor Test");
        autor.setEmail("autor.test@viajarconencanto.com");
        autor.setPassword(bCryptPasswordEncoder.encode(PASSWORD_EN_CLARO));
        autor = userRepository.save(autor);
    }

    /**
     * Hace login real contra el filtro JWTAuthentication (POST /login) y
     * devuelve el header "Authorization" (con el prefijo "Bearer ") que
     * genera successfulAuthentication(), listo para reenviar en las
     * siguientes peticiones.
     */
    private String obtenerToken(String email, String password) throws Exception {
        String body = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);

        MvcResult result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        return result.getResponse().getHeader("Authorization");
    }

    private Publicacion crearPublicacion(User autor, PaisFilter status) {
        Publicacion publicacion = new Publicacion();
        publicacion.setTitle("Publicación de prueba");
        publicacion.setContent("Contenido de prueba");
        publicacion.setPublishDate(new Date());
        publicacion.setStatus(status);
        publicacion.setUser(autor);
        return publicacionRepository.save(publicacion);
    }

    @Test
    public void getAllPublicacion_shouldReturn200AndListOfPublicaciones() throws Exception {
        crearPublicacion(autor, PaisFilter.ESPAÑA);
        crearPublicacion(autor, PaisFilter.PARIS);

        mockMvc.perform(get("/api/v1/publicacion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void getAllPublicacion_shouldReturn200AndEmptyList_whenNoPublicacionesExist() throws Exception {
        mockMvc.perform(get("/api/v1/publicacion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void getPublicacionById_shouldReturn200AndPublicacionData_whenExists() throws Exception {
        Publicacion publicacion = crearPublicacion(autor, PaisFilter.LONDRES);
        String token = obtenerToken(autor.getEmail(), PASSWORD_EN_CLARO);

        mockMvc.perform(get("/api/v1/publicacion/{id}", publicacion.getId())
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(publicacion.getId()))
                .andExpect(jsonPath("$.title").value("Publicación de prueba"))
                .andExpect(jsonPath("$.status").value("LONDRES"))
                .andExpect(jsonPath("$.user.name").value("Autor Test"));
    }

    @Test
    public void getPublicacionById_shouldThrowException_whenPublicacionDoesNotExist() throws Exception {
        int idInexistente = 999999;
        String token = obtenerToken(autor.getEmail(), PASSWORD_EN_CLARO);

        Exception exception = assertThrows(ServletException.class, () ->
                mockMvc.perform(get("/api/v1/publicacion/{id}", idInexistente)
                        .header("Authorization", token))
        );

        assertTrue(exception.getCause() instanceof RuntimeException);
        assertTrue(exception.getCause().getMessage().contains("No existe esa publicacion"));
    }

    @Test
    public void getPublicacionById_shouldReturn403_whenNoTokenIsSent() throws Exception {
        Publicacion publicacion = crearPublicacion(autor, PaisFilter.LONDRES);

        mockMvc.perform(get("/api/v1/publicacion/{id}", publicacion.getId()))
                .andExpect(status().isForbidden());
    }
}