@echo off
cd /d e:\SENA\PQRS\PQRS-JAVA

echo === Compilando clases Java ===
javac -cp "lib\mariadb-java-client-3.0.8.jar" -d target\classes src\main\java\com\pqrs\util\ConexionJDBC.java
javac -cp "lib\mariadb-java-client-3.0.8.jar;target\classes" -d target\classes src\main\java\com\pqrs\dao\FundUsuarioDAO.java
javac -cp "lib\mariadb-java-client-3.0.8.jar;target\classes" -d target\classes src\main\java\com\pqrs\Principal.java

echo === Ejecutando aplicaci?n ===
java -cp "lib\mariadb-java-client-3.0.8.jar;target\classes" com.pqrs.Principal

pause
