package com.perfumeria.order.infrastructure.driver_adapters.rest_client.catalogo;

import com.perfumeria.order.domain.model.ItemPedido;
import com.perfumeria.order.domain.model.gateway.CatalogoGateway;
import com.perfumeria.order.infrastructure.entry_points.dto.ReponerStockDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CatalogoGatewayImpl implements CatalogoGateway {

    private final RestTemplate restTemplate;

    @Override
    public List<ItemPedido> obtenerItemsDeCarrito(Long usuarioId) {
        try {
            ResponseEntity<CarritoResponse> response = restTemplate.getForEntity(
                    "http://localhost:1111/api/perfumeria/carrito/ver?usuarioId=" + usuarioId,
                    CarritoResponse.class
            );

            CarritoResponse carrito = response.getBody();
            if (carrito == null || carrito.getItems() == null) {
                return List.of();
            }

            // Convertir los items del carrito en items de pedido
            return carrito.getItems().stream()
                    .map(item -> {
                        ItemPedido nuevo = new ItemPedido();
                        nuevo.setProductoId(item.getProductoId());
                        nuevo.setNombreProducto(item.getNombreProducto());
                        nuevo.setPrecioUnitario(item.getPrecioUnitario());
                        nuevo.setCantidad(item.getCantidad());
                        nuevo.setSubtotal(item.getSubtotal());
                        return nuevo;
                    })
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Error al consultar el carrito del usuario", e);
        }
    }

    @Override
    public void venderCarrito(Long usuarioId) {
        try {
            restTemplate.postForEntity(
                    "http://localhost:1111/api/perfumeria/carrito/vender/" + usuarioId,
                    null,
                    Void.class
            );
        } catch (Exception ex) {
            throw new RuntimeException("Error al procesar la venta en catálogo", ex);
        }
    }

    @Override
    public void reponerStock(Long productoId, Integer cantidad) {
        try {
            ReponerStockDTO dto = new ReponerStockDTO(productoId, cantidad);
            restTemplate.put(
                    "http://localhost:1111/api/perfumeria/producto/reponer-stock",
                    dto
            );
        } catch (Exception e) {
            throw new RuntimeException("Error al reponer stock del producto " + productoId, e);
        }
    }



}
