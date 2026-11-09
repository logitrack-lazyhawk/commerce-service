package com.logitrack.commerceservice.controlador;

import com.logitrack.commerceservice.dominio.dto.MercadoPagoNotificationDto;
import com.logitrack.commerceservice.servicio.pago.MercadoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final MercadoPagoService mercadoPagoService;

    @PostMapping("/webhook")
    public ResponseEntity<Void> recibirWebhook(@RequestBody MercadoPagoNotificationDto notification) {
        // Mercado Pago envía notificaciones de tipo "payment"
        if ("payment".equals(notification.type())) {
            // Delegamos la lógica al servicio para mantener el controlador limpio
            mercadoPagoService.procesarNotificacionPago(notification.data().id());
        }
        // Siempre devolvemos 200 OK para que Mercado Pago sepa que recibimos el aviso
        return ResponseEntity.ok().build();
    }
}
