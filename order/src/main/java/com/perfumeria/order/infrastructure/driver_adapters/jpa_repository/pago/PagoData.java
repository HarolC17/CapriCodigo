package com.perfumeria.order.infrastructure.driver_adapters.jpa_repository.pago;

import com.perfumeria.order.infrastructure.driver_adapters.jpa_repository.pedido.PedidoData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    private String estadoPago; // PENDIENTE, CONFIRMADO, RECHAZADO
    private String referenciaTransaccion; // Guía de envío o código de pago
    private LocalDateTime fechaPago;

    @OneToOne
    @JoinColumn(name = "id_pedido")
    private PedidoData pedido;

    @PrePersist
    public void asignarFecha() {
        if (fechaPago == null) {
            fechaPago = LocalDateTime.now();
        }
    }

}