package com.logitrack.commerceservice.dominio.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemCarritoResponseDto(
        UUID productoId,
        String nombreProducto,
        BigDecimal precioUnitario,
        Integer cantidad,
        BigDecimal subtotal
) {}
