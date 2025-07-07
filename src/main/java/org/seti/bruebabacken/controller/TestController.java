package org.seti.bruebabacken.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("timestamp", LocalDateTime.now());
        response.put("application", "BTG Fondos API");
        response.put("version", "1.0.0");
        return response;
    }

    @GetMapping("/info")
    public Map<String, Object> getInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("empresa", "BTG Pactual");
        info.put("proyecto", "Plataforma de Gestión de Fondos");
        info.put("tecnologias", "Spring Boot, MongoDB, Java 17");
        info.put("funcionalidades", new String[]{
            "Suscribirse a fondos",
            "Cancelar suscripciones", 
            "Ver historial de transacciones",
            "Notificaciones por email/SMS"
        });
        return info;
    }
} 