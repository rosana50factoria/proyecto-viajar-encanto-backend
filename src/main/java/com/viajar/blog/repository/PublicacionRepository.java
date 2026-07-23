package com.viajar.blog.repository;

//import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viajar.blog.entity.Publicacion;

public interface PublicacionRepository extends JpaRepository<Publicacion, Integer> {

  
}