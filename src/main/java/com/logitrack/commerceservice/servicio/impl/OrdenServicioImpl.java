package com.logitrack.commerceservice.servicio.impl;

import com.logitrack.commerceservice.dominio.dto.PagoResponseDto;
import com.logitrack.commerceservice.dominio.entidad.Carrito;
import com.logitrack.commerceservice.dominio.entidad.Orden;
import com.logitrack.commerceservice.repositorio.CarritoRepository;
import com.logitrack.commerceservice.repositorio.OrdenRepository;
import com.logitrack.commerceservice.servicio.OrdenServicio;
import com.logitrack.commerceservice.servicio.cliente.ShipmentClient;
import com.logitrack.commerceservice.servicio.pago.MercadoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrdenServicioImpl implements OrdenServicio {

    private final OrdenRepository ordenRepository;
    private final CarritoRepository carritoRepository;
    private final ShipmentClient shipmentClient;
    private final MercadoPagoService mercadoPagoService; // <-- NUEVO

    @Override
    @Transactional
    public Orden crearOrden(UUID usuarioId, String direccionDestino) {
        // 1. Buscar el carrito del usuario
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un carrito para el usuario"));

        if (carrito.getItems().isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío");
        }

        // 2. Crear la orden en estado PENDIENTE
        Orden orden = Orden.builder()
                .usuarioId(usuarioId)
                .total(carrito.getTotal())
                .estado(Orden.EstadoOrden.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .direccionDestino(direccionDestino)
                .build();

        return ordenRepository.save(orden);
    }

    @Override
    @Transactional
    public PagoResponseDto pagarOrden(UUID ordenId) {
        // 1. Buscar la orden
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

        if (orden.getEstado() != Orden.EstadoOrden.PENDIENTE) {
            throw new IllegalArgumentException("La orden ya fue procesada");
        }

        // 2. Generar la preferencia de pago en Mercado Pago
        String initPoint = mercadoPagoService.crearPreferenciaPago(
                orden.getId(),
                orden.getTotal(),
                "Compra en LogiTrack - Orden " + orden.getId().toString().substring(0, 8)
        );

        // 3. Devolver el DTO con el init_point
        // NOTA: La orden NO se marca como PAGADA aquí. Eso lo hará el webhook (F22).
        return new PagoResponseDto(
                orden.getId(),
                initPoint,
                "Preferencia de pago creada exitosamente. Redirigir al usuario a la URL."
        );
    }
}
