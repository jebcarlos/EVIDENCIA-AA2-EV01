@echo off
REM Ejecutar aplicación Spring Boot PQRS
REM Usa Maven desde ubicación local

cd /d "%~dp0"

echo === Compilando e iniciando servidor web ===
echo.

REM Ejecutar Maven desde ruta local
call "E:\maven\apache-maven-3.9.9\bin\mvn.cmd" spring-boot:run

pause
