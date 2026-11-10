package com.logitrack.commerceservice.servicio.impl;

import com.logitrack.commerceservice.dominio.dto.AgregarItemRequestDto;
import com.logitrack.commerceservice.dominio.dto.CarritoResponseDto;
import com.logitrack.commerceservice.dominio.dto.ItemCarritoResponseDto;
import com.logitrack.commerceservice.dominio.entidad.Carrito;
import com.logitrack.commerceservice.dominio.entidad.ItemCarrito;
import com.logitrack.commerceservice.dominio.entidad.Producto;
import com.logitrack.commerceservice.repositorio.CarritoRepository;
import com.logitrack.commerceservice.repositorio.ProductoRepository;
import com.logitrack.commerceservice.servicio.CarritoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarritoServicioImpl implements CarritoServicio {

    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional
    public CarritoResponseDto obtenerCarrito(UUID usuarioId) {
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> crearCarritoVacio(usuarioId));
        return mapearHaciaResponse(carrito);
    }

    @Override
    @Transactional
    public CarritoResponseDto agregarItem(UUID usuarioId, AgregarItemRequestDto request) {
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> crearCarritoVacio(usuarioId));

        Producto producto = productoRepository.findById(request.productoId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        // Buscar si el producto ya está en el carrito
        ItemCarrito itemExistente = carrito.getItems().stream()
                .filter(i -> i.getProducto().getId().equals(request.productoId()))
                .findFirst()
                .orElse(null);

        if (itemExistente != null) {
            itemExistente.setCantidad(itemExistente.getCantidad() + request.cantidad());
        } else {
            ItemCarrito nuevoItem = ItemCarrito.builder()
                    .carrito(carrito)
                    .producto(producto)
                    .cantidad(request.cantidad())
                    .build();
            carrito.getItems().add(nuevoItem);
        }

        carrito.calcularTotal();
        return mapearHaciaResponse(carritoRepository.save(carrito));
    }

    @Override
    @Transactional
    public CarritoResponseDto eliminarItem(UUID usuarioId, UUID productoId) {
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

        carrito.getItems().removeIf(item -> item.getProducto().getId().equals(productoId));
        carrito.calcularTotal();
        return mapearHaciaResponse(carritoRepository.save(carrito));
    }

    @Override
    @Transactional
    public void vaciarCarrito(UUID usuarioId) {
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));
        carrito.getItems().clear();
        carrito.setTotal(java.math.BigDecimal.ZERO);
        carritoRepository.save(carrito);
    }

    private Carrito crearCarritoVacio(UUID usuarioId) {
        Carrito nuevoCarrito = Carrito.builder()
                .usuarioId(usuarioId)
                .build();
        return carritoRepository.save(nuevoCarrito);
    }

    private CarritoResponseDto mapearHaciaResponse(Carrito carrito) {
        List<ItemCarritoResponseDto> itemsDto = carrito.getItems().stream()
                .map(item -> new ItemCarritoResponseDto(
                        item.getProducto().getId(),
                        item.getProducto().getNombre(),
                        item.getProducto().getPrecio(),
                        item.getCantidad(),
                        item.getSubtotal()
                ))
                .collect(Collectors.toList());

        return new CarritoResponseDto(
                carrito.getId(),
                carrito.getUsuarioId(),
                itemsDto,
                carrito.getTotal()
        );
    }
}
