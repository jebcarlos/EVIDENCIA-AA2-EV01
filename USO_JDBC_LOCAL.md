# Uso del Controlador JDBC Local

## ✓ Drivers Descargados

```
lib/
├── mariadb-java-client-3.0.8.jar (0.56 MB) ✓
└── mysql-connector-java-8.0.33.jar (Opcional)
```

## Configuración de Classpath

### Opción 1: Con Spring Boot (Automático)
```bash
mvn clean install
mvn spring-boot:run
```
Maven incluye el JAR automáticamente.

### Opción 2: Compilar Java usando el JAR local

#### Windows PowerShell:
```powershell
cd e:\SENA\PQRS\PQRS-JAVA

# Compilar
javac -cp "lib\mariadb-java-client-3.0.8.jar" -d target\classes `
  src\main\java\com\pqrs\util\ConexionJDBC.java

javac -cp "lib\mariadb-java-client-3.0.8.jar;target\classes" -d target\classes `
  src\main\java\com\pqrs\util\TestConexion.java

# Ejecutar Test de Conexión
java -cp "lib\mariadb-java-client-3.0.8.jar;target\classes" `
  com.pqrs.util.TestConexion
```

#### Windows CMD (Síntaxis alternativa):
```cmd
cd e:\SENA\PQRS\PQRS-JAVA

javac -cp "lib\mariadb-java-client-3.0.8.jar" -d target\classes ^
  src\main\java\com\pqrs\util\ConexionJDBC.java

javac -cp "lib\mariadb-java-client-3.0.8.jar;target\classes" -d target\classes ^
  src\main\java\com\pqrs\util\TestConexion.java

java -cp "lib\mariadb-java-client-3.0.8.jar;target\classes" ^
  com.pqrs.util.TestConexion
```

### Opción 3: Ejecutar DAO directamente

```powershell
java -cp "lib\mariadb-java-client-3.0.8.jar;target\classes" `
  com.pqrs.dao.FundUsuarioDAO
```

## Verificación de Descarga

Para verificar que el JAR se descargó correctamente:

```powershell
$jarPath = "e:\SENA\PQRS\PQRS-JAVA\lib\mariadb-java-client-3.0.8.jar"

if (Test-Path $jarPath) {
    $info = Get-Item $jarPath
    Write-Host "✓ Archivo encontrado"
    Write-Host "  Tamaño: $($info.Length / 1MB) MB"
    Write-Host "  Ruta: $($info.FullName)"
} else {
    Write-Host "✗ Archivo no encontrado"
}
```

## Incluir en tu IDE

### IntelliJ IDEA
1. File → Project Structure
2. Libraries → + (Add)
3. Seleccionar `lib\mariadb-java-client-3.0.8.jar`
4. Aplicar cambios

### Eclipse
1. Project → Properties
2. Java Build Path → Libraries → Add External JARs
3. Seleccionar `lib\mariadb-java-client-3.0.8.jar`
4. OK

### Visual Studio Code
```json
// .vscode/settings.json
{
  "java.project.referencedLibraries": [
    "lib/**/*.jar"
  ]
}
```

## Prueba Rápida

```bash
# Verificar que el JAR puede ser cargado
jar tf lib\mariadb-java-client-3.0.8.jar | grep -i "Driver"
```

Debería mostrar:
```
org/mariadb/jdbc/Driver.class
org/mariadb/jdbc/DriverVersion.class
...
```

## Próximo Paso

Una vez verificado el JAR, ejecuta:

```bash
mvn clean install
mvn spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8080**

---

**Nota:** El controlador MariaDB JDBC es totalmente compatible con bases de datos MariaDB 10.3+ y MySQL 5.7+
