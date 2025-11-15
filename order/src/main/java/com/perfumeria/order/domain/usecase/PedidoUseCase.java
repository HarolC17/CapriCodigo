package com.perfumeria.order.domain.usecase;


import com.perfumeria.order.domain.exception.*;
import com.perfumeria.order.domain.model.*;
import com.perfumeria.order.domain.model.gateway.*;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class PedidoUseCase {

    private final PedidoGateway pedidoGateway;
    private final PagoUseCase pagoUseCase;
    private final CatalogoGateway catalogGateway;
    private final UsuarioGateway usuarioGateway;

    /**
     * Crea un nuevo pedido desde el carrito de un usuario.
     * Valida existencia de usuario, datos obligatorios y contenido del carrito.
     */
    public Pedido crearPedido(Long usuarioId, String direccionEnvio) {

        // 1️⃣ Validar usuario
        if (usuarioId == null || usuarioId <= 0) {
            throw new CampoObligatorioException("El ID del usuario es obligatorio.");
        }

        if (!usuarioGateway.usuarioExiste(usuarioId)) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + usuarioId);
        }

        // 2️⃣ Validar dirección
        if (direccionEnvio == null || direccionEnvio.isBlank()) {
            throw new CampoObligatorioException("La dirección de envío es obligatoria.");
        }

        // 3️⃣ Obtener productos del carrito
        List<ItemPedido> items;
        try {
            items = catalogGateway.obtenerItemsDeCarrito(usuarioId);
        } catch (Exception e) {
            throw new ErrorDeComunicacionException("Error al obtener el carrito del usuario.");
        }

        if (items == null || items.isEmpty()) {
            throw new CarritoVacioException("El carrito está vacío o no existe para el usuario " + usuarioId);
        }

        // 4️⃣ Calcular total
        double total = items.stream()
                .mapToDouble(ItemPedido::getSubtotal)
                .sum();

        if (total <= 0) {
            throw new TotalInvalidoException("El total del pedido no puede ser 0 o negativo.");
        }

        // 5️⃣ Crear el pedido antes de guardarlo
        Pedido pedido = new Pedido();
        pedido.setUsuarioId(usuarioId);
        pedido.setDireccionEnvio(direccionEnvio);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");
        pedido.setTipoPago("CONTRA_ENTREGA");
        pedido.setTotal(total);
        pedido.setItems(items);

        // 6️⃣ Guardar pedido
        Pedido pedidoGuardado;
        try {
            pedidoGuardado = pedidoGateway.guardarPedido(pedido);
        } catch (Exception e) {
            throw new PedidoPersistenciaException("Error al guardar el pedido: " + e.getMessage());
        }

        // 7️⃣ Crear pago simulado usando PagoUseCase
        try {
            Pago pago = new Pago();
            pago.setPedidoId(pedidoGuardado.getId());
            pago.setReferenciaTransaccion(null);
            pagoUseCase.registrarPago(pago);
        } catch (Exception e) {
            throw new PagoPersistenciaException("Error al registrar el pago del pedido: " + e.getMessage());
        }

        // 8️⃣ Eliminar carrito después de crear el pedido
        try {
            catalogGateway.venderCarrito(usuarioId);
        } catch (Exception e) {
            throw new ErrorDeComunicacionException("Error al eliminar el carrito del usuario.");
        }

        return pedidoGuardado;
    }

    /**
     * Buscar un pedido por su ID.
     */
    public Pedido buscarPedidoPorId(Long id) {
        if (id == null || id <= 0) {
            throw new CampoObligatorioException("El ID del pedido es obligatorio.");
        }

        Pedido pedido = pedidoGateway.buscarPedidoPorId(id);
        if (pedido == null) {
            throw new PedidoNoEncontradoException("No se encontró el pedido con ID: " + id);
        }

        return pedido;
    }

    /**
     * Obtener todos los pedidos de un usuario.
     */
    public List<Pedido> obtenerPedidosPorUsuario(Long usuarioId) {
        if (usuarioId == null || usuarioId <= 0) {
            throw new CampoObligatorioException("El ID del usuario es obligatorio.");
        }

        List<Pedido> pedidos = pedidoGateway.obtenerPedidosPorUsuario(usuarioId);
        if (pedidos == null || pedidos.isEmpty()) {
            throw new PedidoNoEncontradoException("El usuario no tiene pedidos registrados.");
        }

        return pedidos;
    }

}
