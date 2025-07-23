package org.seti.bruebabacken.service;

import org.seti.bruebabacken.model.Cliente;
import org.seti.bruebabacken.model.Role;
import org.seti.bruebabacken.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminInitializationService implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminInitializationService(ClienteRepository clienteRepository, 
                                     PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existe un administrador
        if (!clienteRepository.existsByRole("ADMIN")) {
            crearAdminInicial();
        }
    }

    private void crearAdminInicial() {
        Cliente admin = new Cliente();
        admin.setNombre("Administrador del Sistema");
        admin.setEmail("admin@sistema.com");
        admin.setTelefono("3000000000");
        admin.setPassword(passwordEncoder.encode("admin123")); // Contraseña por defecto
        admin.setPreferenciaNotificacion("EMAIL");
        admin.setRole(Role.ADMIN);
        admin.setSaldo(new java.math.BigDecimal("1000000")); // Saldo inicial para admin

        clienteRepository.save(admin);
        System.out.println("Administrador inicial creado:");
        System.out.println("Email: admin@sistema.com");
        System.out.println("Contraseña: admin123");
        System.out.println("ID: " + admin.getId());
    }
} 