package com.perfumeria.order.infrastructure.entry_points;

import com.perfumeria.order.domain.model.Pago;
import com.perfumeria.order.domain.usecase.PagoUseCase;
import com.perfumeria.order.infrastructure.entry_points.dto.PagoResponseDTO;
import com.perfumeria.order.infrastructure.entry_points.dto.ResponseDTO;
import com.perfumeria.order.infrastructure.mapper.MapperPago;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/perfumeria/pago")
@RequiredArgsConstructor
public class PagoController {

    private final PagoUseCase pagoUseCase;
    private final MapperPago mapperPago;

    // ==========================
    // 🔍 Buscar pago por ID
    // ==========================
    @GetMapping("/{idPago}")
    public ResponseEntity<PagoResponseDTO> buscarPagoPorId(@PathVariable Long idPago) {
        Pago pago = pagoUseCase.buscarPagoPorId(idPago);
        PagoResponseDTO dto = mapperPago.toResponseDTO(pago);
        return ResponseEntity.ok(dto);
    }

    // ==========================
    // 🔄 Actualizar estado del pago (CONFIRMADO / RECHAZADO)
    // ==========================
    @PutMapping("/{idPago}/estado")
    public ResponseEntity<ResponseDTO> actualizarEstadoPago(
            @PathVariable Long idPago,
            @RequestParam String nuevoEstado) {

        Pago pagoActualizado = pagoUseCase.actualizarEstadoPago(idPago, nuevoEstado);
        PagoResponseDTO dto = mapperPago.toResponseDTO(pagoActualizado);

        ResponseDTO response = new ResponseDTO(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Estado del pago actualizado correctamente a: " + dto.getEstadoPago()
        );

        return ResponseEntity.ok(response);
    }

    // ==========================
    // 🧾 Registrar referencia de transacción (número de guía o comprobante)
    // ==========================
    @PutMapping("/{idPago}/referencia")
    public ResponseEntity<ResponseDTO> registrarReferenciaTransaccion(
            @PathVariable Long idPago,
            @RequestParam String referencia) {

        Pago pagoActualizado = pagoUseCase.registrarReferenciaTransaccion(idPago, referencia);
        PagoResponseDTO dto = mapperPago.toResponseDTO(pagoActualizado);

        ResponseDTO response = new ResponseDTO(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Referencia registrada correctamente: " + dto.getReferenciaTransaccion()
        );

        return ResponseEntity.ok(response);
    }
}