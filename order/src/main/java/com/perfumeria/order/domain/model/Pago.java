package com.perfumeria.order.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pago {
    private Long id;
    private Long pedidoId;
    private String estadoPago;             // Ejemplo: "PENDIENTE", "COMPLETADO", "FALLIDO"
    private LocalDateTime fechaPago;
    private String referenciaTransaccion;  // Numero de guia asignado por el ADMIN
}
