package com.perfumeria.catalogo.infrastructure.mapper;

import com.perfumeria.catalogo.domain.model.Carrito;
import com.perfumeria.catalogo.domain.model.ItemCarrito;
import com.perfumeria.catalogo.infrastructure.entry_points.dto.CarritoResponseDTO;
import com.perfumeria.catalogo.infrastructure.entry_points.dto.ItemCarritoResponseDTO;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarritoResponseMapper {

    public CarritoResponseDTO toDto(Carrito carrito) {
        if (carrito == null) return null;

        CarritoResponseDTO dto = new CarritoResponseDTO();
        dto.setIdCarrito(carrito.getId());
        dto.setUsuarioId(carrito.getUsuarioId());
        dto.setPrecioTotal(carrito.getPrecioTotal());

        List<ItemCarritoResponseDTO> items = carrito.getItems() != null
                ? carrito.getItems().stream()
                .map(this::toItemDto)
                .collect(Collectors.toList())
                : List.of();

        dto.setItems(items);
        return dto;
    }

    private ItemCarritoResponseDTO toItemDto(ItemCarrito item) {
        return new ItemCarritoResponseDTO(
                item.getProductoId(),
                item.getNombreProducto(),
                item.getPrecioUnitario(),
                item.getCantidad(),
                item.getSubtotal()
        );
    }
}
