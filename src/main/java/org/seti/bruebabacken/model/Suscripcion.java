package org.seti.bruebabacken.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Suscripcion {
    private String idFondo;
    private BigDecimal monto;
    private LocalDateTime fechaSuscripcion;
    private String estado; // "ACTIVA" o "CANCELADA"
} 