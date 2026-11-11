package com.logitrack.commerceservice.controlador;

import com.logitrack.commerceservice.dominio.entidad.ContenidoPlay;
import com.logitrack.commerceservice.repositorio.ContenidoPlayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/play")
@RequiredArgsConstructor
public class ContenidoPlayController {

    private final ContenidoPlayRepository contenidoRepository;

    @GetMapping
    public ResponseEntity<List<ContenidoPlay>> listar(@RequestParam(required = false) String categoria) {
        List<ContenidoPlay> contenido = (categoria != null && !categoria.isBlank())
                ? contenidoRepository.findByCategoria(categoria)
                : contenidoRepository.findAll();
        return ResponseEntity.ok(contenido);
    }

    // Endpoint de prueba para insertar contenido rápido (solo para el Sprint 4)
    @PostMapping("/seed")
    public ResponseEntity<List<ContenidoPlay>> seed() {
        List<ContenidoPlay> items = List.of(
                ContenidoPlay.builder()
                        .titulo("Introducción a Microservicios")
                        .descripcion("Video educativo sobre arquitecturas distribuidas.")
                        .categoria("Educativo")
                        .urlVideo("https://www.youtube.com/embed/dQw4w9WgXcQ") // URL de ejemplo
                        .miniaturaUrl("https://img.youtube.com/vi/dQw4w9WgXcQ/0.jpg")
                        .build(),
                ContenidoPlay.builder()
                        .titulo("Corto: La Logística del Futuro")
                        .descripcion("Un vistazo a los drones y la entrega autónoma.")
                        .categoria("Cortos")
                        .urlVideo("https://www.youtube.com/embed/5qap5aO4i9A")
                        .miniaturaUrl("https://img.youtube.com/vi/5qap5aO4i9A/0.jpg")
                        .build()
        );
        return ResponseEntity.ok(contenidoRepository.saveAll(items));
    }
}