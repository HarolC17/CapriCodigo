package com.perfumeria.catalogo.infrastructure.driver_adapters.jpa_repository.producto;

import com.perfumeria.catalogo.domain.exception.ProductoNoEncontradoException;
import com.perfumeria.catalogo.domain.model.Producto;
import com.perfumeria.catalogo.domain.model.gateway.ProductoGateway;
import com.perfumeria.catalogo.infrastructure.mapper.ProductoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductoGatewayImpl implements ProductoGateway {

    private final ProductoDataJpaRepository repository;
    private final ProductoMapper productoMapper;

    @Override
    public Producto guardarProducto(Producto producto) {
        ProductoData productoData = productoMapper.toData(producto);
        ProductoData saved = repository.save(productoData);
        return productoMapper.toProducto(saved);
    }

    @Override
    public Producto buscarProductoPorId(Long id) {
        return repository.findById(id)
                .map(productoMapper::toProducto)
                .orElse(null);
    }

    @Override
    public Producto actualizarProducto(Producto producto) {
        ProductoData data = productoMapper.toData(producto);

        // Mantener fecha de creación original
        ProductoData existente = repository.findById(data.getId())
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto con ID " + data.getId() + " no existe"));
        data.setFechaCreacion(existente.getFechaCreacion());

        ProductoData actualizado = repository.save(data);
        return productoMapper.toProducto(actualizado);
    }

    @Override
    public void eliminarProducto(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Producto> obtenerProductos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductoData> productoPage = repository.findAll(pageable);
        return productoPage.getContent()
                .stream()
                .map(productoMapper::toProducto)
                .toList();
    }

    @Override
    public List<Producto> buscarPorMarca(String marca, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductoData> pageData = repository.findByMarcaContainingIgnoreCase(marca, pageable);
        return pageData.getContent()
                .stream()
                .map(productoMapper::toProducto)
                .toList();
    }

    @Override
    public List<Producto> buscarPorTipo(String tipo, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductoData> pageData = repository.findByTipoContainingIgnoreCase(tipo, pageable);
        return pageData.getContent()
                .stream()
                .map(productoMapper::toProducto)
                .toList();
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductoData> pageData = repository.findByNombreContainingIgnoreCase(nombre, pageable);
        return pageData.getContent()
                .stream()
                .map(productoMapper::toProducto)
                .toList();
    }

    @Override
    public Producto buscarPorNombreExacto(String nombre) {
        return repository.findByNombreIgnoreCase(nombre)
                .map(productoMapper::toProducto)
                .orElse(null);
    }

}
