package com.logitrack.commerceservice.controlador;

import com.logitrack.commerceservice.dominio.dto.PagoResponseDto;
import com.logitrack.commerceservice.dominio.entidad.Orden;
import com.logitrack.commerceservice.servicio.OrdenServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ordenes")
@RequiredArgsConstructor
public class OrdenController {

    private final OrdenServicio ordenServicio;

    @PostMapping("/{usuarioId}")
    public ResponseEntity<Orden> crearOrden(@PathVariable UUID usuarioId, @RequestParam String direccionDestino) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordenServicio.crearOrden(usuarioId, direccionDestino));
    }

    @PatchMapping("/{ordenId}/pagar")
    public ResponseEntity<PagoResponseDto> pagarOrden(@PathVariable UUID ordenId) {
        return ResponseEntity.ok(ordenServicio.pagarOrden(ordenId));
    }
}