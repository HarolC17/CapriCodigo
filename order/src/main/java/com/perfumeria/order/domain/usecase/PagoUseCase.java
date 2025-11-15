package com.perfumeria.order.domain.usecase;

import com.perfumeria.order.domain.exception.*;
import com.perfumeria.order.domain.model.Pago;
import com.perfumeria.order.domain.model.Pedido;
import com.perfumeria.order.domain.model.gateway.CatalogoGateway;
import com.perfumeria.order.domain.model.gateway.PagoGateway;
import com.perfumeria.order.domain.model.gateway.PedidoGateway;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class PagoUseCase {

    private final PagoGateway pagoGateway;
    private final PedidoGateway pedidoGateway;
    private final CatalogoGateway catalogoGateway;

    /**
     * Registrar un nuevo pago (lo utiliza PedidoUseCase o directamente el admin)
     */
    public Pago registrarPago(Pago pago) {
        if (pago == null) {
            throw new CampoObligatorioException("El pago no puede ser nulo.");
        }
        if (pago.getPedidoId() == null || pago.getPedidoId() <= 0) {
            throw new CampoObligatorioException("El ID del pedido es obligatorio para registrar un pago.");
        }

        pago.setFechaPago(LocalDateTime.now());
        pago.setEstadoPago("PENDIENTE");

        try {
            return pagoGateway.guardarPago(pago);
        } catch (Exception e) {
            throw new PagoPersistenciaException("Error al guardar el pago: " + e.getMessage());
        }
    }

    /**
     * Actualiza el estado del pago (CONFIRMADO o RECHAZADO)
     * Si es RECHAZADO → reponer stock y cancelar pedido
     * Si es CONFIRMADO → marcar pedido como ENVIADO
     */
    public Pago actualizarEstadoPago(Long idPago, String nuevoEstado) {
        if (idPago == null || idPago <= 0) {
            throw new CampoObligatorioException("El ID del pago es obligatorio.");
        }
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new CampoObligatorioException("El nuevo estado no puede estar vacío.");
        }

        Pago pago = pagoGateway.buscarPagoPorId(idPago);
        if (pago == null) {
            throw new PagoNoEncontradoException("No se encontró el pago con ID: " + idPago);
        }

        pago.setEstadoPago(nuevoEstado.toUpperCase());
        pago.setFechaPago(LocalDateTime.now());

        try {
            // 🔹 Guardar el pago con su nuevo estado
            pago = pagoGateway.guardarPago(pago);

            // ===============================
            // 🔁 Manejo de efectos según estado
            // ===============================
            if ("CONFIRMADO".equalsIgnoreCase(nuevoEstado)) {
                // Pedido confirmado → marcado como ENVIADO
                pedidoGateway.actualizarEstadoPedido(pago.getPedidoId(), "ENVIADO");
            }

            if ("RECHAZADO".equalsIgnoreCase(nuevoEstado)) {
                Pedido pedido = pedidoGateway.buscarPedidoPorId(pago.getPedidoId());
                if (pedido != null && pedido.getItems() != null) {
                    // 🔹 Reponer stock de cada producto
                    pedido.getItems().forEach(item -> {
                        try {
                            catalogoGateway.reponerStock(item.getProductoId(), item.getCantidad());
                        } catch (Exception ex) {
                            throw new RuntimeException("Error al reponer stock del producto: " + item.getProductoId(), ex);
                        }
                    });
                }

                // 🔹 Actualizar pedido a CANCELADO
                pedidoGateway.actualizarEstadoPedido(pago.getPedidoId(), "CANCELADO");
            }

            return pago;
        } catch (Exception e) {
            throw new PagoPersistenciaException("Error al actualizar el pago: " + e.getMessage());
        }
    }
    /**
     * Registrar referencia de transacción (por ejemplo, guía del envío o código de pago)
     */
    public Pago registrarReferenciaTransaccion(Long idPago, String referencia) {
        if (idPago == null || idPago <= 0) {
            throw new CampoObligatorioException("El ID del pago es obligatorio.");
        }
        if (referencia == null || referencia.isBlank()) {
            throw new CampoObligatorioException("La referencia de transacción es obligatoria.");
        }

        Pago pago = pagoGateway.buscarPagoPorId(idPago);
        if (pago == null) {
            throw new PagoNoEncontradoException("No se encontró el pago con ID: " + idPago);
        }

        pago.setReferenciaTransaccion(referencia.trim());
        pago.setEstadoPago("CONFIRMADO");
        pago.setFechaPago(LocalDateTime.now());

        try {
            // Actualizar también el estado del pedido
            pedidoGateway.actualizarEstadoPedido(pago.getPedidoId(), "ENVIADO");
            return pagoGateway.guardarPago(pago);
        } catch (Exception e) {
            throw new PagoPersistenciaException("Error al registrar la referencia del pago: " + e.getMessage());
        }
    }

    /**
     * Buscar pago por su ID
     */
    public Pago buscarPagoPorId(Long idPago) {
        if (idPago == null || idPago <= 0) {
            throw new CampoObligatorioException("El ID del pago es obligatorio.");
        }

        Pago pago = pagoGateway.buscarPagoPorId(idPago);
        if (pago == null) {
            throw new PagoNoEncontradoException("Pago no encontrado con ID: " + idPago);
        }

        return pago;
    }

}

