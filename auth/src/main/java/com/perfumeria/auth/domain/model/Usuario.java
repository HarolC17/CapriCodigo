package com.perfumeria.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class Usuario {

    private Long id_usuario;
    private String nombre;
    private String email;
    private String password;
    private String role;
    private String numeroTelefono;
    private LocalDateTime fecha_creacion;
}
