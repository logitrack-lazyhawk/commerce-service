package com.logitrack.commerceservice.dominio.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dimensiones {

    @Column(name = "ancho", nullable = false)
    private Double ancho;

    @Column(name = "largo", nullable = false)
    private Double largo;

    @Column(name = "profundidad", nullable = false)
    private Double profundidad;
}
