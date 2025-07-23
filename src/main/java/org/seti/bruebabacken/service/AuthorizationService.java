package org.seti.bruebabacken.service;

import org.seti.bruebabacken.model.Cliente;
import org.seti.bruebabacken.model.Role;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    /**
     * Verificar si un usuario tiene el rol requerido
     * @param cliente Cliente a verificar
     * @param roleRequired Rol requerido
     * @return true si tiene el rol requerido
     */
    public boolean hasRole(Cliente cliente, Role roleRequired) {
        if (cliente == null || cliente.getRole() == null) {
            return false;
        }
        
        try {
            Role userRole = cliente.getRole();
            return userRole == roleRequired || userRole == Role.ADMIN;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Verificar si un usuario es admin
     * @param cliente Cliente a verificar
     * @return true si es admin
     */
    public boolean isAdmin(Cliente cliente) {
        return hasRole(cliente, Role.ADMIN);
    }

    /**
     * Verificar si un usuario puede modificar a otro usuario
     * @param currentUser Usuario actual
     * @param targetUser Usuario objetivo
     * @return true si puede modificar
     */
    public boolean canModifyUser(Cliente currentUser, Cliente targetUser) {
        // Solo los admins pueden modificar usuarios
        if (!isAdmin(currentUser)) {
            return false;
        }
        
        // Un admin no puede modificar a otro admin (por seguridad)
        if (isAdmin(targetUser)) {
            return false;
        }
        
        return true;
    }

    /**
     * Verificar si un usuario puede eliminar a otro usuario
     * @param currentUser Usuario actual
     * @param targetUser Usuario objetivo
     * @return true si puede eliminar
     */
    public boolean canDeleteUser(Cliente currentUser, Cliente targetUser) {
        // Solo los admins pueden eliminar usuarios
        if (!isAdmin(currentUser)) {
            return false;
        }
        
        // Un admin no puede eliminar a otro admin (por seguridad)
        if (isAdmin(targetUser)) {
            return false;
        }
        
        return true;
    }

    /**
     * Verificar si un usuario puede acceder a sus propios datos
     * @param currentUser Usuario actual
     * @param targetUserId ID del usuario objetivo
     * @return true si puede acceder
     */
    public boolean canAccessOwnData(Cliente currentUser, String targetUserId) {
        // Los admins pueden acceder a cualquier dato
        if (isAdmin(currentUser)) {
            return true;
        }
        
        // Los clientes solo pueden acceder a sus propios datos
        return currentUser.getId().equals(targetUserId);
    }
} 