package org.seti.bruebabacken.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.seti.bruebabacken.model.Cliente;
import org.seti.bruebabacken.model.Role;
import org.seti.bruebabacken.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final ClienteService clienteService;

    @Autowired
    public AdminController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Listar todos los clientes (solo para administradores)
     */
    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> listarTodosLosClientes(@RequestParam String adminId) {
        try {
            // Verificar que el usuario es admin
            Optional<Cliente> admin = clienteService.obtenerClientePorId(adminId);
            if (admin.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            List<Cliente> clientes = clienteService.listarTodosLosClientes(admin.get());
            // No enviar las contraseñas en la respuesta
            clientes.forEach(cliente -> cliente.setPassword(null));
            return ResponseEntity.ok(clientes);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).build(); // Forbidden
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Obtener un cliente específico (solo para administradores)
     */
    @GetMapping("/clientes/{clienteId}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable String clienteId,
                                                 @RequestParam String adminId) {
        try {
            // Verificar que el usuario es admin
            Optional<Cliente> admin = clienteService.obtenerClientePorId(adminId);
            if (admin.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Optional<Cliente> cliente = clienteService.obtenerClientePorId(clienteId);
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

    /**
     * Actualizar un cliente (solo para administradores)
     */
    @PutMapping("/clientes")
    public ResponseEntity<Cliente> actualizarCliente(@RequestBody Cliente clienteActualizado,
                                                    @RequestParam String adminId) {
        try {
            // Verificar que el usuario es admin
            Optional<Cliente> admin = clienteService.obtenerClientePorId(adminId);
            if (admin.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Cliente clienteGuardado = clienteService.actualizarCliente(admin.get(), clienteActualizado);
            // No enviar la contraseña en la respuesta
            clienteGuardado.setPassword(null);
            return ResponseEntity.ok(clienteGuardado);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("No tiene permisos")) {
                return ResponseEntity.status(403).build(); // Forbidden
            }
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Eliminar un cliente (solo para administradores)
     */
    @DeleteMapping("/clientes/{clienteId}")
    public ResponseEntity<String> eliminarCliente(@PathVariable String clienteId,
                                                 @RequestParam String adminId) {
        try {
            // Verificar que el usuario es admin
            Optional<Cliente> admin = clienteService.obtenerClientePorId(adminId);
            if (admin.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            clienteService.eliminarCliente(admin.get(), clienteId);
            return ResponseEntity.ok("Cliente eliminado exitosamente");
        } catch (RuntimeException e) {
            if (e.getMessage().contains("No tiene permisos")) {
                return ResponseEntity.status(403).build(); // Forbidden
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Crear un nuevo administrador (solo para administradores existentes)
     */
    @PostMapping("/crear-admin")
    public ResponseEntity<Cliente> crearClienteAdmin(@RequestBody Cliente cliente,
                                                    @RequestParam String adminId) {
        try {
            // Verificar que el usuario es admin
            Optional<Cliente> admin = clienteService.obtenerClientePorId(adminId);
            if (admin.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Cliente clienteGuardado = clienteService.crearClienteAdmin(admin.get(), cliente);
            // No enviar la contraseña en la respuesta
            clienteGuardado.setPassword(null);
            return ResponseEntity.ok(clienteGuardado);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Solo los administradores")) {
                return ResponseEntity.status(403).build(); // Forbidden
            }
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Obtener estadísticas del sistema (solo para administradores)
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Object> obtenerEstadisticas(@RequestParam String adminId) {
        try {
            // Verificar que el usuario es admin
            Optional<Cliente> admin = clienteService.obtenerClientePorId(adminId);
            if (admin.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            List<Cliente> todosLosClientes = clienteService.listarTodosLosClientes(admin.get());
            
            // Calcular estadísticas básicas
            long totalClientes = todosLosClientes.size();
            long totalAdmins = todosLosClientes.stream()
                    .filter(c -> Role.ADMIN.equals(c.getRole()))
                    .count();
            long totalClientesNormales = totalClientes - totalAdmins;

            return ResponseEntity.ok(Map.of(
                "totalUsuarios", totalClientes,
                "totalAdmins", totalAdmins,
                "totalClientes", totalClientesNormales
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).build(); // Forbidden
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 