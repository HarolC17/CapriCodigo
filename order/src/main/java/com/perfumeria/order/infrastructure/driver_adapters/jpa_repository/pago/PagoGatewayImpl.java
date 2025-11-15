package com.perfumeria.order.infrastructure.driver_adapters.jpa_repository.pago;

import com.perfumeria.order.domain.exception.PagoNoEncontradoException;
import com.perfumeria.order.domain.exception.PagoPersistenciaException;
import com.perfumeria.order.domain.exception.PedidoNoEncontradoException;
import com.perfumeria.order.domain.model.Pago;
import com.perfumeria.order.domain.model.gateway.PagoGateway;
import com.perfumeria.order.infrastructure.driver_adapters.jpa_repository.pedido.PedidoData;
import com.perfumeria.order.infrastructure.driver_adapters.jpa_repository.pedido.PedidoDataJpaRepository;
import com.perfumeria.order.infrastructure.mapper.MapperPago;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PagoGatewayImpl implements PagoGateway {

    private final PagoDataJpaRepository pagoRepository;
    private final PedidoDataJpaRepository pedidoRepository;
    private final MapperPago mapperPago;

    @Override
    public Pago guardarPago(Pago pago) {
        try {
            PedidoData pedidoData = pedidoRepository.findById(pago.getPedidoId())
                    .orElseThrow(() -> new PedidoNoEncontradoException(
                            "No se encontró el pedido con ID: " + pago.getPedidoId() + " para registrar el pago."
                    ));

            PagoData pagoData = mapperPago.toData(pago, pedidoData);
            PagoData guardado = pagoRepository.save(pagoData);

            return mapperPago.toDomain(guardado);
        } catch (PedidoNoEncontradoException e) {
            throw e; // se propaga directamente
        } catch (Exception e) {
            throw new PagoPersistenciaException("Error al guardar el pago en la base de datos: " + e.getMessage());
        }
    }

    @Override
    public Pago buscarPagoPorId(Long idPago) {
        try {
            return pagoRepository.findById(idPago)
                    .map(mapperPago::toDomain)
                    .orElseThrow(() -> new PagoNoEncontradoException("No se encontró el pago con ID: " + idPago));
        } catch (PagoNoEncontradoException e) {
            throw e; // se propaga correctamente al bloque 200
        } catch (Exception e) {
            throw new PagoPersistenciaException("Error al consultar el pago: " + e.getMessage());
        }
    }


    @Override
    public void eliminarPago(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new PagoNoEncontradoException("No se puede eliminar: el pago con ID " + id + " no existe.");
        }

        try {
            pagoRepository.deleteById(id);
        } catch (Exception e) {
            throw new PagoPersistenciaException("Error al eliminar el pago: " + e.getMessage());
        }
    }
}

