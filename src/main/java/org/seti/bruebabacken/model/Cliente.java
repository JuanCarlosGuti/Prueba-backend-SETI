package org.seti.bruebabacken.model;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("clientes")
public class Cliente {
    @Id
    private String id;
    private String nombre;
    private String email;
    private String telefono;
    private String preferenciaNotificacion;
    private BigDecimal saldo = new BigDecimal("500000");
    private List<Suscripcion> suscripciones;
    private List<Transaccion> transacciones;
}

