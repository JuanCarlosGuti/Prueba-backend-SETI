# Ejemplos de Bodies para BTG Fondos API

## 1. CLIENTES

### Crear Cliente (POST /api/clientes)

**Body JSON:**
```json
{
    "nombre": "Juan Carlos Gutiérrez",
    "email": "juancgutierrezh@gmail.com",
    "telefono": "+573001234567",
    "preferenciaNotificacion": "EMAIL"
}
```

**Otros ejemplos de bodies:**

```json
{
    "nombre": "María González",
    "email": "maria.gonzalez@email.com",
    "telefono": "+573001234568",
    "preferenciaNotificacion": "SMS"
}
```

```json
{
    "nombre": "Carlos Rodríguez",
    "email": "carlos.rodriguez@email.com",
    "telefono": "+573001234569",
    "preferenciaNotificacion": "EMAIL"
}
```

**Respuesta esperada:**
```json
{
    "id": "64f8a1b2c3d4e5f6a7b8c9d0",
    "nombre": "Juan Carlos Gutiérrez",
    "email": "juancgutierrezh@gmail.com",
    "telefono": "+573001234567",
    "preferenciaNotificacion": "EMAIL",
    "saldo": 500000,
    "suscripciones": [],
    "transacciones": []
}
```

### Obtener Cliente por ID (GET /api/clientes/{id})
**No requiere body**

### Listar Todos los Clientes (GET /api/clientes)
**No requiere body**

---

## 2. FONDOS

### Listar Todos los Fondos (GET /api/fondos)
**No requiere body**

### Obtener Fondo por ID (GET /api/fondos/{id})
**No requiere body**

### Suscribirse a Fondo (POST /api/fondos/suscribir)

**Parámetros de Query:**
- `clienteId`: ID del cliente
- `fondoId`: ID del fondo
- `monto`: Monto a invertir

**Ejemplos de URLs:**
```
POST /api/fondos/suscribir?clienteId=64f8a1b2c3d4e5f6a7b8c9d0&fondoId=64f8a1b2c3d4e5f6a7b8c9d1&monto=100000
POST /api/fondos/suscribir?clienteId=64f8a1b2c3d4e5f6a7b8c9d0&fondoId=64f8a1b2c3d4e5f6a7b8c9d1&monto=250000
POST /api/fondos/suscribir?clienteId=64f8a1b2c3d4e5f6a7b8c9d0&fondoId=64f8a1b2c3d4e5f6a7b8c9d1&monto=500000
```

**Respuesta esperada:**
```json
"Suscripción exitosa al fondo. Monto invertido: 100000"
```

### Cancelar Suscripción (DELETE /api/fondos/cancelar)

**Parámetros de Query:**
- `clienteId`: ID del cliente
- `fondoId`: ID del fondo

**Ejemplos de URLs:**
```
DELETE /api/fondos/cancelar?clienteId=64f8a1b2c3d4e5f6a7b8c9d0&fondoId=64f8a1b2c3d4e5f6a7b8c9d1
```

**Respuesta esperada:**
```json
"Suscripción cancelada exitosamente. Monto devuelto: 100000"
```

### Historial de Transacciones (GET /api/fondos/transacciones/{clienteId})
**No requiere body**

**Respuesta esperada:**
```json
[
    {
        "id": "64f8a1b2c3d4e5f6a7b8c9d2",
        "clienteId": "64f8a1b2c3d4e5f6a7b8c9d0",
        "fondoId": "64f8a1b2c3d4e5f6a7b8c9d1",
        "tipo": "SUSCRIPCION",
        "monto": 100000,
        "fecha": "2024-01-15T10:30:00"
    },
    {
        "id": "64f8a1b2c3d4e5f6a7b8c9d3",
        "clienteId": "64f8a1b2c3d4e5f6a7b8c9d0",
        "fondoId": "64f8a1b2c3d4e5f6a7b8c9d1",
        "tipo": "CANCELACION",
        "monto": 100000,
        "fecha": "2024-01-16T14:45:00"
    }
]
```

---

## 3. NOTIFICACIONES

### Probar Notificación (POST /api/notificaciones/test)

**Parámetros de Query:**
- `email`: Email donde enviar la notificación

**Ejemplos de URLs:**
```
POST /api/notificaciones/test?email=juancgutierrezh@gmail.com
POST /api/notificaciones/test?email=maria.gonzalez@email.com
POST /api/notificaciones/test?email=carlos.rodriguez@email.com
```

**Respuesta esperada:**
```json
{
    "mensaje": "Notificación de prueba enviada exitosamente",
    "destinatario": "juancgutierrezh@gmail.com"
}
```

### Estado de Notificaciones (GET /api/notificaciones/status)
**No requiere body**

**Respuesta esperada:**
```json
{
    "notificaciones_habilitadas": true,
    "email_habilitado": true,
    "mensaje": "Sistema de notificaciones por email funcionando correctamente"
}
```

---

## 4. TEST & HEALTH

### Health Check (GET /api/test/health)
**No requiere body**

**Respuesta esperada:**
```json
{
    "status": "OK",
    "timestamp": "2024-01-15T10:30:00",
    "application": "BTG Fondos API",
    "version": "1.0.0"
}
```

### Get API Info (GET /api/test/info)
**No requiere body**

**Respuesta esperada:**
```json
{
    "empresa": "BTG Pactual",
    "proyecto": "Plataforma de Gestión de Fondos",
    "tecnologias": "Spring Boot, MongoDB, Java 17",
    "funcionalidades": [
        "Suscribirse a fondos",
        "Cancelar suscripciones",
        "Ver historial de transacciones",
        "Notificaciones por email/SMS"
    ]
}
```

---

## Variables de Entorno para Postman

Configura estas variables en tu colección de Postman:

```json
{
    "base_url": "http://localhost:8080",
    "cliente_id": "64f8a1b2c3d4e5f6a7b8c9d0",
    "fondo_id": "64f8a1b2c3d4e5f6a7b8c9d1"
}
```

## Flujo de Prueba Recomendado

1. **Health Check** - Verificar que la API esté funcionando
2. **Crear Cliente** - Crear un cliente de prueba
3. **Listar Fondos** - Ver fondos disponibles
4. **Suscribirse a Fondo** - Usar los IDs obtenidos
5. **Historial de Transacciones** - Verificar la transacción
6. **Probar Notificación** - Enviar email de prueba
7. **Cancelar Suscripción** - Cancelar la suscripción
8. **Historial de Transacciones** - Verificar ambas transacciones 