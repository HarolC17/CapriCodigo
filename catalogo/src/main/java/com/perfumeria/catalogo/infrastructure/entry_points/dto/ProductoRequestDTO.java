package com.perfumeria.catalogo.infrastructure.entry_points.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoRequestDTO {
    private Long id;
    private String nombre;
    private String marca;
    private String tipo;
    private Double precio;
    private Integer stock;
}
