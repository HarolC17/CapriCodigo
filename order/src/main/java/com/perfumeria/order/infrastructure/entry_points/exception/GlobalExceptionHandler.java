package com.perfumeria.order.infrastructure.entry_points.exception;

import com.perfumeria.order.domain.exception.*;
import com.perfumeria.order.infrastructure.entry_points.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==========================
    // ⚠️ EXCEPCIONES DE USUARIO (Errores de lógica del dominio)
    // ==========================
    @ExceptionHandler({
            CampoObligatorioException.class,
            UsuarioNoEncontradoException.class,
            CarritoVacioException.class,
            TotalInvalidoException.class,
            PedidoNoEncontradoException.class,
            PagoNoEncontradoException.class,
            ErrorDeComunicacionException.class
    })
    public ResponseEntity<ResponseDTO> handleUserErrors(RuntimeException ex) {
        ResponseDTO response = new ResponseDTO(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ==========================
    // ⚙️ VALIDACIONES DE SPRING/JAKARTA (DTOs con @Valid)
    // ==========================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(". "));

        ResponseDTO response = new ResponseDTO(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                errores
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ==========================
    // 💾 EXCEPCIONES DE PERSISTENCIA
    // ==========================
    @ExceptionHandler({
            PedidoPersistenciaException.class,
            PagoPersistenciaException.class
    })
    public ResponseEntity<ResponseDTO> handlePersistenceErrors(RuntimeException ex) {
        ResponseDTO response = new ResponseDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error de persistencia: " + ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ==========================
    // 🧱 EXCEPCIÓN GENERAL (fallback)
    // ==========================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleGeneral(Exception ex) {
        ResponseDTO response = new ResponseDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor: " + ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}