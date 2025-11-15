package com.perfumeria.catalogo.domain.usecase;

import com.perfumeria.catalogo.domain.exception.*;
import com.perfumeria.catalogo.domain.model.Carrito;
import com.perfumeria.catalogo.domain.model.ItemCarrito;
import com.perfumeria.catalogo.domain.model.Producto;
import com.perfumeria.catalogo.domain.model.gateway.CarritoGateway;
import com.perfumeria.catalogo.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CarritoUseCase {

    private final CarritoGateway carritoGateway;
    private final ProductoUseCase productoUseCase;
    private final UsuarioGateway usuarioGateway;

    // =============================
    // MÉTODOS PRINCIPALES
    // =============================

    public Carrito agregarProductoAlCarrito(Long usuarioId, ItemCarrito itemCarrito) {

        if (!usuarioGateway.usuarioExiste(usuarioId)) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + usuarioId);
        }

        if (itemCarrito.getCantidad() == null || itemCarrito.getCantidad() <= 0) {
            throw new CantidadInvalidaException("La cantidad debe ser mayor a 0");
        }

        Producto producto = productoUseCase.buscarProductoPorId(itemCarrito.getProductoId());
        itemCarrito.setNombreProducto(producto.getNombre());
        itemCarrito.setPrecioUnitario(producto.getPrecio());
        itemCarrito.setSubtotal(producto.getPrecio() * itemCarrito.getCantidad());

        Carrito carrito = carritoGateway.buscarPorUsuarioId(usuarioId);
        if (carrito == null) {
            carrito = new Carrito();
            carrito.setUsuarioId(usuarioId);
            carrito.setItems(new ArrayList<>());
        }

        Optional<ItemCarrito> existente = carrito.getItems().stream()
                .filter(item -> item.getProductoId().equals(itemCarrito.getProductoId()))
                .findFirst();

        if (existente.isPresent()) {
            ItemCarrito itemExistente = existente.get();
            itemExistente.setCantidad(itemExistente.getCantidad() + itemCarrito.getCantidad());
            itemExistente.setSubtotal(itemExistente.getPrecioUnitario() * itemExistente.getCantidad());
        } else {
            carrito.getItems().add(itemCarrito);
        }

        carrito.setPrecioTotal(calcularTotal(carrito));

        try {
            return carritoGateway.guardar(carrito);
        } catch (Exception e) {
            throw new CarritoPersistenciaException("Error al guardar el carrito: " + e.getMessage());
        }
    }

    public Carrito verCarrito(Long usuarioId) {
        Carrito carrito = carritoGateway.buscarPorUsuarioId(usuarioId);
        if (carrito == null) {
            throw new CarritoNoEncontradoException("No se encontró un carrito para el usuario con ID: " + usuarioId);
        }
        return carrito;
    }

    public void vaciarCarrito(Long usuarioId) {
        if (!usuarioGateway.usuarioExiste(usuarioId)) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + usuarioId);
        }

        Carrito carrito = carritoGateway.buscarPorUsuarioId(usuarioId);
        if (carrito == null) {
            throw new CarritoNoEncontradoException("No se encontró un carrito para el usuario con ID: " + usuarioId);
        }

        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new CarritoVacioException("El carrito ya está vacío");
        }

        carrito.getItems().clear();
        carrito.setPrecioTotal(0.0);
        carritoGateway.guardar(carrito);
    }

    public Carrito eliminarProductoDelCarrito(Long usuarioId, Long productoId) {
        if (!usuarioGateway.usuarioExiste(usuarioId)) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + usuarioId);
        }

        Carrito carrito = carritoGateway.buscarPorUsuarioId(usuarioId);
        if (carrito == null) {
            throw new CarritoNoEncontradoException("Carrito no encontrado para el usuario ID: " + usuarioId);
        }

        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new CarritoVacioException("El carrito está vacío, no hay productos para eliminar.");
        }

        boolean existeProducto = carrito.getItems().stream()
                .anyMatch(item -> item.getProductoId().equals(productoId));

        if (!existeProducto) {
            throw new ProductoNoEncontradoException("El producto con ID " + productoId + " no está en el carrito.");
        }

        List<ItemCarrito> itemsFiltrados = carrito.getItems().stream()
                .filter(item -> !item.getProductoId().equals(productoId))
                .collect(Collectors.toList());

        carrito.setItems(itemsFiltrados);
        carrito.setPrecioTotal(calcularTotal(carrito));

        return carritoGateway.guardar(carrito);
    }

    // =============================
    // MÉTODO VENDER — AHORA REFACTORIZADO
    // =============================

    public void vender(Long usuarioId) {
        Carrito carrito = validarCarritoParaVenta(usuarioId);
        procesarVenta(carrito);
    }

    // =============================
    // MÉTODOS PRIVADOS AUXILIARES
    // =============================

    /** Valida usuario, existencia del carrito y stock disponible */
    private Carrito validarCarritoParaVenta(Long usuarioId) {

        if (!usuarioGateway.usuarioExiste(usuarioId)) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + usuarioId);
        }

        Carrito carrito = carritoGateway.buscarPorUsuarioId(usuarioId);
        if (carrito == null) {
            throw new CarritoNoEncontradoException("Carrito no encontrado para el usuario: " + usuarioId);
        }

        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new CarritoVacioException("El carrito está vacío, no hay productos para vender.");
        }

        boolean stockInsuficiente = carrito.getItems().stream()
                .anyMatch(itemCarrito -> {
                    Producto producto = productoUseCase.buscarProductoPorId(itemCarrito.getProductoId());
                    return producto == null || producto.getStock() < itemCarrito.getCantidad();
                });

        if (stockInsuficiente) {
            throw new StockInsuficienteException("No hay suficiente stock");
        }

        return carrito;
    }

    /** Procesa la venta (actualiza stock y elimina el carrito) */
    private void procesarVenta(Carrito carrito) {
        carrito.getItems().forEach(itemCarrito -> {
            Producto producto = productoUseCase.buscarProductoPorId(itemCarrito.getProductoId());
            producto.setStock(producto.getStock() - itemCarrito.getCantidad());
            productoUseCase.actualizarProducto(producto);
        });

        carritoGateway.eliminarCarrito(carrito.getId());
    }

    /** Calcula el total del carrito */
    private double calcularTotal(Carrito carrito) {
        return carrito.getItems().stream()
                .mapToDouble(ItemCarrito::getSubtotal)
                .sum();
    }
}
