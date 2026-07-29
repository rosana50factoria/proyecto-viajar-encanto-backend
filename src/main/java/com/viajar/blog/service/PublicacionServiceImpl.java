package com.viajar.blog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viajar.blog.entity.Publicacion;
import com.viajar.blog.repository.PublicacionRepository;

@Service
public class PublicacionServiceImpl implements PublicacionService {

    private final PublicacionRepository publicacionRepository;
    
    
    public PublicacionServiceImpl(PublicacionRepository publicacionRepository) { 
        this.publicacionRepository = publicacionRepository;
    }

    @Override
    public List<Publicacion> getAllPublicacion() {
        List<Publicacion> publicaciones = publicacionRepository.findAll();
       
        return publicaciones;
    }

    @Override
    public Publicacion getPublicacionById(int id) {
         Optional<Publicacion> publicacion=publicacionRepository.findById(id);
        if(publicacion.isEmpty()){
            throw new RuntimeException("No existe esa publicacion");
        }
        return publicacion.get();
    }

    @Override
    public void deletePublicacionById(int id) {
      Publicacion publicacion = getPublicacionById(id);
      
        publicacionRepository.delete(publicacion);
    }
}


