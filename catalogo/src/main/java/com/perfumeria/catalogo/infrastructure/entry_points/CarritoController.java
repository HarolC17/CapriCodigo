package com.perfumeria.catalogo.infrastructure.entry_points;

import com.perfumeria.catalogo.domain.model.Carrito;
import com.perfumeria.catalogo.domain.model.ItemCarrito;
import com.perfumeria.catalogo.domain.usecase.CarritoUseCase;
import com.perfumeria.catalogo.infrastructure.entry_points.dto.CarritoResponseDTO;
import com.perfumeria.catalogo.infrastructure.entry_points.dto.ItemCarritoRequestDTO;
import com.perfumeria.catalogo.infrastructure.entry_points.dto.ResponseDTO;
import com.perfumeria.catalogo.infrastructure.mapper.CarritoResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/perfumeria/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoUseCase carritoUseCase;
    private final CarritoResponseMapper carritoResponseMapper;

    // Agregar producto al carrito
    @PostMapping("/agregar")
    public ResponseEntity<CarritoResponseDTO> agregarItem(
            @RequestParam Long usuarioId,
            @RequestBody ItemCarritoRequestDTO itemDto) {

        ItemCarrito item = new ItemCarrito();
        item.setProductoId(itemDto.getProductoId());
        item.setCantidad(itemDto.getCantidad());

        Carrito carrito = carritoUseCase.agregarProductoAlCarrito(usuarioId, item);
        return ResponseEntity.ok(carritoResponseMapper.toDto(carrito));
    }

    // ✅ Ver carrito actual del usuario
    @GetMapping("/ver")
    public ResponseEntity<CarritoResponseDTO> verCarrito(@RequestParam Long usuarioId) {
        Carrito carrito = carritoUseCase.verCarrito(usuarioId);
        return ResponseEntity.ok(carritoResponseMapper.toDto(carrito));
    }

    // ✅ Vaciar carrito completamente
    @DeleteMapping("/vaciar")
    public ResponseEntity<ResponseDTO> vaciar(@RequestParam Long usuarioId) {
        carritoUseCase.vaciarCarrito(usuarioId);
        return ResponseEntity.ok(new ResponseDTO(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Carrito vaciado correctamente"
        ));
    }

    // ✅ Eliminar producto específico del carrito
    @DeleteMapping("/eliminar/{productoId}")
    public ResponseEntity<CarritoResponseDTO> eliminarItemDelCarrito(
            @RequestParam Long usuarioId,
            @PathVariable Long productoId) {

        Carrito carritoActualizado = carritoUseCase.eliminarProductoDelCarrito(usuarioId, productoId);
        return ResponseEntity.ok(carritoResponseMapper.toDto(carritoActualizado));
    }

    // ✅ Confirmar venta del carrito (actualiza stock y elimina carrito)
    @PostMapping("/vender/{usuarioId}")
    public ResponseEntity<ResponseDTO> venderCarrito(@PathVariable Long usuarioId) {
        carritoUseCase.vender(usuarioId);
        return ResponseEntity.ok(new ResponseDTO(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Carrito vendido correctamente"
        ));
    }


}
