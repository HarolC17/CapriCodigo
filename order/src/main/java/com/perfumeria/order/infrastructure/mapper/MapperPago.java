package com.perfumeria.order.infrastructure.mapper;

import com.perfumeria.order.domain.model.Pago;
import com.perfumeria.order.infrastructure.driver_adapters.jpa_repository.pago.PagoData;
import com.perfumeria.order.infrastructure.driver_adapters.jpa_repository.pedido.PedidoData;
import com.perfumeria.order.infrastructure.entry_points.dto.PagoResponseDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MapperPago {

    public PagoData toData(Pago pago, PedidoData pedidoData) {
        if (pago == null) return null;

        PagoData pagoData = new PagoData();
        pagoData.setIdPago(pago.getId());
        pagoData.setPedido(pedidoData); // relación directa

        pagoData.setFechaPago(
                pago.getFechaPago() != null ? pago.getFechaPago() : LocalDateTime.now()
        );

        pagoData.setEstadoPago(
                pago.getEstadoPago() != null ? pago.getEstadoPago() : "PENDIENTE"
        );

        pagoData.setReferenciaTransaccion(pago.getReferenciaTransaccion());

        return pagoData;
    }

    public Pago toDomain(PagoData pagoData) {
        if (pagoData == null) return null;

        Pago pago = new Pago();
        pago.setId(pagoData.getIdPago());
        if (pagoData.getPedido() != null) {
            pago.setPedidoId(pagoData.getPedido().getIdPedido());
        }

        pago.setFechaPago(pagoData.getFechaPago());
        pago.setEstadoPago(pagoData.getEstadoPago());
        pago.setReferenciaTransaccion(pagoData.getReferenciaTransaccion());

        return pago;
    }
    public PagoResponseDTO toResponseDTO(Pago pago) {
        if (pago == null) return null;

        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setIdPago(pago.getId());
        dto.setPedidoId(pago.getPedidoId());
        dto.setFechaPago(pago.getFechaPago());
        dto.setEstadoPago(pago.getEstadoPago());
        dto.setReferenciaTransaccion(pago.getReferenciaTransaccion());
        return dto;
    }
}

