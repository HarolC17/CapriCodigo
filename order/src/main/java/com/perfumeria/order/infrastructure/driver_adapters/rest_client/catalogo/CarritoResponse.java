package com.perfumeria.order.infrastructure.driver_adapters.rest_client.catalogo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoResponse {
    private Long id;
    private Long usuarioId;
    private Double precioTotal;
    private List<ItemCarritoResponse> items;
}
