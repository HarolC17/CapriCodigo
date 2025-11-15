package com.perfumeria.auth.domain.usecase;

import com.perfumeria.auth.domain.exception.CampoObligatorioException;
import com.perfumeria.auth.domain.exception.CredencialesInvalidasException;
import com.perfumeria.auth.domain.exception.EmailDuplicadoException;
import com.perfumeria.auth.domain.exception.UsuarioNoEncontradoException;
import com.perfumeria.auth.domain.model.Notificacion;
import com.perfumeria.auth.domain.model.Usuario;
import com.perfumeria.auth.domain.model.gateway.EncrypterGateway;
import com.perfumeria.auth.domain.model.gateway.NotificationGateway;
import com.perfumeria.auth.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioGateway usuarioGateway;
    private final EncrypterGateway encrypterGateway;
    private final NotificationGateway notificationGateway;

    public Usuario guardarUsuario(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getPassword() == null) {
            throw new CampoObligatorioException("El nombre y la contraseña son obligatorios");
        }

        if (usuarioGateway.buscarUsuarioPorEmail(usuario.getEmail()) != null) {
            throw new EmailDuplicadoException("El email ya está registrado");
        }

        // 🔹 Asignar rol por defecto si no se envía
        if (usuario.getRole() == null || usuario.getRole().isBlank()) {
            usuario.setRole("USER");
        }

        String passwordEncrypt = encrypterGateway.encrypt(usuario.getPassword());
        usuario.setPassword(passwordEncrypt);
        Usuario usuarioGuardado = usuarioGateway.guardarUsuario(usuario);

        Notificacion mensajeNotificacion = Notificacion.builder()
                .tipo("Registro Usuario")
                .email(usuarioGuardado.getEmail())
                .numeroTelefono(usuarioGuardado.getNumeroTelefono())
                .mensaje("Usuario registrado con exito")
                .build();

        notificationGateway.enviarMensaje(mensajeNotificacion);

        return usuarioGuardado;
    }

    public Usuario buscarUsuarioPorId(Long id_usuario) {
        Usuario usuario = usuarioGateway.buscarUsuarioPorId(id_usuario);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + id_usuario);
        }
        return usuario;
    }

    public Usuario actualizarUsuario(Usuario usuario) {

        if (usuario.getId_usuario() == null) {
            throw new CampoObligatorioException("El ID del usuario es obligatorio para actualizar");
        }

        // Buscar usuario existente
        Usuario usuarioExistente;
        try {
            usuarioExistente = usuarioGateway.buscarUsuarioPorId(usuario.getId_usuario());
        } catch (UsuarioNoEncontradoException e) {
            throw new UsuarioNoEncontradoException("No se encontró el usuario con ID: " + usuario.getId_usuario());
        }

        if (usuarioExistente == null) {
            throw new UsuarioNoEncontradoException("No se encontró el usuario con ID: " + usuario.getId_usuario());
        }

        // Validar email duplicado si se cambia
        if (!usuario.getEmail().equalsIgnoreCase(usuarioExistente.getEmail())) {
            try {
                Usuario existente = usuarioGateway.buscarUsuarioPorEmail(usuario.getEmail());
                if (existente != null) {
                    throw new EmailDuplicadoException("El email ya está registrado por otro usuario");
                }
            } catch (UsuarioNoEncontradoException e) {
                // Si no existe, se puede usar
            }
        }

        // Mantener la fecha original
        usuario.setFecha_creacion(usuarioExistente.getFecha_creacion());

        // Rol por defecto
        if (usuario.getRole() == null || usuario.getRole().isBlank()) {
            usuario.setRole(usuarioExistente.getRole() != null ? usuarioExistente.getRole() : "USER");
        }

        String passwordEncrypt = encrypterGateway.encrypt(usuario.getPassword());
        usuario.setPassword(passwordEncrypt);
        Usuario usuarioActualizado = usuarioGateway.actualizarUsuario(usuario);

        Notificacion mensajeNotificacion = Notificacion.builder()
                .tipo("Actualizacion Usuario")
                .email(usuarioActualizado.getEmail())
                .numeroTelefono(usuarioActualizado.getNumeroTelefono())
                .mensaje("Usuario actualizado con exito")
                .build();

        notificationGateway.enviarMensaje(mensajeNotificacion);

        return usuarioActualizado;
    }


    public void eliminarUsuario(Long id_usuario) {
        Usuario usuario = usuarioGateway.buscarUsuarioPorId(id_usuario);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("No se puede eliminar. Usuario no encontrado con ID: " + id_usuario);
        }
        usuarioGateway.eliminarUsuario(id_usuario);
    }

    public Usuario iniciarSesionYDevolverUsuario(String email, String password) {
        Usuario usuario = usuarioGateway.buscarUsuarioPorEmail(email);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado");
        }

        if (!encrypterGateway.checkPass(password, usuario.getPassword())) {
            throw new CredencialesInvalidasException("Credenciales incorrectas");
        }

        return usuario;
    }

    public List<Usuario> obtenerUsuarios(int page, int size) {
        return usuarioGateway.obtenerUsuarios(page, size);
    }
}
