// com.viajar.blog.dto.RegisterResponse
package com.viajar.blog.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private Integer id;
    private String name;
    private String email;
}
