package com.viajar.blog.controller;

import com.viajar.blog.repository.UserRepository;
import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viajar.blog.dto.PublicacionRequest;
import com.viajar.blog.entity.Publicacion;
import com.viajar.blog.entity.User;
//import com.mundotech.newspaper.dto.request.ArticleDto;
//import com.mundotech.newspaper.dto.response.ArticleInfoDto;
//import com.mundotech.newspaper.entity.ArticleStatus;
import com.viajar.blog.service.PublicacionService;

//import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/publicacion")
public class PublicacionController {

    private final UserRepository userRepository;
    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService, UserRepository userRepository) {
        this.publicacionService = publicacionService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<Publicacion>> getAllPublicacion() {
        List<Publicacion> listPublicacion = publicacionService.getAllPublicacion();
        return new ResponseEntity<>(listPublicacion, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publicacion> getPublicacionById(@PathVariable int id) {
        Publicacion publicacion = publicacionService.getPublicacionById(id);
        return new ResponseEntity<>(publicacion, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable int id) {
        publicacionService.deletePublicacionById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Publicacion> crearPublicacion(
            @ModelAttribute PublicacionRequest request, Principal principal) {
        
        User usuario = userRepository.findByEmail(principal.getName()).get();
        Publicacion nueva = publicacionService.crearPublicacion(request, usuario);
        return ResponseEntity.ok(nueva);
    }

    @PutMapping(value = "/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Publicacion> actualizarPublicacion(
            @PathVariable int id,
            @ModelAttribute PublicacionRequest request, Principal principal) {
         
        User usuario = userRepository.findByEmail(principal.getName()).get();
        Publicacion actualizada = publicacionService.actualizarPublicacion(id, request,usuario);
        return ResponseEntity.ok(actualizada);
    }
}