package com.perfumeria.catalogo.domain.exception;

public class IdProductoInvalidoException extends RuntimeException {
    public IdProductoInvalidoException(String message) {
        super(message);
    }
}
