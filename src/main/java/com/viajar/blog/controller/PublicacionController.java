package com.viajar.blog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


import com.viajar.blog.entity.Publicacion;
//import com.mundotech.newspaper.dto.request.ArticleDto;
//import com.mundotech.newspaper.dto.response.ArticleInfoDto;
//import com.mundotech.newspaper.entity.ArticleStatus;
import com.viajar.blog.service.PublicacionService;

//import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/publicacion")
public class PublicacionController {

    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService){
        this.publicacionService = publicacionService;
    }
     
   
    @GetMapping
    public ResponseEntity<List<Publicacion>> getAllPublicacion(){
        List<Publicacion> listPublicacion = publicacionService.getAllPublicacion();
        return new ResponseEntity<>(listPublicacion, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publicacion> getPublicacionById(@PathVariable int id){
        Publicacion publicacion = publicacionService.getPublicacionById(id);
        return new ResponseEntity<>(publicacion, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable int id) {
        publicacionService.deletePublicacionById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}