package com.perfumeria.auth.domain.model.gateway;

public interface EncrypterGateway {

    String encrypt (String password);
    Boolean checkPass (String passUser, String passDB);
}
