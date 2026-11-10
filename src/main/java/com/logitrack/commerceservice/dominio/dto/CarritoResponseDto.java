package com.logitrack.commerceservice.dominio.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CarritoResponseDto(
        UUID carritoId,
        UUID usuarioId,
        List<ItemCarritoResponseDto> items,
        BigDecimal total
) {}
