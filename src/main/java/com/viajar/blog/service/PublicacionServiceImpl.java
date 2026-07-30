package com.viajar.blog.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
        Optional<Publicacion> publicacion = publicacionRepository.findById(id);
        if (publicacion.isEmpty()) {
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
    public Publicacion crearPublicacion(PublicacionRequest request, User user) {
        Publicacion publicacion = new Publicacion();
        publicacion.setTitle(request.getTitle());
        publicacion.setContent(request.getContent());
        publicacion.setStatus(request.getStatus());
        publicacion.setPublishDate(new Date());
        publicacion.setUser(user);

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imageUrl = guardarImagen(request.getImage());
            publicacion.setImage(imageUrl);
        }
        return publicacionRepository.save(publicacion);
    }

    @Value("${app.upload.path}")
    private String uploadPath;
    
    private String guardarImagen(MultipartFile file) {
    try {
        String nombreArchivo = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path rutaDestino = Paths.get(uploadPath + nombreArchivo);
        Files.createDirectories(rutaDestino.getParent());
        Files.copy(file.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/" + nombreArchivo; // o URL completa si sirves estáticos
    } catch (IOException e) {
        throw new RuntimeException("Error al guardar la imagen", e);
    }
}

    @Override
    public Publicacion actualizarPublicacion(int id, PublicacionRequest request, User user) {
        Publicacion publicacion = publicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        if (!publicacion.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("No tienes permiso para editar esta publicación");
        }

        publicacion.setTitle(request.getTitle());
        publicacion.setContent(request.getContent());
        publicacion.setStatus(request.getStatus());

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imageUrl = guardarImagen(request.getImage());
            publicacion.setImage(imageUrl);
        }
        return publicacionRepository.save(publicacion);
    }

}
