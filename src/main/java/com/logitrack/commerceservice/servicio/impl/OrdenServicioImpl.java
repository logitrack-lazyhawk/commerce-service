package com.logitrack.commerceservice.servicio.impl;

import com.logitrack.commerceservice.dominio.entidad.Carrito;
import com.logitrack.commerceservice.dominio.entidad.Orden;
import com.logitrack.commerceservice.repositorio.CarritoRepository;
import com.logitrack.commerceservice.repositorio.OrdenRepository;
import com.logitrack.commerceservice.servicio.OrdenServicio;
import com.logitrack.commerceservice.servicio.cliente.ShipmentClient;
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
    public Orden pagarOrden(UUID ordenId) {
        // 1. Buscar la orden
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

        if (orden.getEstado() != Orden.EstadoOrden.PENDIENTE) {
            throw new IllegalArgumentException("La orden ya fue procesada");
        }

        // 2. Simular el pago (aquí luego irá Mercado Pago)
        orden.setEstado(Orden.EstadoOrden.PAGADA);

        // 3. Crear el envío en el shipment-service (Pasamos el usuarioId como clienteId)
        UUID envioId = shipmentClient.crearEnvio(orden.getId(), orden.getUsuarioId(), orden.getDireccionDestino());
        orden.setEnvioId(envioId);
        orden.setEstado(Orden.EstadoOrden.ENVIADA);

        // 4. Vaciar el carrito
        Carrito carrito = carritoRepository.findByUsuarioId(orden.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));
        carrito.getItems().clear();
        carrito.setTotal(java.math.BigDecimal.ZERO);
        carritoRepository.save(carrito);

        return ordenRepository.save(orden);
    }
}
