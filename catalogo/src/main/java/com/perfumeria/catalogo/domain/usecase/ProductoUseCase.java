package com.perfumeria.catalogo.domain.usecase;

import com.perfumeria.catalogo.domain.exception.*;
import com.perfumeria.catalogo.domain.model.Producto;
import com.perfumeria.catalogo.domain.model.gateway.ProductoGateway;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class ProductoUseCase {

    private final ProductoGateway productoGateway;

    public Producto guardarProducto(Producto producto) {

        if (producto == null) {
            throw new ProductoNuloException("El producto no puede ser nulo");
        }

        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new NombreProductoInvalidoException("El nombre del producto es obligatorio");
        }

        if (producto.getMarca() == null || producto.getMarca().isBlank()) {
            throw new MarcaInvalidaException("La marca del producto es obligatoria");
        }

        if (producto.getTipo() == null || producto.getTipo().isBlank()) {
            throw new TipoInvalidoException("El tipo de producto es obligatorio");
        }

        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new PrecioInvalidoException("El precio debe ser mayor que 0");
        }

        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new StockInvalidoException("El stock no puede ser negativo");
        }

        if (productoGateway.buscarPorNombreExacto(producto.getNombre()) != null) {
            throw new NombreProductoInvalidoException(
                    "Ya existe un producto con el nombre: " + producto.getNombre()
            );
        }

        try {
            return productoGateway.guardarProducto(producto);
        } catch (Exception e) {
            throw new ProductoPersistenciaException("Error al guardar el producto: " + e.getMessage());
        }
    }


    public Producto buscarProductoPorId(Long id) {

        if (id == null) {
            throw new IdProductoInvalidoException("Debe proporcionar un ID de producto válido");
        }

        try {
            Producto producto = productoGateway.buscarProductoPorId(id);

            if (producto == null) {
                throw new ProductoNoEncontradoException("No existe un producto con el ID: " + id);
            }

            return producto;

        } catch (ProductoNoEncontradoException e) {
            // Se propaga tal cual (ya es una excepción de negocio)
            throw e;
        } catch (Exception e) {
            // Cubre errores inesperados o de persistencia
            throw new ProductoPersistenciaException("Error al consultar el producto: " + e.getMessage());
        }
    }

    public Producto actualizarProducto(Producto producto) {

        if (producto == null) {
            throw new ProductoNuloException("El producto no puede ser nulo");
        }

        if (producto.getId() == null) {
            throw new IdProductoInvalidoException("Debe especificar el ID del producto a actualizar");
        }

        // Verificar existencia real del producto antes de modificarlo
        Producto existente = productoGateway.buscarProductoPorId(producto.getId());
        if (existente == null) {
            throw new ProductoNoEncontradoException("No se encontró un producto con ID: " + producto.getId());
        }

        // Validaciones de negocio sobre los campos actualizables
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new NombreProductoInvalidoException("El nombre del producto es obligatorio");
        }

        if (producto.getMarca() == null || producto.getMarca().isBlank()) {
            throw new MarcaInvalidaException("La marca del producto es obligatoria");
        }

        if (producto.getTipo() == null || producto.getTipo().isBlank()) {
            throw new TipoInvalidoException("El tipo de producto es obligatorio");
        }

        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new PrecioInvalidoException("El precio debe ser mayor que 0");
        }

        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new StockInvalidoException("El stock no puede ser negativo");
        }

        try {
            // Mantener valores inmutables del producto original
            producto.setFechaCreacion(existente.getFechaCreacion());

            // Actualización propiamente dicha
            return productoGateway.actualizarProducto(producto);

        } catch (Exception e) {
            throw new ProductoPersistenciaException("Error al actualizar el producto: " + e.getMessage());
        }
    }


    public Producto eliminarProducto(Long id) {

        if (id == null) {
            throw new IdProductoInvalidoException("Debe proporcionar un ID válido para eliminar el producto");
        }

        try {
            Producto productoExistente = productoGateway.buscarProductoPorId(id);

            if (productoExistente == null) {
                throw new ProductoNoEncontradoException("No se encontró un producto con el ID: " + id);
            }

            productoGateway.eliminarProducto(id);
            return productoExistente; // opcional: devuelve el producto eliminado para fines informativos

        } catch (ProductoNoEncontradoException e) {
            throw e; // negocio, se maneja en el GlobalExceptionHandler
        } catch (Exception e) {
            throw new ProductoPersistenciaException("Error al eliminar el producto: " + e.getMessage());
        }
    }


    public List<Producto> obtenerProductos(int page, int size) {
        // Validar parámetros de paginación
        if (page < 0) {
            throw new PaginacionInvalidaException("El número de página no puede ser negativo");
        }
        if (size <= 0) {
            throw new PaginacionInvalidaException("El tamaño de página debe ser mayor a 0");
        }
        if (size > 50) { // límite para evitar sobrecargar el sistema
            throw new PaginacionInvalidaException("El tamaño máximo permitido es 50 productos por página");
        }

        // Obtener lista paginada
        List<Producto> productos = productoGateway.obtenerProductos(page, size);

        // Validar resultado
        if (productos == null || productos.isEmpty()) {
            throw new ProductoNoEncontradoException("No se encontraron productos en la página solicitada");
        }

        return productos;
    }


    public List<Producto> buscarPorMarca(String marca, int page, int size) {

        if (marca == null || marca.isBlank()) {
            throw new CampoInvalidoException("La marca no puede estar vacía");
        }

        if (page < 0 || size <= 0) {
            throw new PaginacionInvalidaException("Los parámetros de paginación son inválidos");
        }

        List<Producto> productos = productoGateway.buscarPorMarca(marca.trim(), page, size);

        if (productos == null || productos.isEmpty()) {
            throw new ProductoNoEncontradoException("No se encontraron productos con la marca: " + marca);
        }

        return productos;
    }


    public List<Producto> buscarPorTipo(String tipo, int page, int size) {

        // Validación de datos básicos
        if (tipo == null || tipo.isBlank()) {
            throw new CampoInvalidoException("El tipo de producto no puede estar vacío");
        }

        if (page < 0 || size <= 0) {
            throw new PaginacionInvalidaException("Los parámetros de paginación son inválidos");
        }

        // Ejecución del caso de uso
        List<Producto> productos = productoGateway.buscarPorTipo(tipo.trim(), page, size);

        // Validación de resultados
        if (productos == null || productos.isEmpty()) {
            throw new ProductoNoEncontradoException("No se encontraron productos del tipo: " + tipo);
        }

        return productos;
    }


    public List<Producto> buscarPorNombre(String nombre, int page, int size) {

        // Validar el nombre del producto
        if (nombre == null || nombre.isBlank()) {
            throw new CampoInvalidoException("El nombre del producto no puede estar vacío");
        }

        // Validar los parámetros de paginación
        if (page < 0 || size <= 0) {
            throw new PaginacionInvalidaException("Los parámetros de paginación son inválidos");
        }

        // Consultar productos
        List<Producto> productos = productoGateway.buscarPorNombre(nombre.trim(), page, size);

        // Validar resultados
        if (productos == null || productos.isEmpty()) {
            throw new ProductoNoEncontradoException("No se encontraron productos con el nombre: " + nombre);
        }

        return productos;
    }

    public Producto actualizarStock(Producto producto) {
        return productoGateway.guardarProducto(producto);
    }



}
