package org.seti.bruebabacken.controller;

import java.util.HashMap;
import java.util.Map;

import org.seti.bruebabacken.model.Cliente;
import org.seti.bruebabacken.model.Fondo;
import org.seti.bruebabacken.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionController {

    private final NotificacionService notificacionService;

    @Autowired
    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @PostMapping("/test")
    public ResponseEntity<Map<String, String>> probarNotificacion(
            @RequestParam String email) {
        try {
            // Crear cliente de prueba
            Cliente cliente = new Cliente();
            cliente.setNombre("Cliente de Prueba");
            cliente.setEmail(email);
            cliente.setPreferenciaNotificacion("EMAIL");

            // Crear fondo de prueba
            Fondo fondo = new Fondo();
            fondo.setNombre("FONDO DE PRUEBA");

            // Enviar notificación de prueba
            notificacionService.notificarSuscripcion(cliente, fondo, "100000");

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Notificación de prueba enviada exitosamente");
            response.put("destinatario", email);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error enviando notificación: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> obtenerEstadoNotificaciones() {
        Map<String, Object> status = new HashMap<>();
        status.put("notificaciones_habilitadas", true);
        status.put("email_habilitado", true);
        status.put("mensaje", "Sistema de notificaciones por email funcionando correctamente");
        return ResponseEntity.ok(status);
    }
} 