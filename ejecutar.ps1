# Script para compilar y ejecutar PQRS CRUD

$proyectDir = "e:\SENA\PQRS\PQRS-JAVA"
$libDir = "$proyectDir\lib"
$srcDir = "$proyectDir\src\main\java\com\pqrs"
$targetDir = "$proyectDir\target\classes"

# Crear directorio target si no existe
if (!(Test-Path $targetDir)) {
    New-Item -ItemType Directory -Path $targetDir | Out-Null
}

Write-Host "=== Compilando clases Java ===" -ForegroundColor Green

# Compilar ConexionJDBC
Write-Host "1. Compilando ConexionJDBC.java..."
javac -cp "$libDir\mariadb-java-client-3.0.8.jar" -d $targetDir "$srcDir\util\ConexionJDBC.java"

# Compilar FundUsuarioDAO
Write-Host "2. Compilando FundUsuarioDAO.java..."
javac -cp "$libDir\mariadb-java-client-3.0.8.jar;$targetDir" -d $targetDir "$srcDir\dao\FundUsuarioDAO.java"

# Compilar Principal
Write-Host "3. Compilando Principal.java..."
javac -cp "$libDir\mariadb-java-client-3.0.8.jar;$targetDir" -d $targetDir "$srcDir\Principal.java"

Write-Host "`n=== Ejecutando aplicación ===" -ForegroundColor Green
java -cp "$libDir\mariadb-java-client-3.0.8.jar;$targetDir" com.pqrs.Principal
