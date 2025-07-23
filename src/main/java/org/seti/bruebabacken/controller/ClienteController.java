package org.seti.bruebabacken.controller;

import java.util.Optional;

import org.seti.bruebabacken.model.Cliente;
import org.seti.bruebabacken.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Endpoints para clientes normales
    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
        try {
            // No permitir crear clientes sin contraseña desde este endpoint
            // Usar /api/auth/register para registro completo
            if (cliente.getPassword() == null || cliente.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            Cliente clienteGuardado = clienteService.crearCliente(cliente);
            // No enviar la contraseña en la respuesta
            clienteGuardado.setPassword(null);
            return ResponseEntity.ok(clienteGuardado);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable String id, 
                                                 @RequestParam String currentUserId) {
        try {
            // Obtener el usuario actual
            Optional<Cliente> currentUser = clienteService.obtenerClientePorId(currentUserId);
            if (currentUser.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Obtener el cliente solicitado con verificación de permisos
            Optional<Cliente> cliente = clienteService.obtenerClienteConPermisos(currentUser.get(), id);
            if (cliente.isPresent()) {
                // No enviar la contraseña en la respuesta
                cliente.get().setPassword(null);
                return ResponseEntity.ok(cliente.get());
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Endpoint para que un usuario actualice sus propios datos
    @PutMapping("/perfil")
    public ResponseEntity<Cliente> actualizarPerfil(@RequestBody Cliente clienteActualizado,
                                                   @RequestParam String userId) {
        try {
            // Verificar que el usuario existe
            Optional<Cliente> currentUser = clienteService.obtenerClientePorId(userId);
            if (currentUser.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Solo puede actualizar su propio perfil
            if (!currentUser.get().getId().equals(clienteActualizado.getId())) {
                return ResponseEntity.status(403).build(); // Forbidden
            }

            Cliente clienteGuardado = clienteService.actualizarCliente(currentUser.get(), clienteActualizado);
            // No enviar la contraseña en la respuesta
            clienteGuardado.setPassword(null);
            return ResponseEntity.ok(clienteGuardado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 