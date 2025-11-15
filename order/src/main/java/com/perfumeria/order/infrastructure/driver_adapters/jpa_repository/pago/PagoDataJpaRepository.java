package com.perfumeria.order.infrastructure.driver_adapters.jpa_repository.pago;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoDataJpaRepository extends JpaRepository<PagoData, Long> {

    // Buscar pago por el ID del pedido
    PagoData findByPedidoIdPedido(Long pedidoId);

}