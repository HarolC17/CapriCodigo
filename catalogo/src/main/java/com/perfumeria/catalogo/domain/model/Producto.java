package com.perfumeria.catalogo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class Producto {

    private Long id;
    private String nombre;
    private String descripcion;
    private String marca;
    private String tipo;
    private Double precio;
    private Integer stock;
    private String imagenUrl;
    private LocalDateTime fechaCreacion;

}
