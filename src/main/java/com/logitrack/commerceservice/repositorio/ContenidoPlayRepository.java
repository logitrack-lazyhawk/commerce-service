package com.logitrack.commerceservice.repositorio;

import com.logitrack.commerceservice.dominio.entidad.ContenidoPlay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContenidoPlayRepository extends JpaRepository<ContenidoPlay, UUID> {
    List<ContenidoPlay> findByCategoria(String categoria);
}
