package com.viajar.blog;

import com.viajar.blog.entity.Publicacion;
import com.viajar.blog.entity.PaisFilter;
import com.viajar.blog.repository.PublicacionRepository;
import com.viajar.blog.service.PublicacionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * IMPORTANTE - este archivo debe ir en:
 * src/test/java/com/viajar/blog/service/PublicacionServiceTest.java
 * (la ruta de carpetas debe coincidir con el package de arriba).
 *
 * PublicacionService es una interfaz en tu proyecto, así que el
 * @InjectMocks se aplica sobre PublicacionServiceImpl, la clase
 * concreta que implementa esos métodos.
 *
 * El id de Publicacion es Integer (no Long) según tu entidad.
 */
@ExtendWith(MockitoExtension.class)
class PublicacionServiceTest {

    @Mock
    private PublicacionRepository publicacionRepository;

    @InjectMocks
    private PublicacionServiceImpl publicacionService;

    private Publicacion publicacion;

    @BeforeEach
    void setUp() {
        publicacion = new Publicacion();
        publicacion.setId(1);
        publicacion.setTitle("Un fin de semana en París");
        publicacion.setContent("Contenido de prueba sobre el viaje...");
        publicacion.setStatus(PaisFilter.PARIS);
    }

    @Test
    @DisplayName("getAllPublicacion() devuelve la lista completa de publicaciones")
    void getAllPublicacion_devuelveListaDePublicaciones() {
        // arrange
        Publicacion otra = new Publicacion();
        otra.setId(2);
        otra.setTitle("Escapada a Londres");
        otra.setStatus(PaisFilter.LONDRES);

        when(publicacionRepository.findAll()).thenReturn(Arrays.asList(publicacion, otra));

        // act
        List<Publicacion> resultado = publicacionService.getAllPublicacion();

        // assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(Publicacion::getTitle)
                .containsExactly("Un fin de semana en París", "Escapada a Londres");
        verify(publicacionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getPublicacionById() devuelve la publicación cuando existe")
    void getPublicacionById_publicacionExiste_devuelvePublicacion() {
        // arrange
        when(publicacionRepository.findById(1)).thenReturn(Optional.of(publicacion));

        // act
        Publicacion resultado = publicacionService.getPublicacionById(1);

        // assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1);
        assertThat(resultado.getTitle()).isEqualTo("Un fin de semana en París");
        verify(publicacionRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("getPublicacionById() lanza excepción cuando no existe la publicación")
    void getPublicacionById_publicacionNoExiste_lanzaExcepcion() {
        // arrange
        when(publicacionRepository.findById(99)).thenReturn(Optional.empty());

        // act + assert
        // Cambia RuntimeException por la excepción concreta que uses
        // (p. ej. ResourceNotFoundException) si ya tienes una definida.
        assertThrows(RuntimeException.class, () -> publicacionService.getPublicacionById(99));
        verify(publicacionRepository, times(1)).findById(99);
    }
}