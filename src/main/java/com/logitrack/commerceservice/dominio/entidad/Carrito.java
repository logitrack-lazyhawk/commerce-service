package com.logitrack.commerceservice.dominio.entidad;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "carritos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "usuario_id", nullable = false, unique = true) // Un carrito activo por usuario
    private UUID usuarioId;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemCarrito> items = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default
    private BigDecimal total = BigDecimal.ZERO;

    // Método para calcular el total
    public void calcularTotal() {
        this.total = items.stream()
                .map(ItemCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
