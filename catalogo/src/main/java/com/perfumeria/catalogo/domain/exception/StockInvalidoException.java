package com.perfumeria.catalogo.domain.exception;

public class StockInvalidoException extends RuntimeException {
    public StockInvalidoException(String message) {
        super(message);
    }
}
