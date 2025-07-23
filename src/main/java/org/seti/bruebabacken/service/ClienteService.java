package org.seti.bruebabacken.service;

import java.util.List;
import java.util.Optional;

import org.seti.bruebabacken.model.Cliente;
import org.seti.bruebabacken.model.Role;
import org.seti.bruebabacken.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationService authorizationService;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository, 
                         PasswordEncoder passwordEncoder,
                         AuthorizationService authorizationService) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorizationService = authorizationService;
    }

    /**
     * Crear un nuevo cliente
     * @param cliente Cliente a crear
     * @return Cliente creado
     * @throws RuntimeException si hay error al guardar
     */
    public Cliente crearCliente(Cliente cliente) {
        try {
            // Encriptar la contraseña antes de guardar
            if (cliente.getPassword() != null) {
                String passwordEncriptada = passwordEncoder.encode(cliente.getPassword());
                cliente.setPassword(passwordEncriptada);
            }
            return clienteRepository.save(cliente);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el cliente: " + e.getMessage());
        }
    }

    /**
     * Obtener cliente por email
     * @param email Email del cliente
     * @return Optional con el cliente si existe
     */
    public Optional<Cliente> obtenerClientePorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    /**
     * Validar contraseña de un cliente
     * @param rawPassword Contraseña en texto plano
     * @param encodedPassword Contraseña encriptada
     * @return true si la contraseña es válida
     */
    public boolean validarPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
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
     * Actualizar un cliente (solo para admins o el propio usuario)
     * @param currentUser Usuario que realiza la operación
     * @param clienteActualizado Cliente con datos actualizados
     * @return Cliente actualizado
     * @throws RuntimeException si no tiene permisos o hay error
     */
    public Cliente actualizarCliente(Cliente currentUser, Cliente clienteActualizado) {
        try {
            // Verificar que el cliente existe
            Optional<Cliente> clienteExistente = clienteRepository.findById(clienteActualizado.getId());
            if (clienteExistente.isEmpty()) {
                throw new RuntimeException("Cliente no encontrado con ID: " + clienteActualizado.getId());
            }

            Cliente targetCliente = clienteExistente.get();

            // Verificar permisos
            if (!authorizationService.canModifyUser(currentUser, targetCliente) && 
                !authorizationService.canAccessOwnData(currentUser, clienteActualizado.getId())) {
                throw new RuntimeException("No tiene permisos para modificar este usuario");
            }

            // Si es admin, puede cambiar el rol
            if (authorizationService.isAdmin(currentUser)) {
                targetCliente.setRole(clienteActualizado.getRole());
            }

            // Actualizar otros campos
            targetCliente.setNombre(clienteActualizado.getNombre());
            targetCliente.setEmail(clienteActualizado.getEmail());
            targetCliente.setTelefono(clienteActualizado.getTelefono());
            targetCliente.setPreferenciaNotificacion(clienteActualizado.getPreferenciaNotificacion());
            
            // Solo los admins pueden modificar el saldo
            if (authorizationService.isAdmin(currentUser)) {
                targetCliente.setSaldo(clienteActualizado.getSaldo());
            }

            return clienteRepository.save(targetCliente);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el cliente: " + e.getMessage());
        }
    }

    /**
     * Eliminar un cliente (solo para admins)
     * @param currentUser Usuario que realiza la operación
     * @param clienteId ID del cliente a eliminar
     * @throws RuntimeException si no tiene permisos o hay error
     */
    public void eliminarCliente(Cliente currentUser, String clienteId) {
        try {
            // Verificar que el cliente existe
            Optional<Cliente> clienteExistente = clienteRepository.findById(clienteId);
            if (clienteExistente.isEmpty()) {
                throw new RuntimeException("Cliente no encontrado con ID: " + clienteId);
            }

            Cliente targetCliente = clienteExistente.get();

            // Verificar permisos
            if (!authorizationService.canDeleteUser(currentUser, targetCliente)) {
                throw new RuntimeException("No tiene permisos para eliminar este usuario");
            }

            clienteRepository.deleteById(clienteId);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el cliente: " + e.getMessage());
        }
    }

    /**
     * Obtener cliente por ID con verificación de permisos
     * @param currentUser Usuario que realiza la consulta
     * @param clienteId ID del cliente a consultar
     * @return Optional con el cliente si tiene permisos
     */
    public Optional<Cliente> obtenerClienteConPermisos(Cliente currentUser, String clienteId) {
        if (!authorizationService.canAccessOwnData(currentUser, clienteId)) {
            return Optional.empty();
        }
        return clienteRepository.findById(clienteId);
    }

    /**
     * Listar todos los clientes (solo para admins)
     * @param currentUser Usuario que realiza la consulta
     * @return Lista de todos los clientes
     * @throws RuntimeException si no es admin
     */
    public List<Cliente> listarTodosLosClientes(Cliente currentUser) {
        if (!authorizationService.isAdmin(currentUser)) {
            throw new RuntimeException("Solo los administradores pueden listar todos los clientes");
        }
        
        try {
            return clienteRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar los clientes: " + e.getMessage());
        }
    }

    /**
     * Crear un cliente admin (solo para admins existentes)
     * @param currentUser Usuario que realiza la operación
     * @param cliente Cliente a crear
     * @return Cliente creado
     * @throws RuntimeException si no tiene permisos
     */
    public Cliente crearClienteAdmin(Cliente currentUser, Cliente cliente) {
        if (!authorizationService.isAdmin(currentUser)) {
            throw new RuntimeException("Solo los administradores pueden crear otros administradores");
        }
        
        // Establecer el rol como ADMIN
        cliente.setRole(Role.ADMIN);
        return crearCliente(cliente);
    }
}
