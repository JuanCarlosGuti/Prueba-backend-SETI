package org.seti.bruebabacken.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ClienteUpdateDTO {
    private String id;
    private String nombre;
    private String email;
    private String telefono;
    private String preferenciaNotificacion;
    private BigDecimal saldo;
    private String role; // ADMIN o CLIENTE
} 