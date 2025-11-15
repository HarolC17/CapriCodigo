package com.perfumeria.catalogo.infrastructure.entry_points.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoResponseDTO {

    private Long idCarrito;
    private Long usuarioId;
    private Double precioTotal;
    private List<ItemCarritoResponseDTO> items;

}
