package com.perfumeria.auth.domain.exception;

public class CampoObligatorioException extends RuntimeException {
    public CampoObligatorioException(String message) {
        super(message);
    }
}
