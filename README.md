# PQRS Java Application - CRUD FUNDUSUARIO

Aplicación Java con **JDBC puro** para realizar operaciones CRUD en la tabla `fundusuario` de MariaDB.

## 📋 Tabla de Contenidos

1. [Descripción](#descripción)
2. [Requisitos](#requisitos)
3. [Configuración](#configuración)
4. [Ejecución](#ejecución)
5. [Documentación](#documentación)

---

## 📖 Descripción

Aplicación de consola que permite:
- **CREATE** (C) - Crear nuevos usuarios
- **READ** (R) - Obtener usuarios (todos, por ID, por identificación)
- **UPDATE** (U) - Actualizar datos de usuarios
- **DELETE** (D) - Eliminar usuarios

Implementado con:
- **JDBC puro** (sin frameworks)
- **MariaDB** como base de datos
- **Java 11+**
- Interfaz de menú interactivo

---

## ✓ Requisitos

- **Java JDK 11+** - Instalado en el sistema
- **MariaDB Server** - Corriendo en localhost:3306
- **Base de datos `bdpqrsej`** - Creada en MariaDB
- **Tabla `fundusuario`** - Con estructura específica
- **Driver JDBC MariaDB** - En carpeta `lib/`

---

## 🔧 Configuración

### Conexión a la Base de Datos

**Ubicación:** `src/main/java/com/pqrs/util/ConexionJDBC.java`

```java
private static final String URL = "jdbc:mariadb://localhost:3306/bdpqrsej";
private static final String USER = "root";
private static final String PASSWORD = "JacMar1953";
private static final String DRIVER = "org.mariadb.jdbc.Driver";
```

**Estos valores están hardcodeados. Si necesitas cambiarlos:**
1. Abre `ConexionJDBC.java`
2. Modifica las constantes
3. Recompila

---

## 🚀 Ejecución

### Opción 1: Automática (Recomendada)

```bash
cd e:\SENA\PQRS\PQRS-JAVA
ejecutar.bat
```

### Opción 2: PowerShell

```powershell
cd e:\SENA\PQRS\PQRS-JAVA
.\ejecutar.ps1
```

### Opción 3: Manual

Ver archivo `EJECUTAR.md` para instrucciones paso a paso.

---

## 📚 Documentación

| Archivo | Contenido |
|---------|-----------|
| **DOCUMENTACION_COMPLETA.md** | Documentación técnica detallada de cada clase, método y variable |
| **EJECUTAR.md** | Instrucciones de ejecución con ejemplos reales |
| **README.md** | Este archivo (información general) |

**IMPORTANTE:** Lee `DOCUMENTACION_COMPLETA.md` para entender:
- Cómo funciona la conexión JDBC
- Cada método del CRUD
- Cada sentencia SQL ejecutada
- Variables y librerías usadas
- Flujo completo de ejecución

---

## 📁 Estructura del Proyecto

```
PQRS-JAVA/
├── lib/
│   └── mariadb-java-client-3.0.8.jar (Driver JDBC)
│
├── src/main/java/com/pqrs/
│   ├── Principal.java
│   │   └── Programa principal con menú interactivo
│   ├── util/
│   │   └── ConexionJDBC.java
│   │       └── Gestión de conexiones a MariaDB
│   └── dao/
│       └── FundUsuarioDAO.java
│           └── Métodos CRUD
│
├── target/classes/ (se genera automáticamente)
│
├── ejecutar.bat (compilar y ejecutar - Windows)
├── ejecutar.ps1 (compilar y ejecutar - PowerShell)
│
├── DOCUMENTACION_COMPLETA.md (técnica detallada)
├── EJECUTAR.md (instrucciones y ejemplos)
├── README.md (este archivo)
└── pom.xml (información de Maven - no se usa en JDBC puro)
```

---

## 🔄 Flujo de la Aplicación

```
1. Usuario ejecuta: ejecutar.bat
   ↓
2. Se compilan las clases Java
   ↓
3. Se ejecuta Principal.main()
   ↓
4. Se muestra menú interactivo
   ↓
5. Usuario selecciona opción (1-7)
   ↓
6. Principal llama al método correspondiente
   ↓
7. FundUsuarioDAO ejecuta operación CRUD
   ↓
8. ConexionJDBC obtiene conexión de MariaDB
   ↓
9. Se ejecuta sentencia SQL
   ↓
10. Resultado se retorna a Principal
    ↓
11. Se muestra resultado al usuario
    ↓
12. Vuelve al menú
```

---

## 🎯 Operaciones CRUD

### CREATE - Crear Usuario
```
Opción 1 en el menú
Solicita: TPD, Identificación, Apellidos, Nombres
Ejecuta: INSERT INTO fundusuario (...)
```

### READ - Obtener Usuarios
```
Opción 2: Obtener todos
Opción 3: Obtener por ID (USUCONSECUTIVO)
Opción 4: Obtener por Identificación (Cédula)
Ejecuta: SELECT FROM fundusuario
```

### UPDATE - Actualizar Usuario
```
Opción 5 en el menú
Solicita: ID del usuario y nuevos datos
Ejecuta: UPDATE fundusuario SET ...
```

### DELETE - Eliminar Usuario
```
Opción 6 en el menú
Solicita: ID del usuario + confirmación
Ejecuta: DELETE FROM fundusuario
```

---

## 💾 Tabla FUNDUSUARIO

**Campos principales:**
- `USUCONSECUTIVO` (INT) - ID auto-incrementado
- `TPD` (INT) - Tipo de documento
- `IDENTIFICACION` (VARCHAR) - Número de cédula
- `DV` (INT) - Dígito verificación
- `PRIMERAPELLIDO` (VARCHAR) - Primer apellido
- `SEGUNDOAPELLIDO` (VARCHAR) - Segundo apellido (opcional)
- `PRIMERNOMBRE` (VARCHAR) - Primer nombre
- `SEGUNDONOMBRE` (VARCHAR) - Segundo nombre (opcional)
- `SEXO` (VARCHAR) - M/F/O
- *Y más campos disponibles*

---

## 🔍 Clases Principales

### Principal.java
- Programa principal
- Menú interactivo
- Valida entrada del usuario
- Llama a métodos del DAO

### ConexionJDBC.java
- Carga el driver JDBC
- Obtiene conexiones a MariaDB
- Cierra conexiones correctamente
- Libera recursos

### FundUsuarioDAO.java
- Métodos CRUD
- Ejecuta sentencias SQL
- Maneja excepciones
- Retorna resultados

---

## ⚠️ Solución de Problemas

Consulta `EJECUTAR.md` para solución de problemas comunes como:
- Connection refused
- Unknown database
- ClassNotFoundException
- Tabla no encontrada

---

## 📝 Notas Importantes

- ✅ Todo el código está en **JDBC puro** (sin frameworks)
- ✅ Las credenciales están en `ConexionJDBC.java`
- ✅ Las sentencias SQL están en `FundUsuarioDAO.java`
- ✅ El menú está en `Principal.java`
- ✅ El driver JDBC debe estar en `lib/`
- ✅ MariaDB debe estar corriendo
- ❌ No se requiere Maven para ejecutar

---

## 📞 Archivos de Referencia

Para más detalles, consulta:

1. **DOCUMENTACION_COMPLETA.md** - Explicación técnica de cada método, variable y libería
2. **EJECUTAR.md** - Cómo ejecutar y ejemplos reales de uso
3. **Código fuente** - Archivos `.java` con comentarios

---

**Versión:** 1.0.0  
**Tecnología:** JDBC Puro + MariaDB  
**Java:** 11+

