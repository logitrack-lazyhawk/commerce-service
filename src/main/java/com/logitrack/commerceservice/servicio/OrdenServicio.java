package com.logitrack.commerceservice.servicio;

import com.logitrack.commerceservice.dominio.dto.PagoResponseDto;
import com.logitrack.commerceservice.dominio.entidad.Orden;

import java.util.UUID;

public interface OrdenServicio {
    Orden crearOrden(UUID usuarioId, String direccionDestino);
    PagoResponseDto pagarOrden(UUID ordenId);
}
