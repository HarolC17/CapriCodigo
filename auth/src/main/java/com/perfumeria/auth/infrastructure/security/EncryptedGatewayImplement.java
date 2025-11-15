package com.perfumeria.auth.infrastructure.security;

import com.perfumeria.auth.domain.model.gateway.EncrypterGateway;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptedGatewayImplement implements EncrypterGateway {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encrypt(String password) {
        return encoder.encode(password);
    }

    @Override
    public Boolean checkPass(String passUser, String passDB) {
        return encoder.matches(passUser, passDB);
    }

}
