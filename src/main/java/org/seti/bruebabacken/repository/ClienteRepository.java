package org.seti.bruebabacken.repository;

import java.util.Optional;

import org.seti.bruebabacken.model.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, String> {
    
    /**
     * Buscar cliente por email
     * @param email Email del cliente
     * @return Optional con el cliente si existe
     */
    Optional<Cliente> findByEmail(String email);
    
    /**
     * Verificar si existe un cliente con el email dado
     * @param email Email a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);
    
    /**
     * Verificar si existe un cliente con el rol dado
     * @param role Rol a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByRole(String role);
} 