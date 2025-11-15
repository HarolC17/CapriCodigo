package com.perfumeria.auth.infrastructure.entry_points.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {
    private Long id_usuario;
    private String nombre;
    private String email;
    private String role;
    private LocalDateTime fecha_creacion;
}
