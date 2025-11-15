package com.perfumeria.catalogo.domain.model.gateway;

import com.perfumeria.catalogo.domain.model.Carrito;

public interface CarritoGateway {

    Carrito guardar(Carrito carrito);
    Carrito buscarPorUsuarioId(Long usuarioId);
    void eliminarCarrito(Long carritoId);
}
