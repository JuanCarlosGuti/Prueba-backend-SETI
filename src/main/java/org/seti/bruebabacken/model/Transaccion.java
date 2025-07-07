package org.seti.bruebabacken.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
public class Transaccion {
    private String id = UUID.randomUUID().toString();
    private String tipo; // "APERTURA" o "CANCELACION"
    private String fondoId;
    private BigDecimal monto;
    private LocalDateTime fecha = LocalDateTime.now();
}

