# 🏦 BTG Pactual - Plataforma de Gestión de Fondos

## 📋 Descripción
API REST para la gestión de fondos de inversión de BTG Pactual, permitiendo a los clientes suscribirse, cancelar suscripciones y ver historial de transacciones sin necesidad de contactar a un asesor.

## 🛠️ Tecnologías Utilizadas
- **Java 17**
- **Spring Boot 3.3.13**
- **Spring Data MongoDB**
- **Spring Security** (autenticación, autorización, roles, encriptación)
- **MongoDB**
- **Lombok**
- **Maven**
- **Spring Boot Mail** (para notificaciones por email)

## 🔒 Seguridad: Autenticación, Autorización, Roles y Encriptación

### **Autenticación y Autorización**
- Todos los endpoints bajo `/api/**` requieren autenticación básica HTTP.
- Se utiliza **Spring Security** con usuarios en memoria.
- Los endpoints públicos (como `/api/test/health`) no requieren autenticación.

### **Usuarios y Roles**
- Usuarios predefinidos:
  - **cliente** / **cliente123** (rol: CLIENTE)
  - **admin** / **admin123** (rol: ADMIN)
- Puedes agregar más usuarios y roles en la clase `SecurityConfig`.

### **Encriptación de contraseñas**
- Las contraseñas de los usuarios en memoria están encriptadas con **BCrypt**.
- Si implementas persistencia de usuarios, usa también BCrypt para guardar contraseñas.

### **Cómo probar la seguridad**

1. **Accede a cualquier endpoint `/api/**` (por ejemplo, `/api/fondos`)**
   - El sistema pedirá usuario y contraseña.
   - Usa uno de los usuarios predefinidos.

2. **En Postman:**
   - Ve a la pestaña "Authorization".
   - Tipo: **Basic Auth**.
   - Username: `cliente` o `admin`.
   - Password: `cliente123` o `admin123`.

3. **Si no pones credenciales o son incorrectas:**
   - Recibirás un error 401 Unauthorized.

4. **Los endpoints públicos (como `/api/test/health`) funcionan sin autenticación.**

---

## 🚀 Configuración y Ejecución

### Prerrequisitos
- Java 17 o superior
- Maven 3.6+
- MongoDB 4.4+ (instalado y ejecutándose en localhost:27017)
- Cuenta de Gmail (para notificaciones por email)

---

### ⚡️ Uso rápido de MongoDB con Docker en Amazon Linux 2023

Si usas Amazon Linux 2023 en EC2 y tienes problemas instalando MongoDB, puedes levantarlo fácilmente con Docker:

1. Instala Docker:
   ```sh
   sudo yum update -y
   sudo yum install -y docker
   sudo systemctl start docker
   sudo systemctl enable docker
   sudo usermod -aG docker ec2-user
   newgrp docker
   ```
2. Levanta MongoDB en un contenedor:
   ```sh
   docker run -d --name mongodb -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=admin mongo:6.0
   ```
3. Verifica que esté corriendo:
   ```sh
   docker ps
   ```

### Configuración de la URI de MongoDB en Spring Boot

En `src/main/resources/application.properties` usa:
```properties
spring.data.mongodb.uri=mongodb://admin:admin@localhost:27017/btg_fondos
```
Esto conecta tu backend a MongoDB en Docker con usuario y contraseña `admin`/`admin` y la base de datos `btg_fondos`.

---

### 1. Clonar y configurar
```bash
git clone https://github.com/JuanCarlosGuti/Prueba-backend-SETI
```

### 2. Configurar MongoDB
Asegúrate de que MongoDB esté ejecutándose:
```bash
# En Windows
mongod

# En Linux/Mac
sudo systemctl start mongod
```

### 3. Configurar notificaciones (opcional)
Edita `src/main/resources/application.properties`:

#### Para Email (Gmail):
```properties
spring.mail.username=tu-email@gmail.com
spring.mail.password=tu-app-password
```

### 4. Ejecutar la aplicación
```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## 📊 Base de Datos

### Configuración
- **Host**: localhost
- **Puerto**: 27017
- **Base de datos**: btg_fondos

### Fondos Predefinidos
Al iniciar la aplicación, se crean automáticamente los siguientes fondos:

| ID | Nombre | Monto Mínimo | Categoría |
|----|--------|--------------|-----------|
| 1 | FPV_BTG_PACTUAL_RECAUDADORA | COP $75.000 | FPV |
| 2 | FPV_BTG_PACTUAL_ECOPETROL | COP $125.000 | FPV |
| 3 | DEUDAPRIVADA | COP $50.000 | FIC |
| 4 | FDO-ACCIONES | COP $250.000 | FIC |
| 5 | FPV_BTG_PACTUAL_DINAMICA | COP $100.000 | FPV |

## 🔌 Endpoints de la API

### Health Check
- **GET** `/api/test/health` - Verificar estado de la aplicación
- **GET** `/api/test/info` - Información del proyecto

### Gestión de Clientes
- **GET** `/api/clientes` - Listar todos los clientes
- **GET** `/api/clientes/{id}` - Obtener cliente por ID
- **POST** `/api/clientes` - Crear nuevo cliente

### Gestión de Fondos
- **GET** `/api/fondos` - Listar todos los fondos
- **GET** `/api/fondos/{id}` - Obtener fondo por ID
- **POST** `/api/fondos/suscribir` - Suscribirse a un fondo
- **DELETE** `/api/fondos/cancelar` - Cancelar suscripción
- **GET** `/api/fondos/transacciones/{clienteId}` - Ver historial de transacciones

### Notificaciones
- **POST** `/api/notificaciones/test` - Probar notificación por email
- **GET** `/api/notificaciones/status` - Estado del sistema de notificaciones

## 📧 Sistema de Notificaciones

- Notificaciones automáticas al suscribirse/cancelar fondos
- Solo por email (Gmail SMTP)
- Preferencia del cliente: EMAIL

## 📝 Ejemplos de Uso

### 1. Crear un cliente
```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Pérez",
    "email": "juan@email.com",
    "telefono": "3001234567",
    "preferenciaNotificacion": "EMAIL"
  }'
```

### 2. Suscribirse a un fondo
```bash
curl -X POST "http://localhost:8080/api/fondos/suscribir?clienteId=CLIENTE_ID&fondoId=1&monto=100000" \
  -u cliente:cliente123
```

### 3. Cancelar suscripción
```bash
curl -X DELETE "http://localhost:8080/api/fondos/cancelar?clienteId=CLIENTE_ID&fondoId=1" \
  -u cliente:cliente123
```

### 4. Ver historial de transacciones
```bash
curl -X GET "http://localhost:8080/api/fondos/transacciones/CLIENTE_ID" \
  -u cliente:cliente123
```

### 5. Probar notificación por email
```bash
curl -X POST "http://localhost:8080/api/notificaciones/test" \
  -d "email=tu-email@gmail.com" \
  -u cliente:cliente123
```

## 🏗️ Arquitectura

### Modelos
- **Cliente**: Información del cliente, saldo, suscripciones y transacciones
- **Fondo**: Información del fondo, monto mínimo y categoría
- **Suscripcion**: Estado de la suscripción de un cliente a un fondo
- **Transaccion**: Historial de operaciones (aperturas y cancelaciones)

### Servicios
- **ServicoFondo**: Lógica de negocio para gestión de fondos
- **NotificacionService**: Sistema de notificaciones por email
- **DataInitializer**: Inicialización de datos de prueba

### Repositorios
- **ClienteRepository**: Acceso a datos de clientes
- **FondoRepository**: Acceso a datos de fondos

## 🔒 Reglas de Negocio

1. **Monto inicial del cliente**: COP $500.000
2. **Transacciones únicas**: Cada transacción tiene un identificador único (UUID)
3. **Monto mínimo**: Cada fondo tiene un monto mínimo de vinculación
4. **Saldo suficiente**: Validación antes de suscribirse
5. **Retorno de saldo**: Al cancelar, el valor se retorna al cliente
6. **Notificaciones automáticas**: Se envían al suscribirse/cancelar según preferencia
7. **Seguridad**: Autenticación, autorización, roles y encriptación

## 🧪 Pruebas

### Ejecutar pruebas unitarias
```bash
mvn test
```

### Verificar endpoints
```bash
# Health check
curl http://localhost:8080/api/test/health

# Listar fondos (requiere autenticación)
curl -u cliente:cliente123 http://localhost:8080/api/fondos

# Estado de notificaciones
curl -u cliente:cliente123 http://localhost:8080/api/notificaciones/status
```

## 📦 Despliegue

### JAR ejecutable
```bash
mvn clean package
java -jar target/bruebaBacken-0.0.1-SNAPSHOT.jar
```

### Variables de entorno
```bash
export SPRING_DATA_MONGODB_HOST=your-mongodb-host
export SPRING_DATA_MONGODB_PORT=27017
export SPRING_DATA_MONGODB_DATABASE=btg_fondos
export SPRING_MAIL_USERNAME=your-email@gmail.com
export SPRING_MAIL_PASSWORD=your-app-password
```

## 🤝 Contribución

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 👥 Autores

- **BTG Pactual** - *Prueba técnica de desarrollo backend*

## 🙏 Agradecimientos

- Spring Boot Team
- MongoDB Team
- Comunidad de desarrolladores Java 