package com.logitrack.commerceservice.servicio;

import com.logitrack.commerceservice.dominio.dto.ProductoRequestDto;
import com.logitrack.commerceservice.dominio.dto.ProductoResponseDto;

import java.util.List;
import java.util.UUID;

public interface ProductoServicio {
    ProductoResponseDto crearProducto(ProductoRequestDto request);
    ProductoResponseDto obtenerPorId(UUID id);
    List<ProductoResponseDto> listarProductos(String categoria); // Filtro opcional
    ProductoResponseDto actualizarProducto(UUID id, ProductoRequestDto request);
    void eliminarProducto(UUID id);
}
