package com.perfumeria.order.infrastructure.driver_adapters.rest_client.usuario;

import com.perfumeria.order.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class UsuarioGatewayImpl implements UsuarioGateway {

    private final RestTemplate restTemplate;

    @Override
    public boolean usuarioExiste(Long usuarioId) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    "http://localhost:1010/api/perfumeria/usuario/" + usuarioId,
                    String.class
            );

            // Verifica si el mensaje indica inexistencia
            if (response.getBody() != null && response.getBody().contains("Usuario no encontrado")) {
                return false;
            }

            return response.getStatusCode().is2xxSuccessful();

        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception ex) {
            throw new RuntimeException("Error al consultar el microservicio de autenticación", ex);
        }
    }
}