package com.logitrack.commerceservice.controlador;

import com.logitrack.commerceservice.dominio.dto.AgregarItemRequestDto;
import com.logitrack.commerceservice.dominio.dto.CarritoResponseDto;
import com.logitrack.commerceservice.servicio.CarritoServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carritos")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoServicio carritoServicio;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<CarritoResponseDto> obtenerCarrito(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(carritoServicio.obtenerCarrito(usuarioId));
    }

    @PostMapping("/{usuarioId}/items")
    public ResponseEntity<CarritoResponseDto> agregarItem(@PathVariable UUID usuarioId, @Valid @RequestBody AgregarItemRequestDto request) {
        return ResponseEntity.ok(carritoServicio.agregarItem(usuarioId, request));
    }

    @DeleteMapping("/{usuarioId}/items/{productoId}")
    public ResponseEntity<CarritoResponseDto> eliminarItem(@PathVariable UUID usuarioId, @PathVariable UUID productoId) {
        return ResponseEntity.ok(carritoServicio.eliminarItem(usuarioId, productoId));
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable UUID usuarioId) {
        carritoServicio.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
