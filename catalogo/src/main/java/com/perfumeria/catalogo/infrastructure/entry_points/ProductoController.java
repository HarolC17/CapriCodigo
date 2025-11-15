package com.perfumeria.catalogo.infrastructure.entry_points;

import com.perfumeria.catalogo.domain.model.Producto;
import com.perfumeria.catalogo.domain.usecase.ProductoUseCase;
import com.perfumeria.catalogo.infrastructure.driver_adapters.jpa_repository.producto.ProductoData;
import com.perfumeria.catalogo.infrastructure.entry_points.dto.*;
import com.perfumeria.catalogo.infrastructure.mapper.ProductoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/perfumeria/producto")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoUseCase productoUseCase;
    private final ProductoMapper productoMapper;

    // ==============================
    // Crear producto
    // ==============================
    @PostMapping("/save")
    public ResponseEntity<ProductoResponseDTO> guardarProducto(@Valid @RequestBody ProductoRequestDTO request) {
        Producto producto = productoMapper.toProducto(request);
        Producto guardado = productoUseCase.guardarProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoMapper.toResponseDTO(guardado));
    }

    // ==============================
    // Obtener producto por ID
    // ==============================
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> buscarProductoPorId(@PathVariable Long id) {
        Producto producto = productoUseCase.buscarProductoPorId(id);
        return ResponseEntity.ok(productoMapper.toResponseDTO(producto));
    }

    // ==============================
    // Listar productos (paginado)
    // ==============================
    @GetMapping("/all")
    public ResponseEntity<List<ProductoResponseDTO>> obtenerProductos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        List<ProductoResponseDTO> productos = productoUseCase.obtenerProductos(page, size)
                .stream()
                .map(productoMapper::toResponseDTO)
                .toList();

        return ResponseEntity.ok(productos);
    }

    // ==============================
    // Actualizar producto
    // ==============================
    @PutMapping("/update")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(@Valid @RequestBody ProductoRequestDTO request) {
        Producto producto = productoMapper.toProducto(request);
        Producto actualizado = productoUseCase.actualizarProducto(producto);
        return ResponseEntity.ok(productoMapper.toResponseDTO(actualizado));
    }

    // ==============================
    // Eliminar producto
    // ==============================
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> eliminarProducto(@PathVariable Long id) {
        productoUseCase.eliminarProducto(id);
        ResponseDTO response = new ResponseDTO(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Producto eliminado correctamente"
        );
        return ResponseEntity.ok(response);
    }

    // ==============================
    // Buscar productos por marca
    // ==============================
    @PostMapping("/buscar/marca")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorMarca(@Valid @RequestBody ProductoBusquedaDTO dto) {
        List<ProductoResponseDTO> productos = productoUseCase.buscarPorMarca(dto.getValor(), dto.getPage(), dto.getSize())
                .stream()
                .map(productoMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(productos);
    }

    // ==============================
    // Buscar productos por tipo
    // ==============================
    @PostMapping("/buscar/tipo")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorTipo(@Valid @RequestBody ProductoBusquedaDTO dto) {
        List<ProductoResponseDTO> productos = productoUseCase.buscarPorTipo(dto.getValor(), dto.getPage(), dto.getSize())
                .stream()
                .map(productoMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(productos);
    }

    // ==============================
    // Buscar productos por nombre
    // ==============================
    @PostMapping("/buscar/nombre")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorNombre(@Valid @RequestBody ProductoBusquedaDTO dto) {
        List<ProductoResponseDTO> productos = productoUseCase.buscarPorNombre(dto.getValor(), dto.getPage(), dto.getSize())
                .stream()
                .map(productoMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(productos);
    }

    @PutMapping("/reponer-stock")
    public ResponseEntity<ResponseDTO> reponerStock(@Valid @RequestBody ReponerStockDTO dto) {
        Producto producto = productoUseCase.buscarProductoPorId(dto.getProductoId());
        producto.setStock(producto.getStock() + dto.getCantidad());
        productoUseCase.actualizarStock(producto);

        ResponseDTO response = new ResponseDTO(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Stock del producto " + producto.getNombre() + " incrementado en " + dto.getCantidad()
        );
        return ResponseEntity.ok(response);
    }


}
