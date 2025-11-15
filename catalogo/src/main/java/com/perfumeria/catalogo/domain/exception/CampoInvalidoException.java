package com.perfumeria.catalogo.domain.exception;

public class CampoInvalidoException extends RuntimeException {
    public CampoInvalidoException(String message) {
        super(message);
    }
}
