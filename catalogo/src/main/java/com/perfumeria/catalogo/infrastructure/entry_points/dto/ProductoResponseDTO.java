package com.perfumeria.catalogo.infrastructure.entry_points.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResponseDTO {
    private Long id;
    private String nombre;
    private String marca;
    private String tipo;
    private Double precio;
    private Integer stock;
    private LocalDateTime fechaCreacion;
}
