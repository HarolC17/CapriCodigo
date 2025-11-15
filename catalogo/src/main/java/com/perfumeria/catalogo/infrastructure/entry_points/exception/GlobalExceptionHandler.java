package com.perfumeria.catalogo.infrastructure.entry_points.exception;


import com.perfumeria.catalogo.domain.exception.*;
import com.perfumeria.catalogo.infrastructure.entry_points.dto.ResponseDTO;
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
    // EXCEPCIONES DE USUARIO -> 200 OK
    // ==========================
    @ExceptionHandler({
            CampoInvalidoException.class,
            IdProductoInvalidoException.class,
            MarcaInvalidaException.class,
            NombreProductoInvalidoException.class,
            PaginacionInvalidaException.class,
            PrecioInvalidoException.class,
            ProductoNoEncontradoException.class,
            ProductoNuloException.class,
            StockInvalidoException.class,
            TipoInvalidoException.class,
            CarritoNoEncontradoException.class,
            CarritoVacioException.class,
            CantidadInvalidaException.class,
            UsuarioNoEncontradoException.class,
            StockInsuficienteException.class
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
    // VALIDACIONES DE BEAN (Jakarta/Spring Validation) -> también 200 OK (según política)
    // ==========================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleValidationException(MethodArgumentNotValidException ex) {
        // Concatenar mensajes de campo en una sola cadena (puedes cambiar el formato si prefieres JSON con detalles)
        String mensajes = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(". "));

        ResponseDTO response = new ResponseDTO(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                mensajes
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ==========================
    // EXCEPCIONES DE INFRAESTRUCTURA / PERSISTENCIA -> 500 INTERNAL SERVER ERROR
    // ==========================
    @ExceptionHandler({
            ProductoPersistenciaException.class,
            CarritoPersistenciaException.class
    })
    public ResponseEntity<ResponseDTO> handlePersistencia(ProductoPersistenciaException ex) {
        ResponseDTO response = new ResponseDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error de persistencia: " + ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ==========================
    // EXCEPCIÓN GENERAL (fallback) -> 500 INTERNAL SERVER ERROR
    // ==========================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleGeneral(Exception ex) {
        // Aquí es buena idea loguear el stacktrace en producción (logger.error(...))
        ResponseDTO response = new ResponseDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor: " + ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
