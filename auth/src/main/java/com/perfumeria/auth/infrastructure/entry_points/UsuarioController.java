package com.perfumeria.auth.infrastructure.entry_points;


import com.perfumeria.auth.domain.model.Usuario;
import com.perfumeria.auth.domain.usecase.UsuarioUseCase;
import com.perfumeria.auth.infrastructure.driver_adapters.jpa_repository.UsuarioData;
import com.perfumeria.auth.infrastructure.entry_points.dto.*;
import com.perfumeria.auth.infrastructure.mapper.UsuarioMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/perfumeria/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;
    private final UsuarioMapper usuarioMapper;

    // ===============================
    // Crear usuario
    // ===============================
    @PostMapping("/save")
    public ResponseEntity<UsuarioResponseDTO> saveUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        Usuario usuario = usuarioMapper.toUsuario(request);
        Usuario guardado = usuarioUseCase.guardarUsuario(usuario);
        UsuarioResponseDTO response = usuarioMapper.toResponseDTO(guardado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ===============================
    // Listar usuarios con paginación
    // ===============================
    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        List<UsuarioResponseDTO> usuarios = usuarioUseCase.obtenerUsuarios(page, size)
                .stream()
                .map(usuarioMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    // ===============================
    // Buscar usuario por ID
    // ===============================
    @GetMapping("/{id_usuario}")
    public ResponseEntity<UsuarioResponseDTO> findByIdUsuarios(@PathVariable Long id_usuario) {
        Usuario usuario = usuarioUseCase.buscarUsuarioPorId(id_usuario);
        return ResponseEntity.ok(usuarioMapper.toResponseDTO(usuario));
    }

    // ===============================
    // Actualizar usuario
    // ===============================
    @PutMapping("/update")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        Usuario usuario = usuarioMapper.toUsuario(request);
        Usuario actualizado = usuarioUseCase.actualizarUsuario(usuario);
        return ResponseEntity.ok(usuarioMapper.toResponseDTO(actualizado));
    }

    // ===============================
    // Eliminar usuario
    // ===============================
    @DeleteMapping("/delete/{id_usuario}")
    public ResponseEntity<ResponseDTO> deleteUsuarioById(@PathVariable Long id_usuario) {
        usuarioUseCase.eliminarUsuario(id_usuario);
        return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(), 200, "Usuario eliminado correctamente"));
    }

    // ===============================
    // Login
    // ===============================
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        Usuario usuario = usuarioUseCase.iniciarSesionYDevolverUsuario(request.getEmail(), request.getPassword());
        LoginResponseDTO response = new LoginResponseDTO(
                "Login exitoso",
                usuario.getId_usuario(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRole()
        );
        return ResponseEntity.ok(response);
    }
}
