package com.logitrack.commerceservice.servicio.cliente;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ShipmentClient {

    private final RestClient restClient;

    @Value("${app.services.shipment.url}")
    private String shipmentServiceUrl;

    public UUID crearEnvio(UUID ordenId, UUID clienteId, String direccionDestino) {
        Map<String, Object> request = Map.of(
                "codigoSeguimiento", "ORD-" + ordenId.toString().substring(0, 8),
                "clienteId", clienteId, // <-- Ahora usa el cliente real
                "peso", 1.0,
                "ancho", 10.0,
                "alto", 10.0,
                "largo", 10.0,
                "origen", Map.of(
                        "calle", "Calle Origen",
                        "numero", "123",
                        "ciudad", "Ciudad Origen",
                        "codigoPostal", "1234",
                        "provincia", "Provincia Origen",
                        "localidad", "Localidad Origen",
                        "descripcion", "Origen"
                ),
                "destino", Map.of(
                        "calle", direccionDestino,
                        "numero", "S/N",
                        "ciudad", "Ciudad Destino",
                        "codigoPostal", "5678",
                        "provincia", "Provincia Destino",
                        "localidad", "Localidad Destino",
                        "descripcion", "Destino"
                )
        );

        var response = restClient.post()
                .uri(shipmentServiceUrl + "/api/v1/envios")
                .body(request)
                .retrieve()
                .body(Map.class);

        return UUID.fromString((String) response.get("id"));
    }
}