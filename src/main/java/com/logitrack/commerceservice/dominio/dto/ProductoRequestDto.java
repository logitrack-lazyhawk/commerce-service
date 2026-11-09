package com.logitrack.commerceservice.dominio.dto;

import com.logitrack.commerceservice.dominio.valueobject.Dimensiones;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductoRequestDto(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        String descripcion,

        @NotNull(message = "El precio es obligatorio")
        @Positive(message = "El precio debe ser mayor a cero")
        BigDecimal precio,

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock,

        String categoria,

        String imagenUrl,

        @NotNull(message = "El peso es obligatorio")
        @Positive(message = "El peso debe ser mayor a cero")
        Double weight,

        @NotNull(message = "Las dimensiones son obligatorias")
        Dimensiones dimensiones
) {}
