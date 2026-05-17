# Estructura del Proyecto PQRS Java

## Descripción General
Proyecto Spring Boot con CRUD completo para la tabla `fundusuario` de la base de datos `bdpqrsej` en MariaDB.

## Configuración de Conexión
- **URL**: jdbc:mariadb://localhost:3306/bdpqrsej
- **Usuario**: root
- **Contraseña**: JacMar1953
- **Driver**: org.mariadb.jdbc.Driver

## Archivos Creados

### 1. Configuración Principal
- **pom.xml**: Configuración Maven con dependencias Spring Boot, JPA, MariaDB
- **src/main/resources/application.properties**: Configuración de la base de datos

### 2. Clase Principal
- **PqrsApplication.java**: Punto de entrada de la aplicación Spring Boot

### 3. Capas de la Aplicación (Arquitectura MVC)

#### Entity Layer (Modelo)
- **FundUsuario.java**: Entidad JPA que mapea la tabla `fundusuario`
  - Atributos: id, nombre, email, telefono, activo, fechaCreacion

#### Repository Layer (Acceso a Datos)
- **FundUsuarioRepository.java**: Interfaz JPA Repository
  - Métodos: findAll(), findById(), save(), delete(), findByEmail()

#### Service Layer (Lógica de Negocio)
- **FundUsuarioService.java**: Lógica CRUD
  - crear()
  - obtenerTodos()
  - obtenerPorId()
  - obtenerPorEmail()
  - actualizar()
  - eliminar()

#### Controller Layer (REST API)
- **FundUsuarioController.java**: Endpoints REST
  - GET /api/fundusuario - Obtener todos
  - GET /api/fundusuario/{id} - Obtener por ID
  - GET /api/fundusuario/email/{email} - Obtener por email
  - POST /api/fundusuario - Crear
  - PUT /api/fundusuario/{id} - Actualizar
  - DELETE /api/fundusuario/{id} - Eliminar

### 4. Utilidades JDBC Puro (Alternativa sin Spring)
- **ConexionJDBC.java**: Clase para conexiones JDBC
  - obtenerConexion()
  - cerrarConexion()
  - cerrarRecursos()

- **TestConexion.java**: Script de prueba de conexión
  - Verifica conexión a MariaDB
  - Lista todas las tablas
  - Muestra estructura de fundusuario

- **FundUsuarioDAO.java**: Data Access Object con operaciones CRUD usando JDBC puro
  - crear()
  - obtenerTodos()
  - obtenerPorId()
  - actualizar()
  - eliminar()

## Flujo de Operaciones

### CREATE (Crear Usuario)
```
POST /api/fundusuario
    ↓
FundUsuarioController.crear()
    ↓
FundUsuarioService.crear()
    ↓
FundUsuarioRepository.save()
    ↓
MariaDB (INSERT)
```

### READ (Obtener Usuarios)
```
GET /api/fundusuario
    ↓
FundUsuarioController.obtenerTodos()
    ↓
FundUsuarioService.obtenerTodos()
    ↓
FundUsuarioRepository.findAll()
    ↓
MariaDB (SELECT)
```

### UPDATE (Actualizar Usuario)
```
PUT /api/fundusuario/{id}
    ↓
FundUsuarioController.actualizar()
    ↓
FundUsuarioService.actualizar()
    ↓
FundUsuarioRepository.save()
    ↓
MariaDB (UPDATE)
```

### DELETE (Eliminar Usuario)
```
DELETE /api/fundusuario/{id}
    ↓
FundUsuarioController.eliminar()
    ↓
FundUsuarioService.eliminar()
    ↓
FundUsuarioRepository.deleteById()
    ↓
MariaDB (DELETE)
```

## Cómo Ejecutar

### Opción 1: Spring Boot REST API
```bash
cd e:\SENA\PQRS\PQRS-JAVA
mvn clean install
mvn spring-boot:run
```
Acceder a http://localhost:8080/api/fundusuario

### Opción 2: JDBC Puro (Test de Conexión)
```bash
cd e:\SENA\PQRS\PQRS-JAVA
javac -cp "lib/*:." src/main/java/com/pqrs/util/ConexionJDBC.java
javac -cp "lib/*:." src/main/java/com/pqrs/util/TestConexion.java
java -cp "lib/*:." com.pqrs.util.TestConexion
```

### Opción 3: JDBC Puro (CRUD Manual)
```bash
java -cp "lib/*:." com.pqrs.dao.FundUsuarioDAO
```

## Ejemplo de Uso REST con cURL

### Crear
```bash
curl -X POST http://localhost:8080/api/fundusuario \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Juan Pérez","email":"juan@example.com","telefono":"3105555555","activo":true}'
```

### Leer Todos
```bash
curl http://localhost:8080/api/fundusuario
```

### Leer por ID
```bash
curl http://localhost:8080/api/fundusuario/1
```

### Actualizar
```bash
curl -X PUT http://localhost:8080/api/fundusuario/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Juan Carlos","email":"juancarlos@example.com"}'
```

### Eliminar
```bash
curl -X DELETE http://localhost:8080/api/fundusuario/1
```

## Notas Importantes

1. **MariaDB debe estar corriendo** en localhost:3306
2. **La base de datos "bdpqrsej"** debe existir
3. **La tabla "fundusuario"** debe existir (se crea automáticamente con Spring Boot si no existe)
4. **Las credenciales** están configuradas en application.properties
5. **JDBC y JPA** están disponibles simultáneamente en este proyecto
