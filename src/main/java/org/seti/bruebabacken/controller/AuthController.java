package org.seti.bruebabacken.controller;

import java.util.HashMap;
import java.util.Map;

import org.seti.bruebabacken.dto.AuthResponse;
import org.seti.bruebabacken.dto.LoginRequest;
import org.seti.bruebabacken.dto.RegisterRequest;
import org.seti.bruebabacken.model.Cliente;
import org.seti.bruebabacken.service.ClienteService;
import org.seti.bruebabacken.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final ClienteService clienteService;
    private final JwtService jwtService;

    @Autowired
    public AuthController(ClienteService clienteService, JwtService jwtService) {
        this.clienteService = clienteService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Verificar si el email ya existe
            if (clienteService.obtenerClientePorEmail(request.getEmail()).isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El email ya está registrado");
                return ResponseEntity.badRequest().body(error);
            }

            // Crear nuevo cliente
            Cliente cliente = new Cliente();
            cliente.setNombre(request.getNombre());
            cliente.setEmail(request.getEmail());
            cliente.setTelefono(request.getTelefono());
            cliente.setPassword(request.getPassword()); // Se encriptará en el servicio
            cliente.setPreferenciaNotificacion(request.getPreferenciaNotificacion());

            Cliente clienteGuardado = clienteService.crearCliente(cliente);

            // Generar token JWT
            String token = jwtService.generateToken(clienteGuardado.getEmail(), clienteGuardado.getRole().toString());

            return ResponseEntity.ok(new AuthResponse(
                token,
                "Cliente registrado exitosamente",
                clienteGuardado.getEmail(),
                clienteGuardado.getRole().toString()
            ));

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al registrar cliente: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Buscar cliente por email
            var clienteOpt = clienteService.obtenerClientePorEmail(request.getEmail());
            
            if (clienteOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Credenciales inválidas");
                return ResponseEntity.status(401).body(error);
            }

            Cliente cliente = clienteOpt.get();

            // Validar contraseña
            if (!clienteService.validarPassword(request.getPassword(), cliente.getPassword())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Credenciales inválidas");
                return ResponseEntity.status(401).body(error);
            }

            // Generar token JWT
            String token = jwtService.generateToken(cliente.getEmail(), cliente.getRole().toString());

            return ResponseEntity.ok(new AuthResponse(
                token,
                "Login exitoso",
                cliente.getEmail(),
                cliente.getRole().toString()
            ));

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error en el login: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Token no válido");
                return ResponseEntity.status(401).body(error);
            }

            String token = authHeader.substring(7); // Remover "Bearer "
            String email = jwtService.extractUsername(token);

            var clienteOpt = clienteService.obtenerClientePorEmail(email);
            if (clienteOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Usuario no encontrado");
                return ResponseEntity.status(404).body(error);
            }

            Cliente cliente = clienteOpt.get();
            // No enviar la contraseña en la respuesta
            cliente.setPassword(null);

            return ResponseEntity.ok(cliente);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener usuario: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
} 