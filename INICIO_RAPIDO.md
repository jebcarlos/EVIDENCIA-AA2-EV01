# ⚡ Inicio Rápido - PQRS CRUD

## 🚀 En 3 Pasos

### Paso 1: Requisitos Básicos
```
✅ Java JDK 11+ instalado
✅ MariaDB corriendo (localhost:3306)
✅ Base de datos: bdpqrsej
✅ Para web: Maven 3.6+ instalado
```

### Paso 2: Ejecutar

#### 🌐 Opción A: Interfaz Web (RECOMENDADO)
```bash
cd e:\SENA\PQRS\PQRS-JAVA
ejecutar-web.bat
```
→ Abre: `http://localhost:8080`

#### 🖥️ Opción B: Consola
```bash
cd e:\SENA\PQRS\PQRS-JAVA
ejecutar.bat
```

### Paso 3: ¡Listo!
- Crea, edita, elimina usuarios
- La interfaz web tiene botones y formularios visuales
- La consola tiene un menú interactivo

---

## 📱 Interfaz Web - Funciones Principales

| Función | Botón | Atajo |
|---------|-------|-------|
| **Crear** | ➕ Nuevo Usuario | - |
| **Editar** | ✏️ Editar | - |
| **Eliminar** | 🗑️ Eliminar | - |
| **Buscar** | 🔍 Buscar | Enter |
| **Ver todos** | 🔄 Resetear | - |

---

## 🎯 Casos de Uso

### Crear Usuario
1. Click ➕ Nuevo Usuario
2. Completa: TPD, Identificación, Apellido, Nombre
3. Click Guardar

### Buscar Usuario
1. Escribe identificación
2. Click 🔍 Buscar
3. Se muestra resultado

### Editar Usuario
1. Busca el usuario
2. Click ✏️ Editar
3. Modifica campos
4. Click Guardar

### Eliminar Usuario
1. Busca el usuario
2. Click 🗑️ Eliminar
3. Confirma
4. ¡Listo!

---

## ⚠️ Problemas Comunes

| Problema | Solución |
|----------|----------|
| **Port 8080 ya en uso** | Cambiar puerto en `application.properties`: `server.port=8081` |
| **Base datos no conecta** | Verificar MariaDB corriendo, usuario/contraseña en ConexionJDBC.java |
| **Maven no encontrado** | Instalar desde: https://maven.apache.org/download.cgi |
| **Error en navegador** | Presionar F12, revisar Console y Network |

---

## 📚 Documentación Completa

Para detalles:
- 🌐 **INTERFAZ_WEB.md** - Guía web completa
- 📖 **DOCUMENTACION_COMPLETA.md** - Detalles técnicos
- 🔧 **EJECUTAR.md** - Instrucciones avanzadas

---

## 💡 Tips

✨ Usa la **interfaz web** para:
- Mejor experiencia visual
- Acceso desde móvil
- Búsqueda y filtrado

⚡ Usa la **consola** para:
- Bajo consumo de recursos
- Ejecución rápida
- Sin dependencias externas

---

**¡Listo para usar! 🎉**
