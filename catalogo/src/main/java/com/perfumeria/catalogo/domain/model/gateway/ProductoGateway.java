package com.perfumeria.catalogo.domain.model.gateway;

import com.perfumeria.catalogo.domain.model.Producto;

import java.util.List;

public interface ProductoGateway {

    Producto guardarProducto(Producto producto);
    Producto buscarProductoPorId (Long id);
    Producto actualizarProducto(Producto producto);
    void eliminarProducto (Long id);
    List<Producto> obtenerProductos(int page, int size);
    List<Producto> buscarPorMarca(String marca, int page, int size);
    List<Producto> buscarPorTipo(String tipo, int page, int size);
    List<Producto> buscarPorNombre(String nombre, int page, int size);
    Producto buscarPorNombreExacto(String nombre);

}
