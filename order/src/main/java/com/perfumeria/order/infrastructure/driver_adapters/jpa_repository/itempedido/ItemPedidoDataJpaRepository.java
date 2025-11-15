package com.perfumeria.order.infrastructure.driver_adapters.jpa_repository.itempedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidoDataJpaRepository extends JpaRepository<ItemPedidoData, Long> {
}