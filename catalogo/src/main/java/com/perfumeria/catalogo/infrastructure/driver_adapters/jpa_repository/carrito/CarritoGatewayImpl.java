package com.perfumeria.catalogo.infrastructure.driver_adapters.jpa_repository.carrito;

import com.perfumeria.catalogo.domain.exception.CarritoPersistenciaException;
import com.perfumeria.catalogo.domain.model.Carrito;
import com.perfumeria.catalogo.domain.model.gateway.CarritoGateway;
import com.perfumeria.catalogo.infrastructure.mapper.MapperCarrito;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CarritoGatewayImpl implements CarritoGateway {

    private final CarritoDataJpaRepository repository;
    private final MapperCarrito mapper;

    @Override
    public Carrito guardar(Carrito carrito) {
        try {
            // Asegurar relación bidireccional
            if (carrito.getItems() != null) {
                carrito.getItems().forEach(item -> item.setCarritoId(carrito.getId()));
            }

            CarritoData carritoData = mapper.toData(carrito);
            CarritoData guardado = repository.save(carritoData);
            return mapper.toDomain(guardado);

        } catch (Exception e) {
            throw new CarritoPersistenciaException("Error al guardar el carrito: " + e.getMessage());
        }
    }

    @Override
    public Carrito buscarPorUsuarioId(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public void eliminarCarrito(Long carritoId) {
        try {
            repository.deleteById(carritoId);
        } catch (Exception e) {
            throw new CarritoPersistenciaException("Error al eliminar el carrito: " + e.getMessage());
        }
    }
}
