package com.perfumeria.catalogo.infrastructure.entry_points.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ResponseDTO {
    private LocalDateTime timestamp;
    private int status;
    private String message;
}
