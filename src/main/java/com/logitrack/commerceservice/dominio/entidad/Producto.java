package com.logitrack.commerceservice.dominio.entidad;

import com.logitrack.commerceservice.dominio.valueobject.Dimensiones;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(length = 50)
    private String categoria;

    @Column(length = 255)
    private String imagenUrl;

    @Column(nullable = false)
    private Double weight; // Peso en kg (o gramos, según definan)

    @Embedded
    private Dimensiones dimensiones; // <-- Objeto embebido con ancho, largo y profundidad
}
