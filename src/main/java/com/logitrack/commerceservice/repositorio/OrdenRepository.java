package com.logitrack.commerceservice.repositorio;

import com.logitrack.commerceservice.dominio.entidad.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, UUID> {
}