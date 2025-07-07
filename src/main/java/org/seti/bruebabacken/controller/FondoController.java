package org.seti.bruebabacken.controller;

import org.seti.bruebabacken.model.Fondo;
import org.seti.bruebabacken.model.Transaccion;
import org.seti.bruebabacken.repository.FondoRepository;
import org.seti.bruebabacken.service.ServicoFondo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fondos")
@CrossOrigin(origins = "*")
public class FondoController {

    private final ServicoFondo fondoService;
    private final FondoRepository fondoRepository;

    @Autowired
    public FondoController(ServicoFondo fondoService, FondoRepository fondoRepository) {
        this.fondoService = fondoService;
        this.fondoRepository = fondoRepository;
    }

    // Listar todos los fondos disponibles
    @GetMapping
    public ResponseEntity<List<Fondo>> listarFondos() {
        try {
            List<Fondo> fondos = fondoRepository.findAll();
            return ResponseEntity.ok(fondos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Obtener un fondo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Fondo> obtenerFondo(@PathVariable String id) {
        Optional<Fondo> fondo = fondoRepository.findById(id);
        return fondo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 1. Suscribirse a un nuevo fondo (apertura)
    @PostMapping("/suscribir")
    public ResponseEntity<String> suscribirFondo(
            @RequestParam String clienteId,
            @RequestParam String fondoId,
            @RequestParam BigDecimal monto) {
        
        try {
            String resultado = fondoService.suscribirFondo(clienteId, fondoId, monto);
            return ResponseEntity.ok(resultado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno del servidor");
        }
    }

    // 2. Cancelar la suscripción a un fondo actual
    @DeleteMapping("/cancelar")
    public ResponseEntity<String> cancelarFondo(
            @RequestParam String clienteId,
            @RequestParam String fondoId) {
        
        try {
            String resultado = fondoService.cancelarFondo(clienteId, fondoId);
            return ResponseEntity.ok(resultado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno del servidor");
        }
    }

    // 3. Ver historial de transacciones (aperturas y cancelaciones)
    @GetMapping("/transacciones/{clienteId}")
    public ResponseEntity<List<Transaccion>> obtenerHistorialTransacciones(
            @PathVariable String clienteId) {
        
        try {
            List<Transaccion> transacciones = fondoService.obtenerHistorial(clienteId);
            return ResponseEntity.ok(transacciones);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 