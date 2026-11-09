package com.logitrack.commerceservice.servicio;

import com.logitrack.commerceservice.dominio.entidad.Orden;

import java.util.UUID;

public interface OrdenServicio {
    Orden crearOrden(UUID usuarioId, String direccionDestino);
    Orden pagarOrden(UUID ordenId);
}
