package com.perfumeria.auth.infrastructure.entry_points.dto;

import lombok.*;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
    private LocalDateTime timestamp;
    private int status;
    private String mensaje;
}
