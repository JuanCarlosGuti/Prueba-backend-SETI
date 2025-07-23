package org.seti.bruebabacken.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nombre;
    private String email;
    private String telefono;
    private String password;
    private String preferenciaNotificacion;
} 