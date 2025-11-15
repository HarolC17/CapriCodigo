package com.perfumeria.order.infrastructure.driver_adapters.jpa_repository.pedido;

import com.perfumeria.order.infrastructure.driver_adapters.jpa_repository.itempedido.ItemPedidoData;
import com.perfumeria.order.infrastructure.driver_adapters.jpa_repository.pago.PagoData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedido")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    private Long usuarioId;
    private String direccionEnvio;
    private String estado; // PENDIENTE, ENVIADO, COMPLETADO, CANCELADO
    private String tipoPago; // CONTRA_ENTREGA, PSE, etc.
    private Double total;

    private LocalDateTime fechaPedido;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedidoData> items;

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    private PagoData pago;

    @PrePersist
    public void asignarFecha() {
        if (fechaPedido == null) {
            fechaPedido = LocalDateTime.now();
        }
    }
}