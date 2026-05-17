# Ejemplos de Uso - PQRS Java Application

## 1. Usando la API REST (Spring Boot)

### Iniciar la aplicación
```bash
cd e:\SENA\PQRS\PQRS-JAVA
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

### 1.1 CREAR un usuario (POST)

**cURL:**
```bash
curl -X POST http://localhost:8080/api/fundusuario \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Carlos Alonso",
    "email": "carlos.alonso@company.com",
    "telefono": "3105551234",
    "activo": true
  }'
```

**Respuesta esperada (201 Created):**
```json
{
  "id": 1,
  "nombre": "Carlos Alonso",
  "email": "carlos.alonso@company.com",
  "telefono": "3105551234",
  "activo": true,
  "fechaCreacion": "2026-05-16T21:40:58"
}
```

### 1.2 OBTENER todos los usuarios (GET)

**cURL:**
```bash
curl http://localhost:8080/api/fundusuario
```

**Respuesta esperada (200 OK):**
```json
[
  {
    "id": 1,
    "nombre": "Carlos Alonso",
    "email": "carlos.alonso@company.com",
    "telefono": "3105551234",
    "activo": true,
    "fechaCreacion": "2026-05-16T21:40:58"
  },
  {
    "id": 2,
    "nombre": "María García",
    "email": "maria.garcia@company.com",
    "telefono": "3155559876",
    "activo": true,
    "fechaCreacion": "2026-05-16T21:45:00"
  }
]
```

### 1.3 OBTENER usuario por ID (GET)

**cURL:**
```bash
curl http://localhost:8080/api/fundusuario/1
```

**Respuesta esperada (200 OK):**
```json
{
  "id": 1,
  "nombre": "Carlos Alonso",
  "email": "carlos.alonso@company.com",
  "telefono": "3105551234",
  "activo": true,
  "fechaCreacion": "2026-05-16T21:40:58"
}
```

### 1.4 OBTENER usuario por Email (GET)

**cURL:**
```bash
curl "http://localhost:8080/api/fundusuario/email/carlos.alonso@company.com"
```

### 1.5 ACTUALIZAR usuario (PUT)

**cURL:**
```bash
curl -X PUT http://localhost:8080/api/fundusuario/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Carlos Alonso Pérez",
    "email": "carlos.perez@company.com",
    "telefono": "3105559999",
    "activo": true
  }'
```

**Respuesta esperada (200 OK):**
```json
{
  "id": 1,
  "nombre": "Carlos Alonso Pérez",
  "email": "carlos.perez@company.com",
  "telefono": "3105559999",
  "activo": true,
  "fechaCreacion": "2026-05-16T21:40:58"
}
```

### 1.6 ELIMINAR usuario (DELETE)

**cURL:**
```bash
curl -X DELETE http://localhost:8080/api/fundusuario/1
```

**Respuesta esperada (204 No Content)** - Sin cuerpo en la respuesta

---

## 2. Usando JDBC Puro (Sin Spring Boot)

### 2.1 Probando la conexión

**Compilar y ejecutar:**
```bash
cd e:\SENA\PQRS\PQRS-JAVA
javac -d target/classes src/main/java/com/pqrs/util/ConexionJDBC.java
javac -d target/classes -cp target/classes src/main/java/com/pqrs/util/TestConexion.java
java -cp target/classes;path/a/mariadb-java-client-3.0.8.jar com.pqrs.util.TestConexion
```

**Salida esperada:**
```
=== Test de Conexión a MariaDB ===
✓ Conexión exitosa a la base de datos
Driver: MariaDB connector/J
Base de datos: MariaDB
Versión: 10.6.x

=== Tablas en bdpqrsej ===
- fundusuario
  Estructura de fundusuario:
    id (int(11))
    nombre (varchar(255))
    email (varchar(255))
    telefono (varchar(20))
    activo (tinyint(1))
    fecha_creacion (datetime)
```

### 2.2 Operaciones CRUD con DAO

**Crear un usuario:**
```java
import com.pqrs.dao.FundUsuarioDAO;

FundUsuarioDAO.crear(
  "Juan Pérez",
  "juan.perez@company.com",
  "3105551111"
);
```

**Obtener todos:**
```java
FundUsuarioDAO.obtenerTodos();
// Imprime en consola todos los usuarios
```

**Obtener por ID:**
```java
FundUsuarioDAO.obtenerPorId(1);
```

**Actualizar:**
```java
FundUsuarioDAO.actualizar(
  1,
  "Juan Carlos Pérez",
  "juancarlos@company.com",
  "3105552222"
);
```

**Eliminar:**
```java
FundUsuarioDAO.eliminar(1);
```

---

## 3. Usando Postman (Recomendado para pruebas)

### 3.1 Importar en Postman

1. Crear nueva Collection: "PQRS API"
2. Crear las siguientes requests:

#### POST - Crear Usuario
```
Method: POST
URL: http://localhost:8080/api/fundusuario
Headers: Content-Type: application/json
Body (raw):
{
  "nombre": "Test User",
  "email": "test@example.com",
  "telefono": "3105551234",
  "activo": true
}
```

#### GET - Obtener Todos
```
Method: GET
URL: http://localhost:8080/api/fundusuario
```

#### GET - Obtener por ID
```
Method: GET
URL: http://localhost:8080/api/fundusuario/1
```

#### GET - Obtener por Email
```
Method: GET
URL: http://localhost:8080/api/fundusuario/email/test@example.com
```

#### PUT - Actualizar
```
Method: PUT
URL: http://localhost:8080/api/fundusuario/1
Headers: Content-Type: application/json
Body (raw):
{
  "nombre": "Updated User",
  "email": "updated@example.com",
  "telefono": "3105559999",
  "activo": true
}
```

#### DELETE - Eliminar
```
Method: DELETE
URL: http://localhost:8080/api/fundusuario/1
```

---

## 4. Códigos de Respuesta HTTP

| Código | Descripción | Ejemplo |
|--------|-------------|---------|
| 200 OK | Operación exitosa | GET, PUT |
| 201 Created | Usuario creado | POST |
| 204 No Content | Usuario eliminado | DELETE |
| 400 Bad Request | Error en los datos | Request inválido |
| 404 Not Found | Usuario no existe | GET con ID inválido |
| 500 Internal Server Error | Error en el servidor | Problema de BD |

---

## 5. Notas Importantes

✓ MariaDB debe estar corriendo: `localhost:3306`
✓ Base de datos `bdpqrsej` debe existir
✓ Tabla `fundusuario` debe existir
✓ Credenciales: user=`root`, password=`JacMar1953`
✓ La aplicación se inicia en puerto `8080` por defecto
✓ Todos los campos (nombre, email, telefono) son requeridos para CREATE
✓ El campo `id` se genera automáticamente
✓ El campo `fechaCreacion` se genera automáticamente
✓ El campo `activo` por defecto es `true`
