# Sistema Visual - CRUD Usuarios PQRS

## 📋 Descripción

Se ha implementado una **interfaz web moderna y responsiva** para el CRUD de usuarios. Reemplaza la interfaz de consola con una aplicación web profesional accesible desde el navegador.

## 🚀 Características

✅ **CRUD Completo**: Crear, leer, actualizar y eliminar usuarios  
✅ **Búsqueda**: Buscar usuarios por identificación  
✅ **Interfaz Responsiva**: Funciona en computadoras, tablets y celulares  
✅ **Modales**: Formularios en modales visuales  
✅ **Validación**: Validación de campos en el cliente y servidor  
✅ **Notificaciones**: Mensajes de éxito/error visuales  
✅ **Diseño Moderno**: UI profesional con gradientes y animaciones  

## 📦 Requisitos

- **Java 11+**
- **Maven 3.6+** (para compilación)
- **MariaDB/MySQL** (base de datos PQRS)
- **Navegador Web moderno** (Chrome, Firefox, Safari, Edge)

### Instalación de Maven (Windows)

1. Descarga Maven desde: https://maven.apache.org/download.cgi
2. Extrae el ZIP en una carpeta (ej: `C:\maven`)
3. Agrega a las variables de entorno PATH: `C:\maven\bin`
4. Verifica con: `mvn -v`

## 🎯 Cómo Ejecutar

### Opción 1: Usando el archivo batch (Recomendado)

```bash
ejecutar-web.bat
```

Esto:
1. Compila el proyecto con Maven
2. Genera el JAR ejecutable
3. Inicia la aplicación Spring Boot
4. Abre automáticamente en `http://localhost:8080`

### Opción 2: Manual con Maven

```bash
mvn clean package -DskipTests
java -jar target/pqrs-java-1.0.0.jar
```

### Opción 3: Desarrollo con Maven

```bash
mvn spring-boot:run
```

## 🌐 Acceso a la Aplicación

Una vez iniciado el servidor, abre tu navegador y accede a:

```
http://localhost:8080
```

## 📱 Funcionalidades

### 1. Listar Usuarios
- Visualiza todos los usuarios en una tabla responsiva
- Información: ID, Documento, Identificación, Nombre Completo

### 2. Crear Usuario
- Click en **➕ Nuevo Usuario**
- Completa el formulario con:
  - Tipo de Documento (requerido)
  - Identificación (requerido)
  - Dígito de Verificación (opcional)
  - Nombres y Apellidos (requerido)
  - Fecha de Nacimiento (opcional)
  - Sexo (opcional)
  - Tipo de Sangre (opcional)
- Click en **Guardar Usuario**

### 3. Editar Usuario
- Click en el botón **✏️ Editar** en la fila del usuario
- Modifica los campos necesarios
- Click en **Guardar Usuario**

### 4. Eliminar Usuario
- Click en el botón **🗑️ Eliminar** en la fila del usuario
- Confirma la eliminación en el diálogo de confirmación

### 5. Buscar Usuario
- Ingresa la identificación en el campo de búsqueda
- Click en **🔍 Buscar** o presiona Enter
- Click en **🔄 Resetear** para ver todos los usuarios nuevamente

## 🏗️ Estructura de Archivos

```
src/
├── main/
│   ├── java/com/pqrs/
│   │   ├── controller/
│   │   │   ├── FundUsuarioController.java        (Controlador antiguo)
│   │   │   └── UsuarioRestController.java        (NUEVO - API REST)
│   │   ├── dao/
│   │   │   └── FundUsuarioDAO.java               (Actualizado con métodos JSON)
│   │   ├── dto/
│   │   │   ├── CreateUserDTO.java                (NUEVO)
│   │   │   ├── UsuarioResponseDTO.java           (NUEVO)
│   │   │   └── ApiResponseDTO.java               (NUEVO)
│   │   ├── entity/
│   │   └── util/
│   └── resources/
│       ├── static/                                (NUEVO - Archivos web)
│       │   ├── index.html                        (NUEVO - Página principal)
│       │   ├── css/
│       │   │   └── style.css                     (NUEVO - Estilos)
│       │   └── js/
│       │       └── main.js                       (NUEVO - Lógica JavaScript)
│       └── application.properties                (Configuración Spring Boot)
```

## 🔌 Endpoints API REST

La aplicación expone los siguientes endpoints:

| Método | URL | Descripción |
|--------|-----|-------------|
| **GET** | `/api/usuarios` | Obtener todos los usuarios |
| **GET** | `/api/usuarios/{id}` | Obtener usuario por ID |
| **GET** | `/api/usuarios/buscar/identificacion/{id}` | Buscar por identificación |
| **POST** | `/api/usuarios` | Crear nuevo usuario |
| **PUT** | `/api/usuarios/{id}` | Actualizar usuario |
| **DELETE** | `/api/usuarios/{id}` | Eliminar usuario |

### Ejemplo de Request (crear usuario)

```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "tpd": 1,
    "identificacion": "1234567890",
    "primerApellido": "García",
    "primerNombre": "Juan",
    "fechaNacimiento": "1990-05-15",
    "sexo": "M",
    "tipoSangre": 3
  }'
```

## 🎨 Tecnologías Utilizadas

### Backend
- **Spring Boot 3.0** - Framework Java
- **Spring Web** - REST APIs
- **JPA/Hibernate** - ORM
- **MariaDB Driver** - Conexión a base de datos

### Frontend
- **HTML5** - Estructura
- **CSS3** - Estilos responsivos con gradientes y animaciones
- **JavaScript Vanilla** - Lógica sin dependencias externas
- **Fetch API** - Comunicación con servidor

## 🔒 Seguridad

⚠️ **Nota**: La aplicación actual permite CORS para todos los orígenes. Para producción:

1. Configura CORS específicamente en `UsuarioRestController`:
```java
@CrossOrigin(origins = "http://dominio.com")
```

2. Agrega autenticación y autorización
3. Valida y sanitiza todas las entradas
4. Usa HTTPS en producción

## 🐛 Troubleshooting

### Error: "Maven no encontrado"
- Instala Maven: https://maven.apache.org/install.html
- O ejecuta manualmente: `mvn clean package -DskipTests`

### Error: Conexión a Base de Datos rechazada
- Verifica que MariaDB esté ejecutándose
- Comprueba usuario/contraseña en `application.properties`
- Verifica que la base de datos `bdpqrsej` existe

### Error: Puerto 8080 en uso
- Cambia el puerto en `application.properties`:
```properties
server.port=8081
```

### Formulario no responde
- Abre la consola del navegador (F12)
- Revisa la pestaña "Network" para ver las llamadas AJAX
- Verifica que la API está respondiendo

## 📝 Notas de Desarrollo

- El sistema mantiene toda la lógica existente del DAO
- La interfaz antigua de consola sigue disponible en `ejecutar.bat`
- Se pueden usar ambas interfaces simultáneamente
- Los datos se sincronizan a través de la misma base de datos

## 🚀 Próximas Mejoras

- [ ] Paginación de resultados
- [ ] Exportar a Excel/PDF
- [ ] Filtros avanzados
- [ ] Sistema de usuarios y autenticación
- [ ] Dashboard con estadísticas
- [ ] Historial de cambios
- [ ] Búsqueda avanzada con múltiples criterios

## 📞 Soporte

Si encontras problemas:
1. Revisa la consola del navegador (F12)
2. Revisa los logs de Spring Boot en la terminal
3. Verifica la conexión a base de datos
4. Consulta la documentación de Spring Boot: https://spring.io

## 📄 Licencia

Este proyecto es parte del sistema PQRS.
