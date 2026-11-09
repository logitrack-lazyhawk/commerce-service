package com.logitrack.commerceservice.dominio.dto;

import com.logitrack.commerceservice.dominio.valueobject.Dimensiones;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductoResponseDto(
        UUID id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock,
        String categoria,
        String imagenUrl,
        Double weight,
        Dimensiones dimensiones
) {}
