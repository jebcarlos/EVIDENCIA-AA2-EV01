# PQRS Java Application - CRUD FUNDUSUARIO

Aplicación Java para realizar operaciones CRUD en la tabla `fundusuario` de MariaDB.

**Ahora con dos formas de acceso:**
- 🖥️ **Interfaz de Consola** (JDBC puro)
- 🌐 **Interfaz Web** (Spring Boot + HTML/CSS/JS)

## 📋 Tabla de Contenidos

1. [Descripción](#descripción)
2. [Requisitos](#requisitos)
3. [Ejecución](#ejecución)
4. [Documentación](#documentación)

---

## 📖 Descripción

Aplicación que permite:
- **CREATE** (C) - Crear nuevos usuarios
- **READ** (R) - Obtener usuarios (todos, por ID, por identificación)
- **UPDATE** (U) - Actualizar datos de usuarios
- **DELETE** (D) - Eliminar usuarios

### Dos interfaces disponibles:

#### 🖥️ Consola (Original)
- JDBC puro
- Menú interactivo en terminal
- Sin dependencias externas
- Ejecución: `ejecutar.bat`

#### 🌐 Web (NUEVO)
- Spring Boot + REST API
- Interfaz web moderna y responsiva
- Diseño moderno con gradientes y animaciones
- Acceso desde navegador: `http://localhost:8080`
- Ejecución: `ejecutar-web.bat`

---

## ✓ Requisitos

### General
- **Java JDK 11+** - Instalado en el sistema
- **MariaDB Server** - Corriendo en localhost:3306
- **Base de datos `bdpqrsej`** - Creada en MariaDB
- **Tabla `fundusuario`** - Con estructura específica
- **Driver JDBC MariaDB** - En carpeta `lib/`

### Para Interfaz Web (NUEVO)
- **Maven 3.6+** - Para compilación
- **Navegador web moderno** - Para acceder
- **Puerto 8080** - Disponible

---

## 🚀 Ejecución

### 🌐 OPCIÓN 1: Interfaz Web (RECOMENDADO)

```bash
cd e:\SENA\PQRS\PQRS-JAVA
ejecutar-web.bat
```

Luego abre en navegador: `http://localhost:8080`

**Características:**
- ✅ Interfaz moderna y responsiva
- ✅ Formularios intuitivos con modales
- ✅ Búsqueda en tiempo real
- ✅ Validación visual
- ✅ Funciona en móvil y desktop
- ✅ Sin dependencias en frontend

### 🖥️ OPCIÓN 2: Interfaz de Consola (Original)

```bash
cd e:\SENA\PQRS\PQRS-JAVA
ejecutar.bat
```

O con PowerShell:

```powershell
cd e:\SENA\PQRS\PQRS-JAVA
.\ejecutar.ps1
```

**Características:**
- ✅ JDBC puro, sin frameworks
- ✅ Menú interactivo en terminal
- ✅ Bajo consumo de recursos
- ✅ Ejecución rápida

---

## 📚 Documentación

| Archivo | Contenido |
|---------|-----------|
| **INTERFAZ_WEB.md** | 🌐 Guía completa de la interfaz web, endpoints API, troubleshooting |
| **CAMBIOS_INTERFAZ_WEB.md** | ✨ Resumen de cambios realizados y arquitectura |
| **DOCUMENTACION_COMPLETA.md** | 📖 Documentación técnica de clases, métodos y variables |
| **EJECUTAR.md** | 🖥️ Instrucciones de ejecución con ejemplos reales |
| **README.md** | Este archivo (información general) |

---

### Para Interfaz Web
Lee **INTERFAZ_WEB.md** para entender:
- Cómo usar la interfaz web
- Endpoints REST disponibles
- Instalación de Maven
- Solución de problemas comunes

### Para Interfaz de Consola
Lee **DOCUMENTACION_COMPLETA.md** para entender:
- Cómo funciona la conexión JDBC
- Cada método del CRUD
- Cada sentencia SQL ejecutada
- Variables y librerías usadas

---

## 📁 Estructura del Proyecto

```
PQRS-JAVA/
├── src/
│   ├── main/
│   │   ├── java/com/pqrs/
│   │   │   ├── Principal.java (consola)
│   │   │   ├── controller/
│   │   │   │   ├── FundUsuarioController.java
│   │   │   │   └── UsuarioRestController.java ⭐ NUEVO
│   │   │   ├── dao/
│   │   │   │   └── FundUsuarioDAO.java
│   │   │   ├── dto/ ⭐ NUEVO
│   │   │   │   ├── CreateUserDTO.java
│   │   │   │   ├── UsuarioResponseDTO.java
│   │   │   │   └── ApiResponseDTO.java
│   │   │   ├── entity/
│   │   │   ├── util/
│   │   │   └── PqrsApplication.java
│   │   └── resources/
│   │       ├── static/ ⭐ NUEVO
│   │       │   ├── index.html
│   │       │   ├── css/style.css
│   │       │   └── js/main.js
│   │       └── application.properties
│   └── test/
├── lib/
├── target/
├── pom.xml
├── ejecutar.bat (consola)
├── ejecutar-web.bat ⭐ NUEVO
├── INTERFAZ_WEB.md ⭐ NUEVO
├── CAMBIOS_INTERFAZ_WEB.md ⭐ NUEVO
├── DOCUMENTACION_COMPLETA.md
├── EJECUTAR.md
└── README.md
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

## 🔄 Flujo de la Aplicación

### 🌐 Interfaz Web

```
1. Ejecutar: ejecutar-web.bat
   ↓
2. Maven compila el proyecto
   ↓
3. Spring Boot inicia en puerto 8080
   ↓
4. Abrir navegador: http://localhost:8080
   ↓
5. Frontend (HTML/CSS/JS) se carga
   ↓
6. Usuario interactúa con formularios
   ↓
7. JavaScript hace llamadas AJAX (Fetch API)
   ↓
8. Backend REST recibe solicitud
   ↓
9. UsuarioRestController procesa datos
   ↓
10. FundUsuarioDAO ejecuta operación CRUD
    ↓
11. SQL se ejecuta en MariaDB
    ↓
12. Resultado se serializa a JSON
    ↓
13. Frontend recibe respuesta JSON
    ↓
14. UI se actualiza automáticamente
```

### 🖥️ Interfaz de Consola

```
1. Ejecutar: ejecutar.bat
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

