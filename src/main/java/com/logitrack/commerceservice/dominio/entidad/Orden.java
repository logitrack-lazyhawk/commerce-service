package com.logitrack.commerceservice.dominio.entidad;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ordenes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoOrden estado;

    @Column(name = "envio_id")
    private UUID envioId; // ID del envío creado en el shipment-service

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "direccion_destino", nullable = false)
    private String direccionDestino;

    public enum EstadoOrden {
        PENDIENTE,
        PAGADA,
        ENVIADA,
        CANCELADA
    }
}
