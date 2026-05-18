# DOCUMENTACIÓN COMPLETA - CRUD FUNDUSUARIO (JDBC + Spring Boot Web)

## Tabla de Contenidos
1. [Arquitectura General](#arquitectura-general)
2. [Clase PqrsApplication](#clase-pqrsapplication)
3. [Clase UsuarioRestController](#clase-usuariorestcontroller)
4. [DTOs (Data Transfer Objects)](#dtos-data-transfer-objects)
5. [Clase FundUsuarioDAO](#clase-fundusuariodao)
6. [Clase ConexionJDBC](#clase-conexionjdbc)
7. [Clase Principal (Consola)](#clase-principal-consola)
8. [Frontend Web](#frontend-web)
9. [Sentencias SQL](#sentencias-sql)
10. [Variables y Librerías](#variables-y-librerías)
11. [Flujo de Ejecución](#flujo-de-ejecución)

---

## Arquitectura General

El proyecto tiene **DOS interfaces** que comparten la misma capa de datos:

### 🌐 Interfaz Web (Spring Boot) — RECOMENDADA
```
Navegador (http://localhost:8080)
    ↓ Fetch API (AJAX)
UsuarioRestController.java (6 endpoints REST)
    ↓
FundUsuarioDAO.java (métodos JSON)
    ↓
ConexionJDBC.java
    ↓
MariaDB (bdpqrsej / fundusuario)
```

### 🖥️ Interfaz Consola (JDBC Puro)
```
Principal.java (menú interactivo)
    ↓
FundUsuarioDAO.java (métodos consola + CRUD)
    ↓
ConexionJDBC.java
    ↓
MariaDB (bdpqrsej / fundusuario)
```

---

## Clase PqrsApplication

**Archivo:** `src/main/java/com/pqrs/PqrsApplication.java`

### Propósito
Punto de entrada de la aplicación Spring Boot. Inicia el servidor web embebido (Tomcat).

### Código completo
```java
package com.pqrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PqrsApplication {
    public static void main(String[] args) {
        SpringApplication.run(PqrsApplication.class, args);
    }
}
```

### Anotaciones
| Anotación | Descripción |
|-----------|-------------|
| `@SpringBootApplication` | Habilita auto-configuración, escaneo de componentes y configuración Spring Boot |

### Qué hace
- `SpringApplication.run()` arranca Tomcat en puerto 8080
- Escanea automáticamente todos los beans (`@RestController`, etc.)
- Sirve archivos estáticos desde `src/main/resources/static/`
- Expone la página `index.html` como welcome page

---

## Clase UsuarioRestController

**Archivo:** `src/main/java/com/pqrs/controller/UsuarioRestController.java`

### Propósito
Exponer endpoints REST para operaciones CRUD sobre la tabla `fundusuario`, con validaciones y respuestas JSON estructuradas.

### Librerías Importadas
```java
import com.pqrs.dao.FundUsuarioDAO;
import com.pqrs.dto.ApiResponseDTO;
import com.pqrs.dto.CreateUserDTO;
import com.pqrs.dto.UsuarioResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
```

### Anotaciones de Clase
```java
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
```

| Anotación | Descripción |
|-----------|-------------|
| `@RestController` | Indica que es un controlador REST (devuelve JSON) |
| `@RequestMapping("/api/usuarios")` | Prefijo base para todos los endpoints |
| `@CrossOrigin(origins = "*")` | Permite peticiones desde cualquier origen (CORS) |

### Endpoints

#### 1. GET /api/usuarios — Obtener todos
```java
@GetMapping
public ResponseEntity<ApiResponseDTO<List<UsuarioResponseDTO>>> obtenerTodos()
```
- **Parámetros:** Ninguno
- **Retorna:** `200 OK` con lista de usuarios, o `500` en error
- **Llama a:** `FundUsuarioDAO.obtenerTodosJSON()`

#### 2. GET /api/usuarios/{id} — Obtener por ID
```java
@GetMapping("/{id}")
public ResponseEntity<ApiResponseDTO<UsuarioResponseDTO>> obtenerPorId(@PathVariable int id)
```
- **Parámetros:** `id` (int) — USUCONSECUTIVO del usuario
- **Retorna:** `200 OK` con usuario, `404` si no existe, `500` en error
- **Llama a:** `FundUsuarioDAO.obtenerPorIdJSON(id)`

#### 3. GET /api/usuarios/buscar/identificacion/{identificacion} — Buscar por identificación
```java
@GetMapping("/buscar/identificacion/{identificacion}")
public ResponseEntity<ApiResponseDTO<List<UsuarioResponseDTO>>> obtenerPorIdentificacion(
        @PathVariable String identificacion)
```
- **Parámetros:** `identificacion` (String) — Número de cédula
- **Retorna:** `200 OK` con lista, `404` si no hay resultados, `500` en error
- **Llama a:** `FundUsuarioDAO.obtenerPorIdentificacionJSON(identificacion)`

#### 4. POST /api/usuarios — Crear usuario
```java
@PostMapping
public ResponseEntity<ApiResponseDTO<String>> crear(@RequestBody CreateUserDTO dto)
```
- **Parámetros:** `dto` (CreateUserDTO) — Datos del usuario en JSON
- **Validaciones:**
  - `identificacion` no puede ser null o vacía
  - `primerApellido` no puede ser null o vacío
  - `primerNombre` no puede ser null o vacío
- **Retorna:** `201 CREATED` si exitoso, `400 BAD REQUEST` si validación falla, `500` en error
- **Llama a:** `FundUsuarioDAO.crear(tpd, identificacion, dv, primerApellido, segundoApellido, primerNombre, segundoNombre, fechaNacimiento, sexo, tipoSangre)`

#### 5. PUT /api/usuarios/{id} — Actualizar usuario
```java
@PutMapping("/{id}")
public ResponseEntity<ApiResponseDTO<String>> actualizar(
        @PathVariable int id,
        @RequestBody CreateUserDTO dto)
```
- **Parámetros:** `id` (int), `dto` (CreateUserDTO)
- **Mismas validaciones que POST**
- **Retorna:** `200 OK` si exitoso, `400` / `404` / `500` en error
- **Llama a:** `FundUsuarioDAO.actualizar(id, tpd, identificacion, dv, primerApellido, segundoApellido, primerNombre, segundoNombre, fechaNacimiento, sexo, tipoSangre)`

#### 6. DELETE /api/usuarios/{id} — Eliminar usuario
```java
@DeleteMapping("/{id}")
public ResponseEntity<ApiResponseDTO<String>> eliminar(@PathVariable int id)
```
- **Parámetros:** `id` (int) — USUCONSECUTIVO del usuario
- **Retorna:** `200 OK` si eliminado, `404` si no existe, `500` en error
- **Llama a:** `FundUsuarioDAO.eliminar(id)`

### Formato de Respuesta JSON
```json
// Éxito
{
  "success": true,
  "message": "Usuarios obtenidos correctamente",
  "data": [ { "usuConsecutivo": 1, "tpd": 1, ... } ],
  "errors": null
}

// Error
{
  "success": false,
  "message": "La identificación no puede estar vacía",
  "data": null,
  "errors": null
}
```

---

## DTOs (Data Transfer Objects)

### CreateUserDTO
**Archivo:** `src/main/java/com/pqrs/dto/CreateUserDTO.java`

Usado para recibir datos en peticiones POST y PUT.

```java
public class CreateUserDTO {
    @JsonProperty("tpd")              private int tpd;
    @JsonProperty("identificacion")   private String identificacion;
    @JsonProperty("dv")               private Integer dv;           // nullable
    @JsonProperty("primerApellido")   private String primerApellido;
    @JsonProperty("segundoApellido")  private String segundoApellido; // nullable
    @JsonProperty("primerNombre")     private String primerNombre;
    @JsonProperty("segundoNombre")    private String segundoNombre;   // nullable
    @JsonProperty("fechaNacimiento")  private LocalDate fechaNacimiento; // nullable
    @JsonProperty("sexo")             private String sexo;           // nullable
    @JsonProperty("tipoSangre")       private Integer tipoSangre;     // nullable
}
```

**Campos:**
| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| `tpd` | int | Sí | Tipo de documento (1=CC, 2=CE, 3=PAS, 4=NIT) |
| `identificacion` | String | Sí | Número de identificación |
| `dv` | Integer | No | Dígito de verificación |
| `primerApellido` | String | Sí | Primer apellido |
| `segundoApellido` | String | No | Segundo apellido |
| `primerNombre` | String | Sí | Primer nombre |
| `segundoNombre` | String | No | Segundo nombre |
| `fechaNacimiento` | LocalDate | No | Fecha de nacimiento (yyyy-MM-dd) |
| `sexo` | String | No | Sexo (M/F/O) |
| `tipoSangre` | Integer | No | Tipo de sangre (0=O- a 7=AB+) |

**Ejemplo JSON:**
```json
{
  "tpd": 1,
  "identificacion": "12345678",
  "dv": 9,
  "primerApellido": "García",
  "segundoApellido": "López",
  "primerNombre": "Juan",
  "segundoNombre": "Carlos",
  "fechaNacimiento": "1990-05-15",
  "sexo": "M",
  "tipoSangre": 3
}
```

### UsuarioResponseDTO
**Archivo:** `src/main/java/com/pqrs/dto/UsuarioResponseDTO.java`

Usado para enviar datos en respuestas GET.

```java
public class UsuarioResponseDTO {
    @JsonProperty("usuConsecutivo")  private int usuConsecutivo;
    @JsonProperty("tpd")            private int tpd;
    @JsonProperty("identificacion") private String identificacion;
    @JsonProperty("dv")             private Integer dv;
    @JsonProperty("primerApellido") private String primerApellido;
    @JsonProperty("segundoApellido") private String segundoApellido;
    @JsonProperty("primerNombre")   private String primerNombre;
    @JsonProperty("segundoNombre")  private String segundoNombre;
    @JsonProperty("fechaNacimiento") private LocalDate fechaNacimiento;
    @JsonProperty("sexo")           private String sexo;
    @JsonProperty("tipoSangre")     private Integer tipoSangre;
}
```

**Diferencia con CreateUserDTO:** Incluye `usuConsecutivo` (el ID auto-incrementado de la BD).

### ApiResponseDTO\<T\>
**Archivo:** `src/main/java/com/pqrs/dto/ApiResponseDTO.java`

Envoltorio genérico para todas las respuestas de la API.

```java
public class ApiResponseDTO<T> {
    @JsonProperty("success") private boolean success;
    @JsonProperty("message") private String message;
    @JsonProperty("data")    private T data;
    @JsonProperty("errors")  private List<String> errors;
}
```

**Constructores:**
- `ApiResponseDTO(boolean success, String message)` — Solo estado y mensaje
- `ApiResponseDTO(boolean success, String message, T data)` — Con datos
- `ApiResponseDTO(boolean success, String message, List<String> errors)` — Con errores

---

## Clase FundUsuarioDAO

**Archivo:** `src/main/java/com/pqrs/dao/FundUsuarioDAO.java`

### Propósito
Implementar TODAS las operaciones CRUD contra la tabla `fundusuario` usando JDBC puro. Contiene métodos para la consola (imprimen en pantalla) y métodos JSON (retornan DTOs) para la API REST.

### Librerías Importadas
```java
import com.pqrs.util.ConexionJDBC;
import com.pqrs.dto.UsuarioResponseDTO;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
```

### MÉTODO 1: crear() — CREATE

**Firma:**
```java
public static boolean crear(int tpd, String identificacion, Integer dv,
                            String primerApellido, String segundoApellido,
                            String primerNombre, String segundoNombre,
                            LocalDate fechaNacimiento, String sexo, Integer tipoSangre)
```

**Parámetros (10):**
| # | Parámetro | Tipo | Descripción |
|---|-----------|------|-------------|
| 1 | `tpd` | int | Tipo de documento |
| 2 | `identificacion` | String | Número de identificación |
| 3 | `dv` | Integer | Dígito de verificación (nullable) |
| 4 | `primerApellido` | String | Primer apellido |
| 5 | `segundoApellido` | String | Segundo apellido (nullable) |
| 6 | `primerNombre` | String | Primer nombre |
| 7 | `segundoNombre` | String | Segundo nombre (nullable) |
| 8 | `fechaNacimiento` | LocalDate | Fecha de nacimiento (nullable) |
| 9 | `sexo` | String | Sexo M/F/O (nullable) |
| 10 | `tipoSangre` | Integer | Tipo de sangre 0-7 (nullable) |

**Retorna:** `boolean` — true si se insertó al menos una fila

**Sentencia SQL:**
```sql
INSERT INTO fundusuario (TPD, IDENTIFICACION, DV, PRIMERAPELLIDO, SEGUNDOAPELLIDO,
                         PRIMERNOMBRE, SEGUNDONOMBRE, FECHANACIMIENTO, SEXO, TIPOSANGRE)
VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
```

**Código:**
```java
try (Connection conn = ConexionJDBC.obtenerConexion();
     PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.setInt(1, tpd);
    pstmt.setString(2, identificacion);
    pstmt.setObject(3, dv);
    pstmt.setString(4, primerApellido);
    pstmt.setString(5, segundoApellido);
    pstmt.setString(6, primerNombre);
    pstmt.setString(7, segundoNombre);
    pstmt.setObject(8, fechaNacimiento != null ? java.sql.Date.valueOf(fechaNacimiento) : null);
    pstmt.setString(9, sexo);
    pstmt.setObject(10, tipoSangre);
    int filasAfectadas = pstmt.executeUpdate();
    return filasAfectadas > 0;
}
```

**Conversión de fecha:** `java.sql.Date.valueOf(fechaNacimiento)` convierte `LocalDate` a `java.sql.Date` para JDBC.

### MÉTODO 2: obtenerTodos() — READ (Consola)

**Firma:**
```java
public static void obtenerTodos()
```

**Retorna:** void (imprime en consola con formato visual)

**Sentencia SQL:**
```sql
SELECT USUCONSECUTIVO, TPD, IDENTIFICACION, DV, PRIMERAPELLIDO, SEGUNDOAPELLIDO,
       PRIMERNOMBRE, SEGUNDONOMBRE, FECHANACIMIENTO, SEXO, TIPOSANGRE
FROM fundusuario
```

**Formato de salida:** Tabla con bordes ASCII, emojis por categoría, manejo de NULL como "N/A".

### MÉTODO 3: obtenerPorId() — READ por ID (Consola)

**Firma:**
```java
public static void obtenerPorId(int usuConsecutivo)
```

**Parámetros:**
| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `usuConsecutivo` | int | ID del usuario |

**Sentencia SQL:**
```sql
SELECT * FROM fundusuario WHERE USUCONSECUTIVO = ?
```

**Formato de salida:** Detalle completo con 13 campos organizados en categorías:
- 📋 INFORMACIÓN PERSONAL (USUCONSECUTIVO, TPD, IDENTIFICACION, DV)
- 👤 DATOS PERSONALES (Apellidos, Nombres, Fecha Nacimiento, Sexo)
- 🏥 INFORMACIÓN MÉDICA (Tipo Sangre, Altura, Estrato)
- 📍 INFORMACIÓN ADICIONAL (Depto Nacimiento, Municipio, Estado Civil, Educación, Ocupación, EPS, SISBEN)

### MÉTODO 4: obtenerPorIdentificacion() — READ por cédula (Consola)

**Firma:**
```java
public static void obtenerPorIdentificacion(String identificacion)
```

**Sentencia SQL:**
```sql
SELECT * FROM fundusuario WHERE IDENTIFICACION = ?
```

### MÉTODO 5: actualizar() — UPDATE

**Firma:**
```java
public static boolean actualizar(int usuConsecutivo, int tpd, String identificacion,
                                 Integer dv, String primerApellido, String segundoApellido,
                                 String primerNombre, String segundoNombre,
                                 LocalDate fechaNacimiento, String sexo, Integer tipoSangre)
```

**Parámetros (11):** Los 10 campos de datos + `usuConsecutivo` para el WHERE.

**Sentencia SQL:**
```sql
UPDATE fundusuario SET TPD = ?, IDENTIFICACION = ?, DV = ?,
       PRIMERAPELLIDO = ?, SEGUNDOAPELLIDO = ?,
       PRIMERNOMBRE = ?, SEGUNDONOMBRE = ?,
       FECHANACIMIENTO = ?, SEXO = ?, TIPOSANGRE = ?
WHERE USUCONSECUTIVO = ?
```

### MÉTODO 6: eliminar() — DELETE

**Firma:**
```java
public static boolean eliminar(int usuConsecutivo)
```

**Sentencia SQL:**
```sql
DELETE FROM fundusuario WHERE USUCONSECUTIVO = ?
```

### MÉTODOS JSON (para API REST)

#### obtenerTodosJSON()
```java
public static List<UsuarioResponseDTO> obtenerTodosJSON()
```
- **Retorna:** `List<UsuarioResponseDTO>` con todos los usuarios
- **SQL:** `SELECT USUCONSECUTIVO, TPD, IDENTIFICACION, DV, PRIMERAPELLIDO, SEGUNDOAPELLIDO, PRIMERNOMBRE, SEGUNDONOMBRE, FECHANACIMIENTO, SEXO, TIPOSANGRE FROM fundusuario`
- **Conversión:** `rs.getDate("FECHANACIMIENTO").toLocalDate()` para fechas

#### obtenerPorIdJSON(int usuConsecutivo)
```java
public static UsuarioResponseDTO obtenerPorIdJSON(int usuConsecutivo)
```
- **Retorna:** `UsuarioResponseDTO` o `null` si no existe
- **SQL:** `SELECT * FROM fundusuario WHERE USUCONSECUTIVO = ?`

#### obtenerPorIdentificacionJSON(String identificacion)
```java
public static List<UsuarioResponseDTO> obtenerPorIdentificacionJSON(String identificacion)
```
- **Retorna:** `List<UsuarioResponseDTO>` (puede estar vacía)
- **SQL:** `SELECT * FROM fundusuario WHERE IDENTIFICACION = ?`

---

## Clase ConexionJDBC

**Archivo:** `src/main/java/com/pqrs/util/ConexionJDBC.java`

### Propósito
Gestionar la conexión a MariaDB usando JDBC. Carga el driver una sola vez (bloque static) y expone métodos para obtener/cerrar conexiones.

### Librerías Importadas
```java
import java.sql.*;
```

### Variables Estáticas (Constantes)
```java
private static final String URL      = "jdbc:mariadb://localhost:3306/bdpqrsej";
private static final String USER     = "root";
private static final String PASSWORD = "JacMar1953";
private static final String DRIVER   = "org.mariadb.jdbc.Driver";
```

| Constante | Valor | Descripción |
|-----------|-------|-------------|
| `URL` | `jdbc:mariadb://localhost:3306/bdpqrsej` | Cadena de conexión JDBC |
| `USER` | `root` | Usuario de MariaDB |
| `PASSWORD` | `JacMar1953` | Contraseña de MariaDB |
| `DRIVER` | `org.mariadb.jdbc.Driver` | Clase del driver JDBC |

### Bloque Estático
```java
static {
    try {
        Class.forName(DRIVER);
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
}
```
- Se ejecuta UNA SOLA VEZ al cargar la clase
- `Class.forName()` registra el driver JDBC en el `DriverManager`

### Métodos

#### obtenerConexion()
```java
public static Connection obtenerConexion() throws SQLException
```
- **Retorna:** `Connection` — Conexión activa a MariaDB
- **Implementación:** `DriverManager.getConnection(URL, USER, PASSWORD)`

#### cerrarConexion(Connection conn)
```java
public static void cerrarConexion(Connection conn)
```
- **Parámetros:** `conn` — Conexión a cerrar
- **Verifica:** `conn != null && !conn.isClosed()` antes de cerrar

#### cerrarRecursos(ResultSet rs, Statement stmt, Connection conn)
```java
public static void cerrarRecursos(ResultSet rs, Statement stmt, Connection conn)
```
- Cierra ResultSet → Statement → Connection en orden

---

## Clase Principal (Consola)

**Archivo:** `src/main/java/com/pqrs/Principal.java`

### Propósito
Programa de consola con menú interactivo para operaciones CRUD.

### Librerías Importadas
```java
import com.pqrs.dao.FundUsuarioDAO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
```

### Variables Globales
```java
private static Scanner scanner = new Scanner(System.in);
private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
```

### Métodos

| Método | Propósito |
|--------|-----------|
| `main(String[] args)` | Punto de entrada, bucle del menú |
| `mostrarMenu()` | Imprime menú con 7 opciones en formato ASCII |
| `obtenerOpcion()` | Lee opción numérica del usuario |
| `crear()` | Pide 10 campos y llama a `FundUsuarioDAO.crear()` |
| `obtenerTodos()` | Llama a `FundUsuarioDAO.obtenerTodos()` |
| `obtenerPorId()` | Pide ID y llama a `FundUsuarioDAO.obtenerPorId()` |
| `obtenerPorIdentificacion()` | Pide cédula y busca |
| `actualizar()` | Pide ID + 10 campos y llama a `FundUsuarioDAO.actualizar()` |
| `eliminar()` | Pide ID + confirmación, llama a `FundUsuarioDAO.eliminar()` |

### Menú
```
╔════════════════════════════════════════════════╗
║     CRUD USUARIOS - TABLA FUNDUSUARIO         ║
╠════════════════════════════════════════════════╣
║ 1. Crear usuario                              ║
║ 2. Obtener todos los usuarios                 ║
║ 3. Obtener usuario por ID (USUCONSECUTIVO)    ║
║ 4. Obtener usuario por Identificación         ║
║ 5. Actualizar usuario                         ║
║ 6. Eliminar usuario                           ║
║ 7. Salir                                      ║
╚════════════════════════════════════════════════╝
```

---

## Frontend Web

### index.html
**Ubicación:** `src/main/resources/static/index.html`

**Componentes:**
- Header con título "Gestión de Usuarios PQRS"
- Toolbar con campo de búsqueda y botones (🔍 Buscar, 🔄 Resetear, ➕ Nuevo Usuario)
- Tabla de usuarios con columnas: ID, Documento, Identificación, Nombre Completo, Acciones
- Modal para crear/editar usuario con todos los campos del CRUD
- Modal de confirmación de eliminación
- Contenedor de alertas (éxito/error)
- Loading spinner

### style.css
**Ubicación:** `src/main/resources/static/css/style.css`

**Características:**
- Diseño responsivo (funciona en desktop, tablet, mobile)
- Variables CSS para colores
- Gradientes (púrpura/azul)
- Animaciones y transiciones suaves (0.3s)
- Efectos hover en botones y filas de tabla
- Modales con overlay semi-transparente
- Alertas codificadas por color (success, error, warning, info)

### main.js
**Ubicación:** `src/main/resources/static/js/main.js`

**Funciones principales:**
| Función | Descripción |
|---------|-------------|
| `loadUsuarios()` | GET /api/usuarios → renderiza tabla |
| `renderTable(usuarios)` | Construye filas de la tabla HTML |
| `searchByIdentificacion()` | GET /api/usuarios/buscar/identificacion/{id} |
| `resetSearch()` | Vuelve a cargar todos los usuarios |
| `openCreateModal()` | Abre modal en modo creación |
| `openEditModal(id)` | Abre modal en modo edición con datos precargados |
| `deleteUsuario(id)` | DELETE /api/usuarios/{id} con confirmación |
| `handleFormSubmit(e)` | POST o PUT según modo (crear/editar) |
| `showAlert(message, type)` | Muestra notificación visual |
| `closeModal()` / `closeDeleteModal()` | Cierra modales |

**Tecnología:** JavaScript vainilla (ES6), Fetch API, sin dependencias externas.

---

## Sentencias SQL

### Tabla FUNDUSUARIO (estructura usada)
```sql
CREATE TABLE fundusuario (
    USUCONSECUTIVO INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    TPD INT(11) NOT NULL DEFAULT 0,
    IDENTIFICACION VARCHAR(20) NOT NULL,
    DV INT(11) NULL DEFAULT NULL,
    PRIMERAPELLIDO VARCHAR(100) NOT NULL,
    SEGUNDOAPELLIDO VARCHAR(100) NULL,
    PRIMERNOMBRE VARCHAR(100) NOT NULL,
    SEGUNDONOMBRE VARCHAR(100) NULL,
    FECHANACIMIENTO DATE NULL,
    SEXO VARCHAR(3) NULL,
    TIPOSANGRE INT(11) NULL,
    ALTURA VARCHAR(10) NULL,
    ESTRATO INT(11) NULL,
    DEPTONACIMIENTO INT(11) NULL,
    MUNICIPIONACIMIENTO INT(11) NULL,
    ESTADOCIVIL INT(11) NULL,
    EDUCACION INT(11) NULL,
    OCUPACIONID INT(11) NULL,
    EPS INT(11) NULL,
    SISBEN INT(11) NULL
);
```

### Sentencias CRUD

**INSERT (crear):**
```sql
INSERT INTO fundusuario (TPD, IDENTIFICACION, DV, PRIMERAPELLIDO, SEGUNDOAPELLIDO,
                         PRIMERNOMBRE, SEGUNDONOMBRE, FECHANACIMIENTO, SEXO, TIPOSANGRE)
VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
```

**SELECT todos (consola):**
```sql
SELECT USUCONSECUTIVO, TPD, IDENTIFICACION, DV, PRIMERAPELLIDO, SEGUNDOAPELLIDO,
       PRIMERNOMBRE, SEGUNDONOMBRE, FECHANACIMIENTO, SEXO, TIPOSANGRE
FROM fundusuario
```

**SELECT todos (JSON):**
```sql
SELECT USUCONSECUTIVO, TPD, IDENTIFICACION, DV, PRIMERAPELLIDO, SEGUNDOAPELLIDO,
       PRIMERNOMBRE, SEGUNDONOMBRE, FECHANACIMIENTO, SEXO, TIPOSANGRE
FROM fundusuario
```

**SELECT por ID:**
```sql
SELECT * FROM fundusuario WHERE USUCONSECUTIVO = ?
```

**SELECT por Identificación:**
```sql
SELECT * FROM fundusuario WHERE IDENTIFICACION = ?
```

**UPDATE:**
```sql
UPDATE fundusuario SET TPD = ?, IDENTIFICACION = ?, DV = ?,
       PRIMERAPELLIDO = ?, SEGUNDOAPELLIDO = ?,
       PRIMERNOMBRE = ?, SEGUNDONOMBRE = ?,
       FECHANACIMIENTO = ?, SEXO = ?, TIPOSANGRE = ?
WHERE USUCONSECUTIVO = ?
```

**DELETE:**
```sql
DELETE FROM fundusuario WHERE USUCONSECUTIVO = ?
```

---

## Variables y Librerías

### Librerías Java Estándar
| Paquete | Clase | Uso |
|---------|-------|-----|
| `java.sql` | `Connection` | Conexión a BD |
| `java.sql` | `DriverManager` | Obtener conexiones JDBC |
| `java.sql` | `PreparedStatement` | Sentencias SQL parametrizadas (anti-SQL injection) |
| `java.sql` | `Statement` | Sentencias SQL simples |
| `java.sql` | `ResultSet` | Resultados de consultas SELECT |
| `java.sql` | `SQLException` | Excepciones de base de datos |
| `java.sql` | `Date` | Fecha SQL (convertida desde LocalDate) |
| `java.time` | `LocalDate` | Fecha sin hora (API moderna) |
| `java.time.format` | `DateTimeFormatter` | Formateo de fechas (yyyy-MM-dd) |
| `java.util` | `Scanner` | Lectura de entrada del usuario |
| `java.util` | `ArrayList` | Lista dinámica para resultados JSON |
| `java.util` | `List` | Interfaz de lista |

### Librerías Spring Boot
| Paquete | Clase/Anotación | Uso |
|---------|-----------------|-----|
| `org.springframework.boot` | `SpringApplication` | Iniciar aplicación |
| `org.springframework.boot.autoconfigure` | `@SpringBootApplication` | Auto-configuración |
| `org.springframework.web.bind.annotation` | `@RestController` | Controlador REST |
| `org.springframework.web.bind.annotation` | `@RequestMapping` | Ruta base |
| `org.springframework.web.bind.annotation` | `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` | Verbos HTTP |
| `org.springframework.web.bind.annotation` | `@PathVariable` | Parámetro de ruta |
| `org.springframework.web.bind.annotation` | `@RequestBody` | Cuerpo de petición |
| `org.springframework.web.bind.annotation` | `@CrossOrigin` | CORS |
| `org.springframework.http` | `ResponseEntity` | Respuesta HTTP con estado |
| `org.springframework.http` | `HttpStatus` | Códigos de estado HTTP |

### Librerías Jackson (JSON)
| Paquete | Anotación | Uso |
|---------|-----------|-----|
| `com.fasterxml.jackson.annotation` | `@JsonProperty` | Nombre de campo en JSON |

### Dependencias Maven (pom.xml)
| Grupo | Artefacto | Versión | Propósito |
|-------|-----------|---------|-----------|
| `org.springframework.boot` | `spring-boot-starter-web` | 3.4.3 | Spring MVC + Tomcat |
| `org.springframework.boot` | `spring-boot-starter-data-jpa` | 3.4.3 | JPA/Hibernate |
| `org.mariadb.jdbc` | `mariadb-java-client` | 3.0.8 | Driver MariaDB |
| `mysql` | `mysql-connector-java` | 8.0.33 | Driver MySQL |
| `org.projectlombok` | `lombok` | 1.18.34 | Reducir boilerplate |
| `org.springframework.boot` | `spring-boot-starter-test` | 3.4.3 | Testing |

### Variables por Clase

**ConexionJDBC.java:**
- `URL` — `jdbc:mariadb://localhost:3306/bdpqrsej`
- `USER` — `root`
- `PASSWORD` — `JacMar1953`
- `DRIVER` — `org.mariadb.jdbc.Driver`

**FundUsuarioDAO.java:**
- `conn` — Conexión JDBC
- `pstmt` — PreparedStatement (consultas parametrizadas)
- `stmt` — Statement (consultas simples)
- `rs` — ResultSet (resultados)
- `sql` — String con la sentencia SQL
- `filasAfectadas` — int con número de filas modificadas
- `contador` — int para numerar usuarios en consola
- `usuarios` — `List<UsuarioResponseDTO>` para métodos JSON

**Principal.java:**
- `scanner` — `Scanner(System.in)` para leer entrada
- `dateFormatter` — `DateTimeFormatter.ofPattern("yyyy-MM-dd")`
- `salir` — boolean para controlar el bucle del menú
- `opcion` — int con la opción seleccionada

**UsuarioRestController.java:**
- Sin variables de instancia — todos los métodos usan parámetros

---

## Flujo de Ejecución

### Ejemplo Web: Crear usuario desde el navegador

```
1. Usuario abre http://localhost:8080
   ↓
2. Tomcat sirve index.html
   ↓
3. main.js ejecuta loadUsuarios() → GET /api/usuarios
   ↓
4. Usuario hace clic en "➕ Nuevo Usuario"
   ↓
5. openCreateModal() muestra el formulario
   ↓
6. Usuario llena campos y hace clic en "Guardar Usuario"
   ↓
7. handleFormSubmit() envía POST /api/usuarios con JSON:
   {
     "tpd": 1,
     "identificacion": "12345678",
     "primerApellido": "García",
     "primerNombre": "Juan",
     ...
   }
   ↓
8. UsuarioRestController.crear() recibe el DTO
   ↓
9. Valida campos requeridos (identificacion, primerApellido, primerNombre)
   ↓
10. Llama a FundUsuarioDAO.crear(tpd, identificacion, dv, ...)
   ↓
11. FundUsuarioDAO:
    - ConexionJDBC.obtenerConexion() → Connection a MariaDB
    - Prepara INSERT con 10 placeholders
    - Convierte LocalDate a java.sql.Date
    - Ejecuta executeUpdate()
   ↓
12. MariaDB inserta la fila
   ↓
13. Retorna true → 201 CREATED
   ↓
14. main.js recibe respuesta, cierra modal, recarga tabla
   ↓
15. Usuario ve el nuevo registro en la tabla
```

### Ejemplo Consola: Eliminar usuario

```
1. Usuario ejecuta: ejecutar.bat
   ↓
2. Principal.main() inicia el bucle
   ↓
3. Muestra menú, usuario elige opción 6
   ↓
4. Principal.eliminar():
    - Pide ID: 1
    - Pide confirmación: "s"
   ↓
5. FundUsuarioDAO.eliminar(1):
    - ConexionJDBC.obtenerConexion()
    - Prepara: DELETE FROM fundusuario WHERE USUCONSECUTIVO = ?
    - pstmt.setInt(1, 1)
    - executeUpdate() → 1 fila afectada
   ↓
6. Retorna true
   ↓
7. Imprime: "✓ Usuario eliminado exitosamente"
   ↓
8. Vuelve al menú
```

---

**Versión:** 2.0.0  
**Tecnología:** JDBC Puro + Spring Boot 3.4.3 + MariaDB  
**Java:** 21+  
**Última actualización:** 2026-05-17