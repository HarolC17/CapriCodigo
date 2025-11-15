package com.perfumeria.catalogo.domain.exception;

public class ProductoPersistenciaException extends RuntimeException {
    public ProductoPersistenciaException(String message) {
        super(message);
    }
}
