package com.perfumeria.order.domain.model.gateway;

import com.perfumeria.order.domain.model.Pedido;

import java.util.List;

public interface PedidoGateway {
    Pedido guardarPedido(Pedido pedido);
    Pedido buscarPedidoPorId(Long id);
    List<Pedido> obtenerPedidosPorUsuario(Long usuarioId);
    void eliminarPedido(Long id);
    Pedido actualizarEstadoPedido(Long idPedido, String nuevoEstado);

}
