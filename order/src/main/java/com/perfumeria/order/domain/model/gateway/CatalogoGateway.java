package com.perfumeria.order.domain.model.gateway;

import com.perfumeria.order.domain.model.ItemPedido;

import java.util.List;

public interface CatalogoGateway {

    List<ItemPedido> obtenerItemsDeCarrito(Long usuarioId);
    void venderCarrito(Long usuarioId);
    void reponerStock (Long productoId, Integer cantidad);

}
