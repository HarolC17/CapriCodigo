package com.perfumeria.order.infrastructure.mapper;


import com.perfumeria.order.domain.model.ItemPedido;
import com.perfumeria.order.domain.model.Pedido;
import com.perfumeria.order.infrastructure.driver_adapters.jpa_repository.itempedido.ItemPedidoData;
import com.perfumeria.order.infrastructure.driver_adapters.jpa_repository.pedido.PedidoData;
import com.perfumeria.order.infrastructure.entry_points.dto.ItemPedidoDTO;
import com.perfumeria.order.infrastructure.entry_points.dto.PedidoResponseDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperPedido {

    public PedidoData toData(Pedido pedido) {
        if (pedido == null) return null;

        PedidoData pedidoData = new PedidoData();
        pedidoData.setIdPedido(pedido.getId());
        pedidoData.setUsuarioId(pedido.getUsuarioId());
        pedidoData.setFechaPedido(
                pedido.getFechaPedido() != null ? pedido.getFechaPedido() : LocalDateTime.now()
        );
        pedidoData.setDireccionEnvio(pedido.getDireccionEnvio());
        pedidoData.setTotal(pedido.getTotal());
        pedidoData.setTipoPago(pedido.getTipoPago());
        pedidoData.setEstado(pedido.getEstado());

        if (pedido.getItems() != null && !pedido.getItems().isEmpty()) {
            List<ItemPedidoData> items = pedido.getItems().stream()
                    .map(item -> {
                        ItemPedidoData itemData = new ItemPedidoData();
                        itemData.setIdItemPedido(item.getId());
                        itemData.setProductoId(item.getProductoId());
                        itemData.setNombreProducto(item.getNombreProducto());
                        itemData.setPrecioUnitario(item.getPrecioUnitario());
                        itemData.setCantidad(item.getCantidad());
                        itemData.setSubtotal(item.getSubtotal());
                        itemData.setPedido(pedidoData); // relación bidireccional
                        return itemData;
                    })
                    .collect(Collectors.toList());
            pedidoData.setItems(items);
        } else {
            pedidoData.setItems(new ArrayList<>()); // evita NPE al persistir
        }

        return pedidoData;
    }

    public Pedido toDomain(PedidoData pedidoData) {
        if (pedidoData == null) return null;

        Pedido pedido = new Pedido();
        pedido.setId(pedidoData.getIdPedido());
        pedido.setUsuarioId(pedidoData.getUsuarioId());
        pedido.setFechaPedido(pedidoData.getFechaPedido());
        pedido.setDireccionEnvio(pedidoData.getDireccionEnvio());
        pedido.setTotal(pedidoData.getTotal());
        pedido.setTipoPago(pedidoData.getTipoPago());
        pedido.setEstado(pedidoData.getEstado());

        if (pedidoData.getItems() != null && !pedidoData.getItems().isEmpty()) {
            List<ItemPedido> items = pedidoData.getItems().stream()
                    .map(itemData -> {
                        ItemPedido item = new ItemPedido();
                        item.setId(itemData.getIdItemPedido());
                        item.setProductoId(itemData.getProductoId());
                        item.setNombreProducto(itemData.getNombreProducto());
                        item.setPrecioUnitario(itemData.getPrecioUnitario());
                        item.setCantidad(itemData.getCantidad());
                        item.setSubtotal(itemData.getSubtotal());
                        item.setPedidoId(pedidoData.getIdPedido());
                        return item;
                    })
                    .collect(Collectors.toList());
            pedido.setItems(items);
        } else {
            pedido.setItems(new ArrayList<>());
        }

        return pedido;
    }


    public PedidoResponseDTO toResponseDTO(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setUsuarioId(pedido.getUsuarioId());
        dto.setDireccionEnvio(pedido.getDireccionEnvio());
        dto.setTotal(pedido.getTotal());
        dto.setEstado(pedido.getEstado());
        dto.setTipoPago(pedido.getTipoPago());
        dto.setFechaPedido(pedido.getFechaPedido());

        if (pedido.getItems() != null) {
            dto.setItems(pedido.getItems().stream()
                    .map(this::toItemDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private ItemPedidoDTO toItemDTO(ItemPedido item) {
        ItemPedidoDTO dto = new ItemPedidoDTO();
        dto.setProductoId(item.getProductoId());
        dto.setNombreProducto(item.getNombreProducto());
        dto.setPrecioUnitario(item.getPrecioUnitario());
        dto.setCantidad(item.getCantidad());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }


}
