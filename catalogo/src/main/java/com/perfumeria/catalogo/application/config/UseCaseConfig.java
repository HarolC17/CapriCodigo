package com.perfumeria.catalogo.application.config;

import com.perfumeria.catalogo.domain.model.gateway.CarritoGateway;
import com.perfumeria.catalogo.domain.model.gateway.ProductoGateway;
import com.perfumeria.catalogo.domain.model.gateway.UsuarioGateway;
import com.perfumeria.catalogo.domain.usecase.CarritoUseCase;
import com.perfumeria.catalogo.domain.usecase.ProductoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
    @Bean
    public ProductoUseCase productoUseCase(ProductoGateway productoGateway) {
        return new ProductoUseCase(productoGateway);
    }

    @Bean
    public CarritoUseCase carritoUseCase(CarritoGateway carritoGateway,
                                         ProductoUseCase productoUseCase,
                                         UsuarioGateway usuarioGateway) {
        return new CarritoUseCase(carritoGateway, productoUseCase, usuarioGateway);
    }

}
