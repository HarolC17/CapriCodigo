package com.perfumeria.order.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

    private Long id;
    private Long usuarioId;
    private Double total;
    private String estado;          // Ejemplo: "PENDIENTE", "PAGADO", "CANCELADO"
    private String tipoPago;        // Ejemplo: "CONTRA_ENTREGA"
    private String direccionEnvio;
    private LocalDateTime fechaPedido;
    private List<ItemPedido> items;

}
