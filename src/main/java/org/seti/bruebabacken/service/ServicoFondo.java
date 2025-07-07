package org.seti.bruebabacken.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.seti.bruebabacken.model.Cliente;
import org.seti.bruebabacken.model.Fondo;
import org.seti.bruebabacken.model.Suscripcion;
import org.seti.bruebabacken.model.Transaccion;
import org.seti.bruebabacken.repository.ClienteRepository;
import org.seti.bruebabacken.repository.FondoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicoFondo {

    private final ClienteRepository clienteRepository;
    private final FondoRepository fondoRepository;
    private final NotificacionService notificacionService;

    @Autowired
    public ServicoFondo(ClienteRepository clienteRepository, 
                       FondoRepository fondoRepository,
                       NotificacionService notificacionService) {
        this.clienteRepository = clienteRepository;
        this.fondoRepository = fondoRepository;
        this.notificacionService = notificacionService;
    }

    public String suscribirFondo(String clienteId, String fondoId, BigDecimal monto) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Fondo fondo = fondoRepository.findById(fondoId)
                .orElseThrow(() -> new RuntimeException("Fondo no encontrado"));

        // Inicializar listas si son null
        if (cliente.getSuscripciones() == null) {
            cliente.setSuscripciones(new ArrayList<>());
        }
        if (cliente.getTransacciones() == null) {
            cliente.setTransacciones(new ArrayList<>());
        }

        if (cliente.getSaldo().compareTo(monto) < 0) {
            return "No tiene saldo disponible para vincularse al fondo " + fondo.getNombre();
        }
        if (monto.compareTo(fondo.getMontoMinimo()) < 0) {
            return "El monto es menor al mínimo requerido para el fondo " + fondo.getNombre();
        }

        cliente.setSaldo(cliente.getSaldo().subtract(monto));

        // Registrar suscripción
        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setIdFondo(fondoId);
        suscripcion.setMonto(monto);
        suscripcion.setFechaSuscripcion(LocalDateTime.now());
        suscripcion.setEstado("ACTIVA");
        cliente.getSuscripciones().add(suscripcion);

        // Registrar transacción
        Transaccion transaccion = new Transaccion();
        transaccion.setTipo("APERTURA");
        transaccion.setFondoId(fondoId);
        transaccion.setMonto(monto);
        cliente.getTransacciones().add(transaccion);

        clienteRepository.save(cliente);
        
        // Enviar notificación de suscripción exitosa
        notificacionService.notificarSuscripcion(cliente, fondo, monto.toString());
        
        return "Suscripción exitosa al fondo " + fondo.getNombre();
    }

    public String cancelarFondo(String clienteId, String fondoId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Fondo fondo = fondoRepository.findById(fondoId)
                .orElseThrow(() -> new RuntimeException("Fondo no encontrado"));

        if (cliente.getSuscripciones() == null) {
            return "No tiene suscripciones activas";
        }

        Optional<Suscripcion> suscripcionOpt = cliente.getSuscripciones().stream()
                .filter(s -> s.getIdFondo().equals(fondoId) && s.getEstado().equals("ACTIVA"))
                .findFirst();

        if (suscripcionOpt.isEmpty()) {
            return "No existe una suscripción activa a este fondo";
        }

        Suscripcion suscripcion = suscripcionOpt.get();
        suscripcion.setEstado("CANCELADA");

        cliente.setSaldo(cliente.getSaldo().add(suscripcion.getMonto()));

        // Registrar transacción
        if (cliente.getTransacciones() == null) {
            cliente.setTransacciones(new ArrayList<>());
        }
        
        Transaccion transaccion = new Transaccion();
        transaccion.setTipo("CANCELACION");
        transaccion.setFondoId(fondoId);
        transaccion.setMonto(suscripcion.getMonto());
        cliente.getTransacciones().add(transaccion);

        clienteRepository.save(cliente);
        
        // Enviar notificación de cancelación exitosa
        notificacionService.notificarCancelacion(cliente, fondo, suscripcion.getMonto().toString());
        
        return "Cancelación de suscripción exitosa.";
    }

    // 3. Ver historial de transacciones
    public List<Transaccion> obtenerHistorial(String clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        if (cliente.getTransacciones() == null) {
            return new ArrayList<>();
        }
        
        return cliente.getTransacciones();
    }
}
