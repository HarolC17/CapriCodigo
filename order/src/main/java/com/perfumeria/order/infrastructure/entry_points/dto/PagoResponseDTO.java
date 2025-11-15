package com.perfumeria.order.infrastructure.entry_points.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoResponseDTO {
    private Long idPago;
    private Long pedidoId;
    private String estadoPago;
    private String referenciaTransaccion;
    private LocalDateTime fechaPago;
}
