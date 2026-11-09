package com.logitrack.commerceservice.servicio.impl;

import com.logitrack.commerceservice.dominio.dto.ProductoRequestDto;
import com.logitrack.commerceservice.dominio.dto.ProductoResponseDto;
import com.logitrack.commerceservice.dominio.entidad.Producto;
import com.logitrack.commerceservice.repositorio.ProductoRepository;
import com.logitrack.commerceservice.servicio.ProductoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoServicioImpl implements ProductoServicio {

    private final ProductoRepository productoRepository;

    @Override
    @Transactional
    public ProductoResponseDto crearProducto(ProductoRequestDto request) {
        Producto producto = Producto.builder()
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .precio(request.precio())
                .stock(request.stock())
                .categoria(request.categoria())
                .imagenUrl(request.imagenUrl())
                .weight(request.weight())
                .dimensiones(request.dimensiones())
                .build();

        return mapearHaciaResponse(productoRepository.save(producto));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDto obtenerPorId(UUID id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
        return mapearHaciaResponse(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDto> listarProductos(String categoria) {
        List<Producto> productos = (categoria != null && !categoria.isBlank())
                ? productoRepository.findByCategoria(categoria)
                : productoRepository.findAll();

        return productos.stream().map(this::mapearHaciaResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductoResponseDto actualizarProducto(UUID id, ProductoRequestDto request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));

        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setCategoria(request.categoria());
        producto.setImagenUrl(request.imagenUrl());
        producto.setWeight(request.weight());
        producto.setDimensiones(request.dimensiones());

        return mapearHaciaResponse(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public void eliminarProducto(UUID id) {
        if (!productoRepository.existsById(id)) {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    private ProductoResponseDto mapearHaciaResponse(Producto producto) {
        return new ProductoResponseDto(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria(),
                producto.getImagenUrl(),
                producto.getWeight(),
                producto.getDimensiones()
        );
    }
}
