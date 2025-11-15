package com.perfumeria.order.infrastructure.entry_points.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponseDTO {
    private Long id;
    private Long usuarioId;
    private String direccionEnvio;
    private Double total;
    private String estado;
    private String tipoPago;
    private LocalDateTime fechaPedido;
    private List<ItemPedidoDTO> items;
}
