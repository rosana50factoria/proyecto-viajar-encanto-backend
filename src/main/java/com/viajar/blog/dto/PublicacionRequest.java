package com.viajar.blog.dto;

import org.springframework.web.multipart.MultipartFile;

import com.viajar.blog.entity.PaisFilter;
import lombok.Data;

@Data
public class PublicacionRequest {
    private String title;
    private String content;
    private PaisFilter status;
    private MultipartFile image; // nuevo
}
