package com.viajar.blog.service;

import java.util.List;

import com.viajar.blog.dto.PublicacionRequest;
import com.viajar.blog.entity.Publicacion;
import com.viajar.blog.entity.User;


public interface PublicacionService {
    public List<Publicacion> getAllPublicacion(); 

    public Publicacion getPublicacionById(int id);

    public void deletePublicacionById(int id);

    public Publicacion crearPublicacion(PublicacionRequest request, User user);

    public Publicacion actualizarPublicacion(int id, PublicacionRequest request, User user);
    
}