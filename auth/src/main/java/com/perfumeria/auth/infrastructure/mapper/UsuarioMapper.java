package com.perfumeria.auth.infrastructure.mapper;

import com.perfumeria.auth.domain.model.Usuario;
import com.perfumeria.auth.infrastructure.driver_adapters.jpa_repository.UsuarioData;
import com.perfumeria.auth.infrastructure.entry_points.dto.UsuarioRequestDTO;
import com.perfumeria.auth.infrastructure.entry_points.dto.UsuarioResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toUsuario(UsuarioData usuariodata) {
        return new Usuario(
                usuariodata.getId_usuario(),
                usuariodata.getNombre(),
                usuariodata.getEmail(),
                usuariodata.getPassword(),
                usuariodata.getRole(),
                usuariodata.getNumeroTelefono(),
                usuariodata.getFecha_creacion()

        );

    }

    public UsuarioData toData(Usuario usuario) {
        return new UsuarioData(
                usuario.getId_usuario(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getRole(),
                usuario.getNumeroTelefono(),
                usuario.getFecha_creacion()

        );
    }


    public Usuario toUsuario(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setId_usuario(dto.getId_usuario());
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setNumeroTelefono(dto.getNumeroTelefono());
        usuario.setRole(dto.getRole());
        return usuario;
    }

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId_usuario(usuario.getId_usuario());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setRole(usuario.getRole());
        dto.setFecha_creacion(usuario.getFecha_creacion());
        return dto;
    }

}
