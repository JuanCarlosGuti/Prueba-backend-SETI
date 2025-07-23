package org.seti.bruebabacken.service;

import org.seti.bruebabacken.model.Cliente;
import org.seti.bruebabacken.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Crear un nuevo cliente
     * @param cliente Cliente a crear
     * @return Cliente creado
     * @throws RuntimeException si hay error al guardar
     */
    public Cliente crearCliente(Cliente cliente) {
        try {
            return clienteRepository.save(cliente);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el cliente: " + e.getMessage());
        }
    }

    /**
     * Obtener un cliente por su ID
     * @param id ID del cliente
     * @return Optional con el cliente si existe
     */
    public Optional<Cliente> obtenerClientePorId(String id) {
        return clienteRepository.findById(id);
    }

    /**
     * Listar todos los clientes
     * @return Lista de todos los clientes
     * @throws RuntimeException si hay error al consultar
     */
    public List<Cliente> listarTodosLosClientes() {
        try {
            return clienteRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar los clientes: " + e.getMessage());
        }
    }

    /**
     * Verificar si un cliente existe por su ID
     * @param id ID del cliente
     * @return true si existe, false en caso contrario
     */
    public boolean existeCliente(String id) {
        return clienteRepository.existsById(id);
    }

    /**
     * Actualizar un cliente existente
     * @param cliente Cliente con datos actualizados
     * @return Cliente actualizado
     * @throws RuntimeException si hay error al actualizar
     */
    public Cliente actualizarCliente(Cliente cliente) {
        try {
            if (!clienteRepository.existsById(cliente.getId())) {
                throw new RuntimeException("Cliente no encontrado con ID: " + cliente.getId());
            }
            return clienteRepository.save(cliente);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el cliente: " + e.getMessage());
        }
    }

    /**
     * Eliminar un cliente por su ID
     * @param id ID del cliente a eliminar
     * @throws RuntimeException si hay error al eliminar
     */
    public void eliminarCliente(String id) {
        try {
            if (!clienteRepository.existsById(id)) {
                throw new RuntimeException("Cliente no encontrado con ID: " + id);
            }
            clienteRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el cliente: " + e.getMessage());
        }
    }
}
