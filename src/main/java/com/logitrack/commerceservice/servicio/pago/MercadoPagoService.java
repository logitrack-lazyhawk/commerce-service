package com.logitrack.commerceservice.servicio.pago;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.resources.preference.Preference;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MercadoPagoService {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @Value("${mercadopago.notification-url}")
    private String notificationUrl;

    public String crearPreferenciaPago(UUID ordenId, BigDecimal total, String descripcion) {
        try {
            MercadoPagoConfig.setAccessToken(accessToken);

            // 1. Crear el item de la compra
            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .title(descripcion)
                    .quantity(1)
                    .unitPrice(total)
                    .currencyId("ARS")
                    .build();

            // 2. Configurar las URLs de retorno (a donde vuelve el usuario)
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("http://localhost:5173/pago-exitoso")
                    .pending("http://localhost:5173/pago-pendiente")
                    .failure("http://localhost:5173/pago-fallido")
                    .build();

            // 3. Crear la preferencia
            PreferenceRequest request = PreferenceRequest.builder()
                    .items(List.of(item))
                    .backUrls(backUrls)
                    .notificationUrl(notificationUrl)
                    .externalReference(ordenId.toString()) // <-- Para vincular el pago con nuestra orden
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(request);

            // 4. Devolver el init_point (URL de pago)
            return preference.getInitPoint();
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la preferencia de pago: " + e.getMessage());
        }
    }
}
