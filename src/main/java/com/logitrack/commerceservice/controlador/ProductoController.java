package com.logitrack.commerceservice.controlador;

import com.logitrack.commerceservice.dominio.dto.ProductoRequestDto;
import com.logitrack.commerceservice.dominio.dto.ProductoResponseDto;
import com.logitrack.commerceservice.servicio.ProductoServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoServicio productoServicio;

    @PostMapping
    public ResponseEntity<ProductoResponseDto> crear(@Valid @RequestBody ProductoRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoServicio.crearProducto(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(productoServicio.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponseDto>> listar(@RequestParam(required = false) String categoria) {
        return ResponseEntity.ok(productoServicio.listarProductos(categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> actualizar(@PathVariable UUID id, @Valid @RequestBody ProductoRequestDto request) {
        return ResponseEntity.ok(productoServicio.actualizarProducto(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        productoServicio.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
