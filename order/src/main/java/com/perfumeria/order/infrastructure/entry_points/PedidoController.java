package com.perfumeria.order.infrastructure.entry_points;

import com.perfumeria.order.domain.model.Pedido;
import com.perfumeria.order.domain.usecase.PedidoUseCase;
import com.perfumeria.order.infrastructure.entry_points.dto.PedidoRequestDTO;
import com.perfumeria.order.infrastructure.entry_points.dto.PedidoResponseDTO;
import com.perfumeria.order.infrastructure.entry_points.dto.ResponseDTO;
import com.perfumeria.order.infrastructure.mapper.MapperPedido;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/perfumeria/pedido")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoUseCase pedidoUseCase;
    private final MapperPedido pedidoMapper;

    // ==========================
    // 🛒 Crear pedido a partir del carrito
    // ==========================
    @PostMapping("/crear")
    public ResponseEntity<PedidoResponseDTO> crearPedido(@Valid @RequestBody PedidoRequestDTO request) {
        Pedido pedido = pedidoUseCase.crearPedido(request.getUsuarioId(), request.getDireccionEnvio());
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoMapper.toResponseDTO(pedido));
    }

    // ==========================
    // 🔍 Buscar pedido por ID
    // ==========================
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPedidoPorId(@PathVariable Long id) {
        Pedido pedido = pedidoUseCase.buscarPedidoPorId(id);
        return ResponseEntity.ok(pedidoMapper.toResponseDTO(pedido));
    }

    // ==========================
    // 📦 Listar pedidos de un usuario
    // ==========================
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PedidoResponseDTO>> obtenerPedidosPorUsuario(@PathVariable Long usuarioId) {
        List<PedidoResponseDTO> pedidos = pedidoUseCase.obtenerPedidosPorUsuario(usuarioId)
                .stream()
                .map(pedidoMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pedidos);
    }
}
