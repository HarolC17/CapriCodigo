package com.perfumeria.order.application.config;

import com.perfumeria.order.domain.model.gateway.CatalogoGateway;
import com.perfumeria.order.domain.model.gateway.PagoGateway;
import com.perfumeria.order.domain.model.gateway.PedidoGateway;
import com.perfumeria.order.domain.model.gateway.UsuarioGateway;
import com.perfumeria.order.domain.usecase.PagoUseCase;
import com.perfumeria.order.domain.usecase.PedidoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public PedidoUseCase pedidoUseCase(PedidoGateway pedidoGateway,
                                       PagoUseCase pagoUseCase,
                                       CatalogoGateway catalogoGateway,
                                       UsuarioGateway usuarioGateway) {
        return new PedidoUseCase(pedidoGateway, pagoUseCase, catalogoGateway, usuarioGateway);
    }

    @Bean
    public PagoUseCase pagoUseCase(PagoGateway pagoGateway,
                                   PedidoGateway pedidoGateway,
                                   CatalogoGateway catalogoGateway) {
        return new PagoUseCase(pagoGateway, pedidoGateway, catalogoGateway);
    }
}