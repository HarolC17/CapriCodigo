package com.perfumeria.order.infrastructure.driver_adapters.jpa_repository.itempedido;

import com.perfumeria.order.infrastructure.driver_adapters.jpa_repository.pedido.PedidoData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_pedido")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedidoData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItemPedido;

    private Long productoId;
    private String nombreProducto;
    private Double precioUnitario;
    private Integer cantidad;
    private Double subtotal;

    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private PedidoData pedido;
}