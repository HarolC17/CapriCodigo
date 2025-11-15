package com.perfumeria.auth.domain.model.gateway;
import com.perfumeria.auth.domain.model.Usuario;
import java.util.List;

public interface UsuarioGateway {

    Usuario guardarUsuario(Usuario usuario);
    List<Usuario> obtenerUsuarios(int page, int size);
    Usuario buscarUsuarioPorId (Long id_usuario);
    Usuario actualizarUsuario (Usuario usuario);
    void eliminarUsuario (Long id_usuario);
    Usuario buscarUsuarioPorEmail(String email);

}
