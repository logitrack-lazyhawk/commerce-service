package com.logitrack.commerceservice.repositorio;

import com.logitrack.commerceservice.dominio.entidad.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {
    // Método extra para buscar por categoría (útil para el catálogo)
    List<Producto> findByCategoria(String categoria);
}
