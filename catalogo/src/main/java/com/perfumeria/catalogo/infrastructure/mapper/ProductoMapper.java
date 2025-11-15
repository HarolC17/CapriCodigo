package com.perfumeria.catalogo.infrastructure.mapper;


import com.perfumeria.catalogo.domain.model.Producto;
import com.perfumeria.catalogo.infrastructure.driver_adapters.jpa_repository.producto.ProductoData;
import com.perfumeria.catalogo.infrastructure.entry_points.dto.ProductoRequestDTO;
import com.perfumeria.catalogo.infrastructure.entry_points.dto.ProductoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {


    // Convertir de Data (JPA) → Dominio
    public Producto toProducto(ProductoData productoData) {
        if (productoData == null) {
            return null;
        }

        return new Producto(
                productoData.getId(),
                productoData.getNombre(),
                productoData.getDescripcion(),
                productoData.getMarca(),
                productoData.getTipo(),
                productoData.getPrecio(),
                productoData.getStock(),
                productoData.getImagenUrl(),
                productoData.getFechaCreacion()
        );
    }

    // Convertir de Dominio → Data (JPA)
    public ProductoData toData(Producto producto) {
        if (producto == null) {
            return null;
        }

        return new ProductoData(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getMarca(),
                producto.getTipo(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getImagenUrl(),
                producto.getFechaCreacion()
        );
    }


    public Producto toProducto(ProductoRequestDTO dto) {
        Producto producto = new Producto();
        producto.setId(dto.getId());
        producto.setNombre(dto.getNombre());
        producto.setMarca(dto.getMarca());
        producto.setTipo(dto.getTipo());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        return producto;
    }

    public ProductoResponseDTO toResponseDTO(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setMarca(producto.getMarca());
        dto.setTipo(producto.getTipo());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setFechaCreacion(producto.getFechaCreacion());
        return dto;
    }


}
