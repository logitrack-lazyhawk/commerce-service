package com.logitrack.commerceservice.dominio.dto;

import java.util.UUID;

public record PagoResponseDto(
        UUID ordenId,
        String initPoint, // URL de Mercado Pago
        String mensaje
) {}
