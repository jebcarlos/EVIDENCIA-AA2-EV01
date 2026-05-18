@echo off
chcp 65001 >nul
cd /d "%~dp0"

set "MVN=E:\maven\apache-maven-3.9.9\bin\mvn.cmd"
set "JAR=target\pqrs-java-1.0.0.jar"
set "URL=http://localhost:8080"

echo === Compilando con Maven ===
if not exist "%MVN%" (
    echo ERROR: Maven no encontrado en %MVN%
    pause
    exit /b 1
)

call "%MVN%" clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: La compilacion con Maven fallo. Revisa los logs de arriba.
    pause
    exit /b 1
)

if not exist "%JAR%" (
    echo ERROR: No se genero el archivo %JAR%
    pause
    exit /b 1
)

echo === Iniciando aplicacion Spring Boot ===
echo (Mantener esta ventana abierta mientras la app corre)
echo.
echo Abriendo navegador en %URL% en 3 segundos...
timeout /t 3 /nobreak >nul
start "" "%URL%"

java -jar "%JAR%"

echo.
echo La aplicacion se cerro o fallo. Revisa los mensajes anteriores.
pause