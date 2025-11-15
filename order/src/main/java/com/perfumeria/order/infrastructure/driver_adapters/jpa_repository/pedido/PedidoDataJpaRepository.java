package com.perfumeria.order.infrastructure.driver_adapters.jpa_repository.pedido;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoDataJpaRepository extends JpaRepository<PedidoData, Long> {

    // Buscar pedidos por usuario
    List<PedidoData> findByUsuarioId(Long usuarioId);

}