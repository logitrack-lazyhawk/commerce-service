package com.logitrack.commerceservice.servicio.pago;

import com.logitrack.commerceservice.dominio.entidad.Orden;
import com.logitrack.commerceservice.repositorio.OrdenRepository;
import com.logitrack.commerceservice.servicio.cliente.ShipmentClient;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.resources.payment.Payment;
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
            // NOTA: Para pruebas, intentemos usar la URL de Ngrok si está disponible, o localhost.
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
        } catch (com.mercadopago.exceptions.MPApiException e) {
            // AQUÍ ESTÁ LA MAGIA: Imprimimos el error real de Mercado Pago
            System.err.println("=== ERROR REAL DE MERCADO PAGO ===");
            System.err.println("Status Code: " + e.getApiResponse().getStatusCode());
            System.err.println("Contenido: " + e.getApiResponse().getContent());
            System.err.println("================================");
            throw new RuntimeException("Error al crear la preferencia de pago: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la preferencia de pago: " + e.getMessage());
        }
    }


    private final OrdenRepository ordenRepository;
    private final ShipmentClient shipmentClient;

    public void procesarNotificacionPago(String pagoId) {
        try {
            MercadoPagoConfig.setAccessToken(accessToken);

            // 1. Consultar el estado real del pago a Mercado Pago
            PaymentClient paymentClient = new PaymentClient();
            Payment payment = paymentClient.get(Long.parseLong(pagoId));

            // 2. Verificar si el pago fue aprobado
            if ("approved".equals(payment.getStatus())) {
                // 3. Obtener el ID de nuestra orden desde externalReference
                UUID ordenId = UUID.fromString(payment.getExternalReference());

                Orden orden = ordenRepository.findById(ordenId)
                        .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

                // 4. IDEMPOTENCIA: Si ya fue enviada, no hacemos nada
                if (orden.getEstado() == Orden.EstadoOrden.ENVIADA) {
                    System.out.println("Webhook duplicado ignorado para la orden: " + ordenId);
                    return;
                }

                // 5. Actualizar la orden y crear el envío
                orden.setEstado(Orden.EstadoOrden.PAGADA);
                UUID envioId = shipmentClient.crearEnvio(orden.getId(), orden.getUsuarioId(), orden.getDireccionDestino());
                orden.setEnvioId(envioId);
                orden.setEstado(Orden.EstadoOrden.ENVIADA);

                // 6. Vaciar el carrito (opcional, ya que el usuario pagó)
                // ... (puedes copiar la lógica de vaciar carrito aquí o en el servicio de orden)

                ordenRepository.save(orden);
                System.out.println("Pago aprobado y envío creado para la orden: " + ordenId);
            }
        } catch (Exception e) {
            // Loggeamos el error pero no lanzamos excepción para no devolver 500 a Mercado Pago
            System.err.println("Error procesando webhook: " + e.getMessage());
        }
    }
}
