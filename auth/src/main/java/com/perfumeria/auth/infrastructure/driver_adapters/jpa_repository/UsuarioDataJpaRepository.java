package com.perfumeria.auth.infrastructure.driver_adapters.jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioDataJpaRepository extends JpaRepository<UsuarioData, Long> {

    // Crear consulta buscar por email
    UsuarioData findByEmail(String email);
}
