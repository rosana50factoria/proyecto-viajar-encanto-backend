// com.viajar.blog.dto.RegisterRequest
package com.viajar.blog.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
}
