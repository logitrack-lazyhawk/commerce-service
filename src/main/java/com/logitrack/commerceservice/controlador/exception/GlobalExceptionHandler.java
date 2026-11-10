package com.logitrack.commerceservice.controlador.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException; // <-- IMPORTANTE

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para el microservicio commerce-service.
 * Captura las excepciones de dominio y las transforma en respuestas HTTP coherentes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura las excepciones de argumentos inválidos (ej. producto no encontrado).
     * Devuelve un HTTP 400 Bad Request.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Captura cualquier otra excepción no controlada.
     * Devuelve un HTTP 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "Ocurrió un error inesperado en el servidor.");
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Captura errores de validación de campos (ej. @NotNull, @Min fallidos).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Error");

        // Extraemos todos los mensajes de error de los campos
        Map<String, String> errores = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }
        body.put("message", errores);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Captura errores cuando el JSON está mal formado o un UUID no es válido.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Malformed JSON Request");
        body.put("message", "El cuerpo de la petición no tiene un formato válido. Verifica los tipos de datos (ej. UUIDs).");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Captura errores 404 de servicios externos (ej. shipment-service no encontró el cliente).
     * Devuelve un HTTP 400 Bad Request con el mensaje del servicio externo.
     */
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundFromExternalService(HttpClientErrorException.NotFound ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Error de Validación Externa");
        body.put("message", "El servicio externo rechazó la petición: " + ex.getResponseBodyAsString());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
