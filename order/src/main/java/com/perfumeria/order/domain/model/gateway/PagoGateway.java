package com.perfumeria.order.domain.model.gateway;

import com.perfumeria.order.domain.model.Pago;

public interface PagoGateway {
    Pago guardarPago(Pago pago);
    Pago buscarPagoPorId(Long pedidoId);
    void eliminarPago(Long id);
}
