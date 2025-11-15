package com.perfumeria.auth.infrastructure.driver_adapters.jpa_repository;


import com.perfumeria.auth.domain.exception.UsuarioNoEncontradoException;
import com.perfumeria.auth.domain.model.Usuario;
import com.perfumeria.auth.domain.model.gateway.UsuarioGateway;
import com.perfumeria.auth.infrastructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UsuarioGatewayImplement implements UsuarioGateway {

    private final UsuarioMapper usuarioMapper;
    private final UsuarioDataJpaRepository repository;

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioMapper.toUsuario(repository.save(usuarioMapper.toData(usuario)));
    }

    @Override
    public List<Usuario> obtenerUsuarios(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable)
                .map(usuarioMapper::toUsuario)
                .getContent();
    }

    @Override
    public Usuario buscarUsuarioPorId(Long id_usuario) {
        return repository.findById(id_usuario)
                .map(usuarioMapper::toUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException(
                        "Usuario no encontrado con ID: " + id_usuario));
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        UsuarioData usuarioData = usuarioMapper.toData(usuario);
        return usuarioMapper.toUsuario(repository.save(usuarioData));
    }

    @Override
    public void eliminarUsuario(Long id_usuario) {
        repository.deleteById(id_usuario);
    }

    @Override
    public Usuario buscarUsuarioPorEmail(String email) {
        UsuarioData usuarioData = repository.findByEmail(email);
        if (usuarioData == null) {
            return null; // 🔹 devolvemos null, no excepción
        }
        return usuarioMapper.toUsuario(usuarioData);
    }
}
