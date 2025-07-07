package org.seti.bruebabacken.controller;

import org.seti.bruebabacken.model.Cliente;
import org.seti.bruebabacken.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // Crear un nuevo cliente
    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
        try {
            Cliente clienteGuardado = clienteRepository.save(cliente);
            return ResponseEntity.ok(clienteGuardado);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Obtener un cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable String id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Listar todos los clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        try {
            List<Cliente> clientes = clienteRepository.findAll();
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 