package com.logitrack.commerceservice.repositorio;

import com.logitrack.commerceservice.dominio.entidad.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, UUID> {
    Optional<Carrito> findByUsuarioId(UUID usuarioId);
}
