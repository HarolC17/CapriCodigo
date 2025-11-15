package com.perfumeria.auth.infrastructure.entry_points.exception;

import com.perfumeria.auth.domain.exception.CampoObligatorioException;
import com.perfumeria.auth.domain.exception.CredencialesInvalidasException;
import com.perfumeria.auth.domain.exception.EmailDuplicadoException;
import com.perfumeria.auth.domain.exception.UsuarioNoEncontradoException;
import com.perfumeria.auth.infrastructure.entry_points.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ===============================
    // Errores de negocio o de usuario (HTTP 200)
    // ===============================

    @ExceptionHandler(EmailDuplicadoException.class)
    public ResponseEntity<ResponseDTO> handleEmailDuplicado(EmailDuplicadoException ex) {
        ResponseDTO response = new ResponseDTO(LocalDateTime.now(), HttpStatus.OK.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ResponseDTO> handleUsuarioNoEncontrado(UsuarioNoEncontradoException ex) {
        ResponseDTO response = new ResponseDTO(LocalDateTime.now(), HttpStatus.OK.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(CampoObligatorioException.class)
    public ResponseEntity<ResponseDTO> handleCampoObligatorio(CampoObligatorioException ex) {
        ResponseDTO response = new ResponseDTO(LocalDateTime.now(), HttpStatus.OK.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ResponseDTO> handleCredencialesInvalidas(CredencialesInvalidasException ex) {
        ResponseDTO response = new ResponseDTO(LocalDateTime.now(), HttpStatus.OK.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ===============================
    // Errores por datos inválidos o mal enviados (HTTP 400)
    // ===============================

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDTO> handleIllegalArgument(IllegalArgumentException ex) {
        ResponseDTO response = new ResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // ===============================
    // Errores no controlados (HTTP 500)
    // ===============================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleGeneralException(Exception ex) {
        ResponseDTO response = new ResponseDTO(LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleValidationException(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        StringBuilder errores = new StringBuilder();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append(". ");
        });

        ResponseDTO response = new ResponseDTO(
                java.time.LocalDateTime.now(),
                org.springframework.http.HttpStatus.OK.value(),
                errores.toString().trim()
        );

        return new ResponseEntity<>(response, org.springframework.http.HttpStatus.OK);
    }

}
