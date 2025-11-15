package com.perfumeria.catalogo.infrastructure.entry_points.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoBusquedaDTO {

    @NotBlank(message = "El campo de búsqueda no puede estar vacío.")
    @Size(min = 2, max = 50, message = "El término de búsqueda debe tener entre 2 y 50 caracteres.")
    private String valor;

    private int page = 0;
    private int size = 5;
}
