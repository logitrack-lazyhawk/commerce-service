package com.logitrack.commerceservice.dominio.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AgregarItemRequestDto(
        @NotNull(message = "El ID del producto es obligatorio")
        UUID productoId,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad mínima es 1")
        Integer cantidad
) {}
