# Sistema de Roles - Backend SETI

## Descripción General

Se ha implementado un sistema de roles que permite diferenciar entre **Administradores** y **Clientes**, con diferentes niveles de permisos para cada uno.

## Roles Disponibles

### 1. ADMIN
- **Permisos completos** sobre el sistema
- Puede gestionar todos los usuarios (ver, modificar, eliminar)
- Puede crear nuevos administradores
- Puede modificar saldos de usuarios
- Puede acceder a estadísticas del sistema

### 2. CLIENTE
- **Permisos limitados** a sus propias operaciones
- Solo puede suscribirse/desuscribirse a fondos
- Solo puede ver y modificar su propio perfil
- No puede acceder a información de otros usuarios

## Endpoints Disponibles

### Para Clientes Normales

#### Obtener Perfil Propio
```
GET /api/clientes/{id}?currentUserId={userId}
```

#### Actualizar Perfil Propio
```
PUT /api/clientes/perfil?userId={userId}
Body: Cliente (sin password)
```

#### Suscribirse a Fondo
```
POST /api/fondos/suscribir
Body: { clienteId, fondoId, monto }
```

#### Desuscribirse de Fondo
```
POST /api/fondos/cancelar
Body: { clienteId, fondoId }
```

#### Ver Historial de Transacciones
```
GET /api/fondos/historial/{clienteId}
```

### Para Administradores

#### Listar Todos los Clientes
```
GET /api/admin/clientes?adminId={adminId}
```

#### Obtener Cliente Específico
```
GET /api/admin/clientes/{clienteId}?adminId={adminId}
```

#### Actualizar Cliente
```
PUT /api/admin/clientes?adminId={adminId}
Body: Cliente (puede incluir cambios de rol y saldo)
```

#### Eliminar Cliente
```
DELETE /api/admin/clientes/{clienteId}?adminId={adminId}
```

#### Crear Nuevo Administrador
```
POST /api/admin/crear-admin?adminId={adminId}
Body: Cliente (se establecerá automáticamente como ADMIN)
```

#### Ver Estadísticas del Sistema
```
GET /api/admin/estadisticas?adminId={adminId}
```

## Administrador Inicial

Al iniciar la aplicación por primera vez, se crea automáticamente un administrador con las siguientes credenciales:

- **Email**: admin@sistema.com
- **Contraseña**: admin123
- **ID**: Se genera automáticamente

**⚠️ IMPORTANTE**: Cambiar estas credenciales después del primer inicio por seguridad.

## Reglas de Seguridad

1. **Un admin no puede modificar/eliminar a otro admin** (por seguridad)
2. **Los clientes solo pueden acceder a sus propios datos**
3. **Solo los admins pueden crear otros admins**
4. **Solo los admins pueden modificar saldos de usuarios**
5. **Las contraseñas nunca se envían en las respuestas**

## Ejemplos de Uso

### Crear un Nuevo Cliente (Registro Normal)
```json
POST /api/auth/register
{
  "nombre": "Juan Pérez",
  "email": "juan@ejemplo.com",
  "password": "miContraseña123",
  "telefono": "3001234567"
}
```

### Login de Administrador
```json
POST /api/auth/login
{
  "email": "admin@sistema.com",
  "password": "admin123"
}
```

### Crear Nuevo Administrador (Solo por Admin Existente)
```json
POST /api/admin/crear-admin?adminId={adminId}
{
  "nombre": "Nuevo Admin",
  "email": "nuevoadmin@ejemplo.com",
  "password": "adminPassword123",
  "telefono": "3009876543"
}
```

### Modificar Cliente (Solo Admin)
```json
PUT /api/admin/clientes?adminId={adminId}
{
  "id": "clienteId",
  "nombre": "Juan Pérez Modificado",
  "email": "juan.nuevo@ejemplo.com",
  "role": "CLIENTE",
  "saldo": 750000
}
```

## Códigos de Respuesta

- **200**: Operación exitosa
- **400**: Error en los datos enviados
- **403**: Sin permisos para realizar la operación
- **404**: Recurso no encontrado
- **500**: Error interno del servidor

## Notas de Implementación

- El sistema usa **Spring Security** para encriptación de contraseñas
- Los roles se almacenan como **enum** en la base de datos
- Se implementa **validación de permisos** en cada operación
- Las **notificaciones** se envían automáticamente para suscripciones/cancelaciones
- El sistema es **escalable** y permite agregar nuevos roles fácilmente 