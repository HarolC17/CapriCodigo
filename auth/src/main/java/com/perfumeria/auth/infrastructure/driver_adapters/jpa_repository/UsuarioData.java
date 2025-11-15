package com.perfumeria.auth.infrastructure.driver_adapters.jpa_repository;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_usuario;

    @NotBlank(message = "El nombre del usuario es obligatorio")
    private String nombre;

    @Email(message = "El formato del correo no es válido")
    @NotBlank(message = "El email no puede estar vacío")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;
    private String role;
    private String numeroTelefono;
    private LocalDateTime fecha_creacion;

    @PrePersist
    public void prePersist() {
        if (fecha_creacion == null) {
            fecha_creacion = LocalDateTime.now();
        }
    }

}
