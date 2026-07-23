package com.viajar.blog.service;

import java.util.List;

import com.viajar.blog.entity.Publicacion;
//import com.viajar.blog.entity.PaisFilter;

public interface PublicacionService {
    public List<Publicacion> getAllPublicacion(); 

    public Publicacion getPublicacionById(int id);
}