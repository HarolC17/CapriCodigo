package com.perfumeria.catalogo.infrastructure.driver_adapters.jpa_repository.producto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoDataJpaRepository extends JpaRepository<ProductoData, Long> {

    // Buscar productos por nombre (insensible a mayúsculas y minúsculas)
    Page<ProductoData> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    // Buscar productos por marca
    Page<ProductoData> findByMarcaContainingIgnoreCase(String marca, Pageable pageable);

    // Buscar productos por tipo
    Page<ProductoData> findByTipoContainingIgnoreCase(String tipo, Pageable pageable);

    Optional<ProductoData> findByNombreIgnoreCase(String nombre);

}
