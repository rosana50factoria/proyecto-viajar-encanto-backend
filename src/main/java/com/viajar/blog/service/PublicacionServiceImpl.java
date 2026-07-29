package com.viajar.blog.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viajar.blog.dto.PublicacionRequest;
import com.viajar.blog.entity.Publicacion;
import com.viajar.blog.entity.User;
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

    @Override
    public Publicacion crearPublicacion(PublicacionRequest request, User user){
        Publicacion publicacion = new Publicacion();
        publicacion.setTitle(request.getTitle());
        publicacion.setContent(request.getContent());
        publicacion.setStatus(request.getStatus());
        publicacion.setPublishDate(new Date());
        publicacion.setUser(user);
        return publicacionRepository.save(publicacion);
    }

    @Override
    public Publicacion actualizarPublicacion(int id, PublicacionRequest request) {
        Publicacion publicacion = publicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        // if (!publicacion.getUser().getId().equals(user.getId())) {
        //     throw new RuntimeException("No tienes permiso para editar esta publicación");
        // }

        publicacion.setTitle(request.getTitle());
        publicacion.setContent(request.getContent());
        publicacion.setStatus(request.getStatus());
        return publicacionRepository.save(publicacion);
    }

}


