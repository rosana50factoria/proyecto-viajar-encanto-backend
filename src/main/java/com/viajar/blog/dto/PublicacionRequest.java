package com.viajar.blog.dto;

import com.viajar.blog.entity.PaisFilter;
import lombok.Data;

@Data
public class PublicacionRequest {
    private String title;
    private String content;
    private PaisFilter status;
}
