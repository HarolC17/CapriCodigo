package com.perfumeria.auth.infrastructure.entry_points.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String mensaje;
    private Long id_usuario;
    private String nombre;
    private String email;
    private String role;
}
