package com.perfumeria.catalogo.domain.exception;

public class NombreProductoInvalidoException extends RuntimeException {
    public NombreProductoInvalidoException(String message) {
        super(message);
    }
}
