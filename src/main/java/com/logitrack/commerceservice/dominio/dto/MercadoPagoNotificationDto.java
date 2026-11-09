package com.logitrack.commerceservice.dominio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MercadoPagoNotificationDto(
        String action,
        String type,
        @JsonProperty("data") Data data
) {
    public record Data(
            String id
    ) {}
}
