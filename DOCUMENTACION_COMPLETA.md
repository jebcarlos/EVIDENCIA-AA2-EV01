# DOCUMENTACIÓN COMPLETA - CRUD FUNDUSUARIO (JDBC + Spring Boot Web)

## Tabla de Contenidos
1. [Arquitectura General](#arquitectura-general)
2. [Dos Interfaces Disponibles](#dos-interfaces-disponibles)
3. [Interfaz Web (Spring Boot)](#interfaz-web-spring-boot)
4. [Interfaz Consola (JDBC Puro)](#interfaz-consola-jdbc-puro)
5. [Clase ConexionJDBC](#clase-conexionjdbc)
6. [Clase FundUsuarioDAO](#clase-fundusuariodao)
7. [Clase Principal](#clase-principal)
8. [Sentencias SQL](#sentencias-sql)
9. [Flujo de Ejecución](#flujo-de-ejecución)
10. [Variables y Librerías](#variables-y-librerías)

---

## Arquitectura General

### OPCIÓN 1: Interfaz Web (Spring Boot) - RECOMENDADA
```
ESTRUCTURA DEL PROYECTO:
┌──────────────────────────────────────────────────────────────┐
│         NAVEGADOR WEB (http://localhost:8080)              │
│    (HTML/CSS/JavaScript - SIN dependencias externas)       │
└───────────────────────┬──────────────────────────────────────┘
                        │ FETCH API (AJAX)
                        ↓
┌──────────────────────────────────────────────────────────────┐
│           UsuarioRestController.java                        │
│          (6 ENDPOINTS REST - Spring Web)                   │
│  GET, POST, PUT, DELETE con Validación                     │
└───────────────────────┬──────────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┐
        ↓               ↓               ↓
┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│ CreateUserDTO│ │ Response DTO │ │ ApiResponseDTO│
│  (Request)   │ │  (Response)  │ │  (Envelope)  │
└──────────────┘ └──────────────┘ └──────────────┘
        │               │               │
        └───────────────┼───────────────┘
                        ↓
        ┌──────────────────────────────┐
        │  FundUsuarioDAO.java         │
        │  (CRUD + Métodos JSON)       │
        │  - crear()                   │
        │  - obtenerTodosJSON()        │
        │  - obtenerPorIdJSON()        │
        │  - obtenerPorIdentificacionJSON()
        │  - actualizar()              │
        │  - eliminar()                │
        └───────────────┬──────────────┘
                        ↓
        ┌──────────────────────────────┐
        │    ConexionJDBC.java         │
        │  (JDBC - Gestión Conexiones) │
        └───────────────┬──────────────┘
                        ↓
        ┌──────────────────────────────┐
        │    MARIADB (bdpqrsej)        │
        │  Tabla: FUNDUSUARIO          │
        └──────────────────────────────┘
```

**Flujo Web:**
Navegador → Fetch API → UsuarioRestController → DTOs → FundUsuarioDAO → ConexionJDBC → MariaDB

### OPCIÓN 2: Interfaz Consola (JDBC Puro) - Original
```
┌──────────────────────────────────────────────────────────────┐
│                      Principal.java                         │
│              (PROGRAMA PRINCIPAL - MENÚ)                   │
└───────────────────────┬──────────────────────────────────────┘
                        ↓
┌──────────────────────────────────────────────────────────────┐
│                    FundUsuarioDAO.java                      │
│         (MÉTODOS CRUD - OPERACIONES DE BD)                │
└───────────────────────┬──────────────────────────────────────┘
                        ↓
┌──────────────────────────────────────────────────────────────┐
│                    ConexionJDBC.java                        │
│     (GESTIÓN DE CONEXIONES A MARIADB)                     │
└───────────────────────┬──────────────────────────────────────┘
                        ↓
        ┌──────────────────────────────┐
        │    MARIADB (bdpqrsej)        │
        │  Tabla: FUNDUSUARIO          │
        └──────────────────────────────┘
```

**Flujo Consola:**
Principal → FundUsuarioDAO → ConexionJDBC → MariaDB

---

## Dos Interfaces Disponibles

### 🌐 Interfaz Web (NUEVA - RECOMENDADA)
- **Tecnología:** Spring Boot + HTML5 + CSS3 + JavaScript ES6
- **Acceso:** `http://localhost:8080`
- **Ejecución:** `ejecutar-web.bat`
- **Características:** Moderno, responsivo, intuitivo, sin dependencias externas en frontend
- **Endpoints:** 6 REST endpoints (GET, POST, PUT, DELETE)

### 🖥️ Interfaz Consola (ORIGINAL)
- **Tecnología:** JDBC puro + menú interactivo
- **Acceso:** Terminal/Consola
- **Ejecución:** `ejecutar.bat`
- **Características:** Ligero, rápido, bajo consumo, sin frameworks

---

## Interfaz Web (Spring Boot)

### UsuarioRestController.java
**Ubicación:** `src/main/java/com/pqrs/controller/UsuarioRestController.java`

#### Propósito
Exponer endpoints REST para operaciones CRUD con validación y respuestas JSON.

#### Endpoints

```java
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioRestController {
    
    // GET /api/usuarios
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<UsuarioResponseDTO>>> obtenerTodos()
    
    // GET /api/usuarios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UsuarioResponseDTO>> obtenerPorId(@PathVariable int id)
    
    // GET /api/usuarios/buscar/identificacion/{id}
    @GetMapping("/buscar/identificacion/{identificacion}")
    public ResponseEntity<ApiResponseDTO<List<UsuarioResponseDTO>>> obtenerPorIdentificacion()
    
    // POST /api/usuarios
    @PostMapping
    public ResponseEntity<ApiResponseDTO<String>> crear(@RequestBody CreateUserDTO dto)
    
    // PUT /api/usuarios/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<String>> actualizar(@PathVariable int id, @RequestBody CreateUserDTO dto)
    
    // DELETE /api/usuarios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<String>> eliminar(@PathVariable int id)
}
```

#### Validaciones
- TPD requerido
- Identificación no vacía
- Apellido no vacío
- Nombre no vacío
- Dígito verificación (opcional)
- Fecha de nacimiento formato yyyy-MM-dd

#### Respuestas
```json
// Éxito (200)
{
  "success": true,
  "message": "Usuarios obtenidos correctamente",
  "data": [ ... ]
}

// Error (400/404/500)
{
  "success": false,
  "message": "Error al obtener usuarios"
}
```

### DTOs (Data Transfer Objects)

#### CreateUserDTO
**Ubicación:** `src/main/java/com/pqrs/dto/CreateUserDTO.java`

Usado para solicitudes de creación/actualización:
```java
- tpd: int
- identificacion: String
- dv: Integer (nullable)
- primerApellido: String
- segundoApellido: String (nullable)
- primerNombre: String
- segundoNombre: String (nullable)
- fechaNacimiento: LocalDate (nullable)
- sexo: String (nullable)
- tipoSangre: Integer (nullable)
```

#### UsuarioResponseDTO
**Ubicación:** `src/main/java/com/pqrs/dto/UsuarioResponseDTO.java`

Usado para respuestas de lectura:
```java
- usuConsecutivo: int
- tpd: int
- identificacion: String
- dv: Integer (nullable)
- primerApellido: String
- segundoApellido: String (nullable)
- primerNombre: String
- segundoNombre: String (nullable)
- fechaNacimiento: LocalDate (nullable)
- sexo: String (nullable)
- tipoSangre: Integer (nullable)
```

#### ApiResponseDTO<T>
**Ubicación:** `src/main/java/com/pqrs/dto/ApiResponseDTO.java`

Envoltorio genérico para todas las respuestas:
```java
- success: boolean
- message: String
- data: T (genérico)
- errors: List<String> (nullable)
```

### Frontend

#### index.html
**Ubicación:** `src/main/resources/static/index.html`

Componentes principales:
- Header con título
- Toolbar con búsqueda y botón crear
- Tabla de usuarios (responsive)
- Modal para crear/editar
- Modal para confirmación de eliminación
- Contenedor de alertas

#### style.css
**Ubicación:** `src/main/resources/static/css/style.css`

Características:
- Diseño responsivo con media queries
- Gradientes modernos (púrpura/azul)
- Animaciones suaves (0.3s)
- Tabla con hover effects
- Modales elegantes
- Botones interactivos
- Alertas visuales (éxito, error, warning, info)
- Mobile-first approach

#### main.js
**Ubicación:** `src/main/resources/static/js/main.js`

Funciones principales:
- `loadUsuarios()` - Carga desde API
- `renderTable(usuarios)` - Renderiza tabla
- `searchByIdentificacion()` - Búsqueda
- `openCreateModal()` - Modal crear
- `editUsuario(id)` - Modal editar
- `deleteUsuario(id)` - Eliminación
- `handleFormSubmit(e)` - Envío de formulario
- `showAlert(message, type)` - Notificaciones

Usa Fetch API (sin jQuery ni dependencias externas).

---

## Interfaz Consola (JDBC Puro)

---

## CLASE ConexionJDBC

**Archivo:** `src/main/java/com/pqrs/util/ConexionJDBC.java`

### Propósito
Gestionar la conexión a la base de datos MariaDB usando JDBC.

### Librerías Importadas
```java
import java.sql.*;
```
- `java.sql.Connection` - Representa la conexión a la BD
- `java.sql.DriverManager` - Carga el driver y obtiene conexiones
- `java.sql.SQLException` - Maneja excepciones de BD

### Variables Estáticas (Constantes)
```java
private static final String URL = "jdbc:mariadb://localhost:3306/bdpqrsej";
```
- **URL**: Ubicación de la base de datos
  - `jdbc:mariadb://` - Protocolo JDBC para MariaDB
  - `localhost:3306` - Host y puerto (3306 es el puerto por defecto)
  - `bdpqrsej` - Nombre de la base de datos

```java
private static final String USER = "root";
```
- **USER**: Usuario de MariaDB que accede a la base de datos

```java
private static final String PASSWORD = "JacMar1953";
```
- **PASSWORD**: Contraseña del usuario root

```java
private static final String DRIVER = "org.mariadb.jdbc.Driver";
```
- **DRIVER**: Nombre completo de la clase del driver MariaDB JDBC
  - Se usa para cargar dinámicamente el driver
  - Está en el archivo JAR: `lib/mariadb-java-client-3.0.8.jar`

### Bloque Estático (static)
```java
static {
    try {
        Class.forName(DRIVER);
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
}
```
- Se ejecuta UNA SOLA VEZ cuando la clase se carga
- `Class.forName(DRIVER)` - Carga el driver de MariaDB en memoria
- Si el driver no se encuentra, lanza `ClassNotFoundException`

### Métodos

#### 1. obtenerConexion()
```java
public static Connection obtenerConexion() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASSWORD);
}
```
**Parámetros:** Ninguno

**Retorna:** `Connection` - Objeto de conexión a la BD

**Qué hace:**
- Usa `DriverManager.getConnection()` para conectarse a MariaDB
- Envía la URL, usuario y contraseña
- Si la conexión falla, lanza `SQLException`

**Uso en otros archivos:**
```java
Connection conn = ConexionJDBC.obtenerConexion();
```

#### 2. cerrarConexion()
```java
public static void cerrarConexion(Connection conn) {
    try {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```
**Parámetros:** 
- `Connection conn` - La conexión a cerrar

**Retorna:** void (nada)

**Qué hace:**
- Verifica que la conexión no sea nula (`conn != null`)
- Verifica que no esté cerrada (`!conn.isClosed()`)
- Si todo es válido, cierra la conexión con `conn.close()`
- Maneja errores con `try-catch`

#### 3. cerrarRecursos()
```java
public static void cerrarRecursos(ResultSet rs, Statement stmt, Connection conn) {
    try {
        if (rs != null) rs.close();
        if (stmt != null) stmt.close();
        cerrarConexion(conn);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```
**Parámetros:**
- `ResultSet rs` - Los resultados de una consulta
- `Statement stmt` - La sentencia SQL ejecutada
- `Connection conn` - La conexión a la BD

**Retorna:** void (nada)

**Qué hace:**
- Cierra los resultados (`rs.close()`)
- Cierra la sentencia (`stmt.close()`)
- Cierra la conexión llamando a `cerrarConexion()`
- Limpia todos los recursos para evitar fugas de memoria

---

## CLASE FundUsuarioDAO

**Archivo:** `src/main/java/com/pqrs/dao/FundUsuarioDAO.java`

### Propósito
Implementar todas las operaciones CRUD (Create, Read, Update, Delete) en la tabla `fundusuario`.

### Librerías Importadas
```java
import com.pqrs.util.ConexionJDBC;
import java.sql.*;
```
- `ConexionJDBC` - Clase personalizada para obtener conexiones
- `java.sql.Connection` - Conexión a la BD
- `java.sql.PreparedStatement` - Ejecuta sentencias SQL parametrizadas
- `java.sql.Statement` - Ejecuta sentencias SQL simples
- `java.sql.ResultSet` - Contiene resultados de una consulta
- `java.sql.SQLException` - Excepciones de base de datos

### MÉTODO 1: crear()

**Firma del método:**
```java
public static boolean crear(int tpd, String identificacion, Integer dv, 
                            String primerApellido, String segundoApellido, 
                            String primerNombre, String segundoNombre)
```

**Parámetros:**
| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `tpd` | int | Tipo de documento (1=CC, 2=CE, etc) |
| `identificacion` | String | Número de identificación |
| `dv` | Integer | Dígito de verificación (puede ser nulo) |
| `primerApellido` | String | Primer apellido (obligatorio) |
| `segundoApellido` | String | Segundo apellido (opcional) |
| `primerNombre` | String | Primer nombre (obligatorio) |
| `segundoNombre` | String | Segundo nombre (opcional) |

**Retorna:** `boolean` (true si se insertó, false si falló)

**Sentencia SQL:**
```sql
INSERT INTO fundusuario (TPD, IDENTIFICACION, DV, PRIMERAPELLIDO, SEGUNDOAPELLIDO, 
                        PRIMERNOMBRE, SEGUNDONOMBRE) 
VALUES (?, ?, ?, ?, ?, ?, ?)
```

**Explicación de la sentencia:**
- `INSERT INTO fundusuario` - Inserta una nueva fila
- `(TPD, IDENTIFICACION, ...)` - Especifica las columnas a llenar
- `VALUES (?, ?, ...)` - Los `?` son placeholders que se reemplazarán con los valores

**Código paso a paso:**
```java
String sql = "INSERT INTO fundusuario (TPD, IDENTIFICACION, DV, PRIMERAPELLIDO, SEGUNDOAPELLIDO, " +
             "PRIMERNOMBRE, SEGUNDONOMBRE) VALUES (?, ?, ?, ?, ?, ?, ?)";
```
Define la sentencia SQL

```java
try (Connection conn = ConexionJDBC.obtenerConexion();
     PreparedStatement pstmt = conn.prepareStatement(sql)) {
```
- `try-with-resources` - Cierra automáticamente conexión y statement
- Obtiene conexión de ConexionJDBC
- Prepara la sentencia SQL (parametrizada para seguridad)

```java
pstmt.setInt(1, tpd);
pstmt.setString(2, identificacion);
pstmt.setObject(3, dv);
pstmt.setString(4, primerApellido);
pstmt.setString(5, segundoApellido);
pstmt.setString(6, primerNombre);
pstmt.setString(7, segundoNombre);
```
Remplaza los placeholders `?` con valores:
- `setInt(1, tpd)` - Posición 1 es INT
- `setString(2, identificacion)` - Posición 2 es STRING
- `setObject(3, dv)` - Posición 3 puede ser nula
- Etc.

```java
int filasAfectadas = pstmt.executeUpdate();
return filasAfectadas > 0;
```
- `executeUpdate()` - Ejecuta INSERT/UPDATE/DELETE
- Retorna número de filas afectadas
- Si > 0, significa que se insertó correctamente

### MÉTODO 2: obtenerTodos()

**Firma del método:**
```java
public static void obtenerTodos()
```

**Parámetros:** Ninguno

**Retorna:** void (imprime en consola)

**Sentencia SQL:**
```sql
SELECT USUCONSECUTIVO, IDENTIFICACION, PRIMERAPELLIDO, SEGUNDOAPELLIDO, 
       PRIMERNOMBRE, SEGUNDONOMBRE, SEXO 
FROM fundusuario
```

**Código paso a paso:**
```java
try (Connection conn = ConexionJDBC.obtenerConexion();
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery(sql)) {
```
- Obtiene conexión
- `createStatement()` - Crea una sentencia simple (no parametrizada)
- `executeQuery()` - Ejecuta SELECT y retorna ResultSet

```java
System.out.println("\n=== Todos los Usuarios ===");
while (rs.next()) {
```
- `rs.next()` - Se mueve a la siguiente fila
- El bucle continúa mientras haya filas

```java
System.out.println("ID: " + rs.getInt("USUCONSECUTIVO") + 
                 " | Cédula: " + rs.getString("IDENTIFICACION") + 
                 " | Nombre: " + rs.getString("PRIMERNOMBRE") + " " + 
                 (rs.getString("SEGUNDONOMBRE") != null ? rs.getString("SEGUNDONOMBRE") : "") +
                 " | Apellido: " + rs.getString("PRIMERAPELLIDO") + " " +
                 (rs.getString("SEGUNDOAPELLIDO") != null ? rs.getString("SEGUNDOAPELLIDO") : ""));
```
- Obtiene valores de cada columna
- `rs.getInt("USUCONSECUTIVO")` - Obtiene INT
- `rs.getString("IDENTIFICACION")` - Obtiene STRING
- `? : ""` - Si es nulo, imprime vacío

### MÉTODO 3: obtenerPorId()

**Firma del método:**
```java
public static void obtenerPorId(int usuConsecutivo)
```

**Parámetros:**
| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `usuConsecutivo` | int | ID del usuario (USUCONSECUTIVO) |

**Retorna:** void (imprime en consola)

**Sentencia SQL:**
```sql
SELECT * FROM fundusuario 
WHERE USUCONSECUTIVO = ?
```

**Explicación:**
- `SELECT *` - Obtiene todas las columnas
- `WHERE USUCONSECUTIVO = ?` - Filtra por el ID

**Código:**
```java
pstmt.setInt(1, usuConsecutivo);
ResultSet rs = pstmt.executeQuery();

if (rs.next()) {
    // Imprime datos del usuario
} else {
    System.out.println("Usuario no encontrado");
}
```

### MÉTODO 4: obtenerPorIdentificacion()

**Firma del método:**
```java
public static void obtenerPorIdentificacion(String identificacion)
```

**Parámetros:**
| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `identificacion` | String | Número de cédula/identificación |

**Retorna:** void (imprime en consola)

**Sentencia SQL:**
```sql
SELECT * FROM fundusuario 
WHERE IDENTIFICACION = ?
```

### MÉTODO 5: actualizar()

**Firma del método:**
```java
public static boolean actualizar(int usuConsecutivo, String primerApellido, 
                                 String segundoApellido, String primerNombre, 
                                 String segundoNombre, String sexo)
```

**Parámetros:**
| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `usuConsecutivo` | int | ID del usuario a actualizar |
| `primerApellido` | String | Nuevo primer apellido |
| `segundoApellido` | String | Nuevo segundo apellido |
| `primerNombre` | String | Nuevo primer nombre |
| `segundoNombre` | String | Nuevo segundo nombre |
| `sexo` | String | Nuevo sexo (M/F/O) |

**Retorna:** `boolean` (true si actualizó, false si falló)

**Sentencia SQL:**
```sql
UPDATE fundusuario 
SET PRIMERAPELLIDO = ?, SEGUNDOAPELLIDO = ?, 
    PRIMERNOMBRE = ?, SEGUNDONOMBRE = ?, SEXO = ? 
WHERE USUCONSECUTIVO = ?
```

**Explicación:**
- `UPDATE fundusuario` - Modifica la tabla
- `SET columna = ?` - Asigna nuevos valores
- `WHERE USUCONSECUTIVO = ?` - Solo modifica esta fila

### MÉTODO 6: eliminar()

**Firma del método:**
```java
public static boolean eliminar(int usuConsecutivo)
```

**Parámetros:**
| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `usuConsecutivo` | int | ID del usuario a eliminar |

**Retorna:** `boolean` (true si eliminó, false si falló)

**Sentencia SQL:**
```sql
DELETE FROM fundusuario 
WHERE USUCONSECUTIVO = ?
```

**Explicación:**
- `DELETE FROM fundusuario` - Elimina filas
- `WHERE USUCONSECUTIVO = ?` - Solo elimina esta fila

---

## CLASE Principal

**Archivo:** `src/main/java/com/pqrs/Principal.java`

### Propósito
Programa principal que presenta un menú interactivo al usuario.

### Librerías Importadas
```java
import com.pqrs.dao.FundUsuarioDAO;
import java.util.Scanner;
```
- `FundUsuarioDAO` - Clase con métodos CRUD
- `java.util.Scanner` - Lee entrada del usuario

### Variables Globales
```java
private static Scanner scanner = new Scanner(System.in);
```
- **scanner** - Lee texto que escribe el usuario en la consola
- `System.in` - Entrada estándar (teclado)

### MÉTODO 1: main()

**Firma:**
```java
public static void main(String[] args)
```

**Propósito:** Punto de entrada del programa

**Código:**
```java
boolean salir = false;

while (!salir) {
    mostrarMenu();
    int opcion = obtenerOpcion();
    
    switch (opcion) {
        case 1: crear(); break;
        case 2: obtenerTodos(); break;
        case 3: obtenerPorId(); break;
        case 4: obtenerPorIdentificacion(); break;
        case 5: actualizar(); break;
        case 6: eliminar(); break;
        case 7: salir = true; break;
    }
}
scanner.close();
```

**Explicación:**
- `while (!salir)` - Bucle infinito hasta que salir = true
- `mostrarMenu()` - Imprime opciones
- `obtenerOpcion()` - Lee lo que escribió el usuario
- `switch` - Ejecuta la opción seleccionada
- `scanner.close()` - Cierra el lector de entrada

### MÉTODO 2: mostrarMenu()

Imprime el menú en la consola con 7 opciones.

### MÉTODO 3: obtenerOpcion()

```java
private static int obtenerOpcion() {
    try {
        return Integer.parseInt(scanner.nextLine());
    } catch (NumberFormatException e) {
        return -1;
    }
}
```

**Qué hace:**
- Lee una línea del usuario
- `Integer.parseInt()` - Convierte texto a número
- Si no es número válido, retorna -1

### MÉTODO 4: crear()

```java
private static void crear() {
    System.out.println("\n--- CREAR NUEVO USUARIO ---");
    
    System.out.print("Tipo de Documento (TPD): ");
    int tpd = Integer.parseInt(scanner.nextLine());
    
    System.out.print("Identificación: ");
    String identificacion = scanner.nextLine().trim();
    if (identificacion.isEmpty()) {
        System.out.println("✗ La identificación no puede estar vacía");
        return;
    }
    
    // ... más campos ...
    
    if (FundUsuarioDAO.crear(tpd, identificacion, dv, primerApellido, 
                            segundoApellido, primerNombre, segundoNombre)) {
        System.out.println("✓ Usuario creado exitosamente");
    } else {
        System.out.println("✗ Error al crear el usuario");
    }
}
```

**Pasos:**
1. Pide al usuario cada dato requerido
2. Valida que no estén vacíos
3. Llama a `FundUsuarioDAO.crear()`
4. Imprime resultado

### MÉTODO 5: obtenerTodos()

```java
private static void obtenerTodos() {
    System.out.println("\n--- OBTENER TODOS LOS USUARIOS ---");
    FundUsuarioDAO.obtenerTodos();
}
```
Solo llama al método del DAO.

### MÉTODO 6: obtenerPorId()

```java
private static void obtenerPorId() {
    System.out.println("\n--- OBTENER USUARIO POR ID ---");
    System.out.print("ID del usuario: ");
    try {
        int id = Integer.parseInt(scanner.nextLine());
        FundUsuarioDAO.obtenerPorId(id);
    } catch (NumberFormatException e) {
        System.out.println("✗ ID no válido");
    }
}
```

**Pasos:**
1. Pide el ID
2. Convierte a entero
3. Llama al método del DAO
4. Si hay error de formato, imprime mensaje

### MÉTODO 7: obtenerPorIdentificacion()

Igual a obtenerPorId() pero busca por cédula.

### MÉTODO 8: actualizar()

```java
private static void actualizar() {
    System.out.println("\n--- ACTUALIZAR USUARIO ---");
    System.out.print("ID del usuario a actualizar: ");
    int usuConsecutivo = Integer.parseInt(scanner.nextLine());
    
    // Pide nuevos datos
    System.out.print("Nuevo primer apellido: ");
    String primerApellido = scanner.nextLine().trim();
    // ... más campos ...
    
    if (FundUsuarioDAO.actualizar(usuConsecutivo, primerApellido, 
                                  segundoApellido, primerNombre, 
                                  segundoNombre, sexo)) {
        System.out.println("✓ Usuario actualizado exitosamente");
    }
}
```

### MÉTODO 9: eliminar()

```java
private static void eliminar() {
    System.out.println("\n--- ELIMINAR USUARIO ---");
    System.out.print("ID del usuario a eliminar: ");
    int usuConsecutivo = Integer.parseInt(scanner.nextLine());
    
    System.out.print("¿Estás seguro? (s/n): ");
    String confirmacion = scanner.nextLine().trim().toLowerCase();
    
    if (confirmacion.equals("s")) {
        if (FundUsuarioDAO.eliminar(usuConsecutivo)) {
            System.out.println("✓ Usuario eliminado exitosamente");
        }
    }
}
```

**Seguridad:** Pide confirmación antes de eliminar.

---

## Sentencias SQL

### TABLA FUNDUSUARIO
```sql
CREATE TABLE fundusuario (
    USUCONSECUTIVO INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    TPD INT(11) NOT NULL DEFAULT '0',
    IDENTIFICACION VARCHAR(20) NOT NULL,
    DV INT(11) NULL DEFAULT NULL,
    PRIMERAPELLIDO VARCHAR(100) NOT NULL,
    SEGUNDOAPELLIDO VARCHAR(100) NULL,
    PRIMERNOMBRE VARCHAR(100) NOT NULL,
    SEGUNDONOMBRE VARCHAR(100) NULL,
    FECHANACIMIENTO DATE NULL,
    SEXO VARCHAR(3) NULL,
    ... (más campos)
)
```

### SENTENCIAS USADAS EN EL CRUD

**1. INSERT (Crear):**
```sql
INSERT INTO fundusuario (TPD, IDENTIFICACION, DV, PRIMERAPELLIDO, 
                        SEGUNDOAPELLIDO, PRIMERNOMBRE, SEGUNDONOMBRE) 
VALUES (?, ?, ?, ?, ?, ?, ?)
```

**2. SELECT Todos:**
```sql
SELECT USUCONSECUTIVO, IDENTIFICACION, PRIMERAPELLIDO, SEGUNDOAPELLIDO, 
       PRIMERNOMBRE, SEGUNDONOMBRE, SEXO 
FROM fundusuario
```

**3. SELECT por ID:**
```sql
SELECT * FROM fundusuario 
WHERE USUCONSECUTIVO = ?
```

**4. SELECT por Identificación:**
```sql
SELECT * FROM fundusuario 
WHERE IDENTIFICACION = ?
```

**5. UPDATE:**
```sql
UPDATE fundusuario 
SET PRIMERAPELLIDO = ?, SEGUNDOAPELLIDO = ?, 
    PRIMERNOMBRE = ?, SEGUNDONOMBRE = ?, SEXO = ? 
WHERE USUCONSECUTIVO = ?
```

**6. DELETE:**
```sql
DELETE FROM fundusuario 
WHERE USUCONSECUTIVO = ?
```

---

## Flujo de Ejecución Completo

### Ejemplo: Crear un usuario

```
1. Usuario ejecuta: java com.pqrs.Principal
   ↓
2. Se ejecuta main()
   ↓
3. Se muestra el menú
   ↓
4. Usuario selecciona opción 1 (Crear)
   ↓
5. Se llama a Principal.crear()
   ↓
6. Pide datos al usuario
   ↓
7. Usuario ingresa: TPD=1, Identificación=12345678, Nombre=Juan
   ↓
8. Principal.crear() llama a FundUsuarioDAO.crear(1, "12345678", null, "Pérez", null, "Juan", null)
   ↓
9. FundUsuarioDAO.crear():
   - Obtiene conexión: ConexionJDBC.obtenerConexion()
   - Prepara sentencia: "INSERT INTO fundusuario (...) VALUES (?, ?, ...)"
   - Establece parámetros: pstmt.setInt(1, 1), pstmt.setString(2, "12345678"), etc.
   - Ejecuta: pstmt.executeUpdate()
   ↓
10. ConexionJDBC.obtenerConexion():
    - Carga driver: Class.forName("org.mariadb.jdbc.Driver")
    - Conecta a MariaDB: DriverManager.getConnection(URL, USER, PASSWORD)
    ↓
11. MariaDB:
    - Autentica usuario root con contraseña
    - Ejecuta INSERT en tabla fundusuario
    ↓
12. Retorna número de filas afectadas (1)
    ↓
13. FundUsuarioDAO.crear() retorna true
    ↓
14. Principal.crear() imprime: "✓ Usuario creado exitosamente"
    ↓
15. Vuelve al menú
```

---

## Variables y Librerías

### Librerías Java Estándar

| Librería | Clase | Uso |
|----------|-------|-----|
| `java.sql` | `Connection` | Conexión a BD |
| `java.sql` | `DriverManager` | Carga driver y obtiene conexiones |
| `java.sql` | `PreparedStatement` | Ejecuta sentencias parametrizadas |
| `java.sql` | `Statement` | Ejecuta sentencias SQL |
| `java.sql` | `ResultSet` | Contiene resultados de SELECT |
| `java.sql` | `SQLException` | Excepciones de BD |
| `java.util` | `Scanner` | Lee entrada del usuario |

### Driver JDBC Externo

| Archivo | Descripción |
|---------|-------------|
| `lib/mariadb-java-client-3.0.8.jar` | Driver JDBC de MariaDB |

### Variables por Clase

**ConexionJDBC.java:**
- `URL` - Cadena de conexión
- `USER` - Usuario de BD
- `PASSWORD` - Contraseña
- `DRIVER` - Clase del driver

**FundUsuarioDAO.java:**
- `conn` - Conexión a BD
- `stmt` - Sentencia SQL
- `pstmt` - Sentencia parametrizada
- `rs` - Resultados de consulta
- `sql` - Cadena con sentencia SQL
- `filasAfectadas` - Número de filas modificadas

**Principal.java:**
- `scanner` - Lector de entrada
- `salir` - Bandera para salir del programa
- `opcion` - Opción seleccionada por usuario
- `tpd`, `identificacion`, `dv`, etc. - Datos del usuario

---

## Tipos de Datos

| Tipo Java | Tipo SQL | Método JDBC |
|-----------|----------|------------|
| `int` | INT | `setInt()` / `getInt()` |
| `String` | VARCHAR | `setString()` / `getString()` |
| `boolean` | BOOLEAN | `setBoolean()` / `getBoolean()` |
| `Date` | DATE | `setDate()` / `getDate()` |
| `null` | NULL | `setObject()` / `getObject()` |

---

## Resumen de Operaciones

| Operación | Clase | Método | SQL | Retorna |
|-----------|-------|--------|-----|---------|
| **CREATE** | FundUsuarioDAO | `crear()` | INSERT | boolean |
| **READ (todos)** | FundUsuarioDAO | `obtenerTodos()` | SELECT * | void (imprime) |
| **READ (ID)** | FundUsuarioDAO | `obtenerPorId()` | SELECT WHERE | void (imprime) |
| **READ (Cédula)** | FundUsuarioDAO | `obtenerPorIdentificacion()` | SELECT WHERE | void (imprime) |
| **UPDATE** | FundUsuarioDAO | `actualizar()` | UPDATE | boolean |
| **DELETE** | FundUsuarioDAO | `eliminar()` | DELETE | boolean |

