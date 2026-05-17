# CÓMO EJECUTAR LA APLICACIÓN - CRUD FUNDUSUARIO

## ✓ Requisitos Previos

1. **MariaDB corriendo** en localhost:3306
2. **Base de datos `bdpqrsej`** creada
3. **Tabla `fundusuario`** creada con la estructura correcta
4. **Java JDK 11+** instalado en el sistema
5. **Driver JDBC MariaDB** en la carpeta `lib/mariadb-java-client-3.0.8.jar`

## Opción 1: Ejecutar con BAT (Windows - Más fácil)

```bash
cd e:\SENA\PQRS\PQRS-JAVA
ejecutar.bat
```

**Qué hace:**
- Compila automáticamente todas las clases Java
- Añade el driver JDBC al classpath
- Ejecuta el programa con interfaz de menú interactivo

## Opción 2: Ejecutar con PowerShell

```powershell
cd e:\SENA\PQRS\PQRS-JAVA
.\ejecutar.ps1
```

## Opción 3: Manual paso a paso

### Paso 1: Crear directorio de salida

```powershell
cd e:\SENA\PQRS\PQRS-JAVA
mkdir target\classes -Force
```

### Paso 2: Compilar las clases

**ConexionJDBC.java:**
```powershell
javac -cp "lib\mariadb-java-client-3.0.8.jar" -d target\classes `
  src\main\java\com\pqrs\util\ConexionJDBC.java
```
- `-cp "lib\..."` - Añade el driver JDBC al classpath
- `-d target\classes` - Guarda archivos compilados en target\classes

**FundUsuarioDAO.java:**
```powershell
javac -cp "lib\mariadb-java-client-3.0.8.jar;target\classes" -d target\classes `
  src\main\java\com\pqrs\dao\FundUsuarioDAO.java
```
- Necesita ConexionJDBC, por eso incluye target\classes

**Principal.java:**
```powershell
javac -cp "lib\mariadb-java-client-3.0.8.jar;target\classes" -d target\classes `
  src\main\java\com\pqrs\Principal.java
```

### Paso 3: Ejecutar el programa

```powershell
java -cp "lib\mariadb-java-client-3.0.8.jar;target\classes" com.pqrs.Principal
```

---

## Uso del Programa

Una vez ejecutado, verás este menú:

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
Selecciona una opción:
```

---

## EJEMPLOS REALES DE USO

### OPCIÓN 1: CREAR USUARIO

```
Selecciona una opción: 1

--- CREAR NUEVO USUARIO ---
Tipo de Documento (TPD) [1=CC, 2=CE, etc]: 1
Identificación: 12345678
DV (Dígito de Verificación) [Opcional, presiona Enter para saltar]: 9
Primer Apellido: García
Segundo Apellido [Opcional]: López
Primer Nombre: Juan
Segundo Nombre [Opcional]: Carlos
✓ Usuario creado exitosamente
```

**Qué ocurre internamente:**
1. Principal.crear() recibe los datos
2. Llama a FundUsuarioDAO.crear(1, "12345678", 9, "García", "López", "Juan", "Carlos")
3. FundUsuarioDAO obtiene conexión con ConexionJDBC
4. Ejecuta: `INSERT INTO fundusuario (TPD, IDENTIFICACION, DV, PRIMERAPELLIDO, SEGUNDOAPELLIDO, PRIMERNOMBRE, SEGUNDONOMBRE) VALUES (1, "12345678", 9, "García", "López", "Juan", "Carlos")`
5. MariaDB inserta la fila con USUCONSECUTIVO autoincrementado
6. Retorna true, imprime el mensaje de éxito

---

### OPCIÓN 2: OBTENER TODOS LOS USUARIOS

```
Selecciona una opción: 2

--- OBTENER TODOS LOS USUARIOS ---

=== Todos los Usuarios ===
ID: 1 | Cédula: 12345678 | Nombre: Juan Carlos | Apellido: García López
ID: 2 | Cédula: 87654321 | Nombre: María | Apellido: Rodríguez
ID: 3 | Cédula: 11111111 | Nombre: Pedro | Apellido: Martínez
```

**Qué ocurre internamente:**
1. Principal.obtenerTodos() llama a FundUsuarioDAO.obtenerTodos()
2. FundUsuarioDAO ejecuta: `SELECT USUCONSECUTIVO, IDENTIFICACION, PRIMERAPELLIDO, SEGUNDOAPELLIDO, PRIMERNOMBRE, SEGUNDONOMBRE, SEXO FROM fundusuario`
3. MariaDB retorna todas las filas
4. FundUsuarioDAO itera con rs.next() e imprime cada fila
5. Se cierra ResultSet automáticamente

---

### OPCIÓN 3: OBTENER USUARIO POR ID

```
Selecciona una opción: 3

--- OBTENER USUARIO POR ID (USUCONSECUTIVO) ---
ID del usuario: 1

=== Usuario ID 1 ===
Identificación: 12345678
DV: 9
Nombre: Juan Carlos
Apellido: García López
Sexo: M
Tipo Doc: 1
```

**Qué ocurre internamente:**
1. Principal.obtenerPorId() lee el ID (1)
2. Llama a FundUsuarioDAO.obtenerPorId(1)
3. FundUsuarioDAO ejecuta: `SELECT * FROM fundusuario WHERE USUCONSECUTIVO = 1`
4. Si encuentra la fila (rs.next() == true), imprime los datos
5. Si no encuentra, imprime "Usuario no encontrado"

---

### OPCIÓN 4: OBTENER USUARIO POR IDENTIFICACIÓN

```
Selecciona una opción: 4

--- OBTENER USUARIO POR IDENTIFICACIÓN ---
Número de identificación: 12345678

=== Usuario con Cédula 12345678 ===
ID: 1
Nombre: Juan Carlos
Apellido: García López
```

**Qué ocurre internamente:**
1. Principal.obtenerPorIdentificacion() lee la cédula ("12345678")
2. Llama a FundUsuarioDAO.obtenerPorIdentificacion("12345678")
3. FundUsuarioDAO ejecuta: `SELECT * FROM fundusuario WHERE IDENTIFICACION = "12345678"`
4. Busca por número de cédula en lugar de por ID

---

### OPCIÓN 5: ACTUALIZAR USUARIO

```
Selecciona una opción: 5

--- ACTUALIZAR USUARIO ---
ID del usuario a actualizar (USUCONSECUTIVO): 1
Nuevo primer apellido: García
Nuevo segundo apellido [Opcional]: Martínez
Nuevo primer nombre: Juan
Nuevo segundo nombre [Opcional]: Pedro
Sexo [M/F/O] [Opcional]: M
✓ Usuario actualizado exitosamente
```

**Qué ocurre internamente:**
1. Principal.actualizar() lee el ID (1) y nuevos datos
2. Llama a FundUsuarioDAO.actualizar(1, "García", "Martínez", "Juan", "Pedro", "M")
3. FundUsuarioDAO ejecuta: `UPDATE fundusuario SET PRIMERAPELLIDO = "García", SEGUNDOAPELLIDO = "Martínez", PRIMERNOMBRE = "Juan", SEGUNDONOMBRE = "Pedro", SEXO = "M" WHERE USUCONSECUTIVO = 1`
4. MariaDB modifica la fila
5. Si filasAfectadas > 0, retorna true e imprime éxito

---

### OPCIÓN 6: ELIMINAR USUARIO

```
Selecciona una opción: 6

--- ELIMINAR USUARIO ---
ID del usuario a eliminar (USUCONSECUTIVO): 1
¿Estás seguro? (s/n): s
✓ Usuario eliminado exitosamente
```

**Qué ocurre internamente:**
1. Principal.eliminar() lee el ID (1)
2. Pide confirmación (seguridad)
3. Si el usuario escribe "s", llama a FundUsuarioDAO.eliminar(1)
4. FundUsuarioDAO ejecuta: `DELETE FROM fundusuario WHERE USUCONSECUTIVO = 1`
5. MariaDB elimina la fila
6. Si filasAfectadas > 0, retorna true e imprime éxito

Si escribe "n":
```
¿Estás seguro? (s/n): n
✗ Operación cancelada
```

---

### OPCIÓN 7: SALIR

```
Selecciona una opción: 7
✓ Aplicación cerrada
```

El programa termina y se cierran todos los recursos.

---

## Solución de Problemas

### Error: "Connection refused"
```
java.sql.SQLException: [HY000] (1045) Access denied for user 'root'@'localhost'
```
**Causa:** MariaDB no está corriendo o contraseña incorrecta

**Solución:**
- Verifica que MariaDB esté corriendo: `mysql -u root -p`
- Verifica la contraseña en ConexionJDBC.java es "JacMar1953"

### Error: "Unknown database 'bdpqrsej'"
```
java.sql.SQLException: [HY000] (1049) Unknown database 'bdpqrsej'
```
**Causa:** La BD no existe

**Solución:**
- Crea la BD: `CREATE DATABASE bdpqrsej;`

### Error: "Table 'bdpqrsej.fundusuario' doesn't exist"
```
java.sql.SQLException: [HY000] (1146) Table 'bdpqrsej.fundusuario' doesn't exist
```
**Causa:** La tabla no existe

**Solución:**
- Crea la tabla con la estructura proporcionada

### Error: "java.lang.ClassNotFoundException: org.mariadb.jdbc.Driver"
**Causa:** El driver JDBC no está en el classpath

**Solución:**
- Verifica que existe: `lib\mariadb-java-client-3.0.8.jar`
- Añade `-cp "lib\mariadb-java-client-3.0.8.jar"` al compilar y ejecutar

### Error: "No suitable driver found"
**Causa:** El driver no se cargó correctamente

**Solución:**
- Revisa que el nombre de la clase sea exacto: `org.mariadb.jdbc.Driver`
- Verifica que la URL es: `jdbc:mariadb://localhost:3306/bdpqrsej`

---

## Estructura de Archivos

```
PQRS-JAVA/
├── lib/
│   └── mariadb-java-client-3.0.8.jar (DRIVER JDBC)
│
├── src/main/java/com/pqrs/
│   ├── Principal.java (PROGRAMA PRINCIPAL)
│   ├── util/
│   │   └── ConexionJDBC.java (CONEXIÓN)
│   └── dao/
│       └── FundUsuarioDAO.java (CRUD)
│
├── target/classes/ (ARCHIVOS COMPILADOS - se generan automáticamente)
│   ├── com/pqrs/Principal.class
│   ├── com/pqrs/util/ConexionJDBC.class
│   └── com/pqrs/dao/FundUsuarioDAO.class
│
├── ejecutar.bat (SCRIPT WINDOWS)
├── ejecutar.ps1 (SCRIPT POWERSHELL)
│
├── EJECUTAR.md (Este archivo)
├── DOCUMENTACION_COMPLETA.md (Documentación técnica detallada)
└── README.md (Información general)
```

---

## Resumen de Comandos

**Compilar:**
```powershell
javac -cp "lib\mariadb-java-client-3.0.8.jar;target\classes" -d target\classes src\main\java\com\pqrs\util\ConexionJDBC.java
javac -cp "lib\mariadb-java-client-3.0.8.jar;target\classes" -d target\classes src\main\java\com\pqrs\dao\FundUsuarioDAO.java
javac -cp "lib\mariadb-java-client-3.0.8.jar;target\classes" -d target\classes src\main\java\com\pqrs\Principal.java
```

**Ejecutar:**
```powershell
java -cp "lib\mariadb-java-client-3.0.8.jar;target\classes" com.pqrs.Principal
```

**Automático (Recomendado):**
```powershell
.\ejecutar.bat
```

---

**NOTA IMPORTANTE:** Todos los ejemplos arriba muestran cómo el programa interactúa con MariaDB usando JDBC. Las sentencias SQL reales se encuentran en `FundUsuarioDAO.java`.

Para entender cada sentencia SQL en detalle, consulta `DOCUMENTACION_COMPLETA.md`

