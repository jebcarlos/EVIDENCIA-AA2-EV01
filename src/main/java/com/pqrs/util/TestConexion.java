package com.pqrs.util;

import java.sql.*;

public class TestConexion {
    
    public static void main(String[] args) {
        System.out.println("=== Test de Conexión a MariaDB ===");
        Connection conn = null;
        try {
            conn = ConexionJDBC.obtenerConexion();
            if (conn != null) {
                System.out.println("✓ Conexión exitosa a la base de datos");
                
                // Obtener información de la conexión
                DatabaseMetaData metaData = conn.getMetaData();
                System.out.println("Driver: " + metaData.getDriverName());
                System.out.println("Base de datos: " + metaData.getDatabaseProductName());
                System.out.println("Versión: " + metaData.getDatabaseProductVersion());
                
                // Listar tablas
                System.out.println("\n=== Tablas en bdpqrsej ===");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'bdpqrsej'");
                
                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME");
                    System.out.println("- " + tableName);
                    
                    if (tableName.equals("fundusuario")) {
                        System.out.println("  Estructura de fundusuario:");
                        ResultSet columnas = stmt.executeQuery("DESCRIBE fundusuario");
                        while (columnas.next()) {
                            System.out.println("    " + columnas.getString("Field") + " (" + columnas.getString("Type") + ")");
                        }
                    }
                }
                
                ConexionJDBC.cerrarRecursos(rs, stmt, conn);
            } else {
                System.out.println("✗ No se pudo establecer la conexión");
            }
        } catch (SQLException e) {
            System.out.println("✗ Error en la conexión:");
            e.printStackTrace();
        } finally {
            ConexionJDBC.cerrarConexion(conn);
        }
    }
}
