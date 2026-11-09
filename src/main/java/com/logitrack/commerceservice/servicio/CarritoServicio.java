package com.logitrack.commerceservice.servicio;

import com.logitrack.commerceservice.dominio.dto.AgregarItemRequestDto;
import com.logitrack.commerceservice.dominio.dto.CarritoResponseDto;

import java.util.UUID;

public interface CarritoServicio {
    CarritoResponseDto obtenerCarrito(UUID usuarioId);
    CarritoResponseDto agregarItem(UUID usuarioId, AgregarItemRequestDto request);
    CarritoResponseDto eliminarItem(UUID usuarioId, UUID productoId);
    void vaciarCarrito(UUID usuarioId);
}
