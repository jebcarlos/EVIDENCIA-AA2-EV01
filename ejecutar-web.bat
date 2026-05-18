@echo off
REM Ejecutar aplicación Spring Boot PQRS
REM Este script busca Maven en el PATH o usa java directamente

cd /d "%~dp0"

echo === Compilando con Maven ===

REM Intentar ejecutar Maven desde PATH
where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Maven encontrado. Compilando...
    call mvn clean package -DskipTests
    if %ERRORLEVEL% EQU 0 (
        echo.
        echo === Ejecutando aplicación ===
        java -jar target\pqrs-java-1.0.0.jar
    ) else (
        echo Error durante la compilación
        pause
    )
) else (
    echo Maven no encontrado en PATH.
    echo Por favor, instala Maven o agrega a PATH.
    pause
)
