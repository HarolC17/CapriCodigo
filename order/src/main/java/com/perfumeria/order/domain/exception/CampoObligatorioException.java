package com.perfumeria.order.domain.exception;

public class CampoObligatorioException extends RuntimeException {
    public CampoObligatorioException(String message) {
        super(message);
    }
}
