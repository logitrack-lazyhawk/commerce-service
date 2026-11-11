package com.logitrack.commerceservice.dominio.entidad;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "contenido_play")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContenidoPlay {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, length = 50)
    private String categoria; // Ej: "Educativo", "Cortos", "Documental"

    @Column(nullable = false, length = 255)
    private String urlVideo; // URL del embed de YouTube u otro proveedor autorizado

    @Column(length = 255)
    private String miniaturaUrl;
}
