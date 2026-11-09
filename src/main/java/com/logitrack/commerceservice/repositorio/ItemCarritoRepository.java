package com.logitrack.commerceservice.repositorio;

import com.logitrack.commerceservice.dominio.entidad.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, UUID> {
}
