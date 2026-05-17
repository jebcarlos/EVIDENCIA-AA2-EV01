package com.pqrs.util;

import java.sql.*;

public class ConexionJDBC {
    
    private static final String URL = "jdbc:mariadb://localhost:3306/bdpqrsej";
    private static final String USER = "root";
    private static final String PASSWORD = "JacMar1953";
    private static final String DRIVER = "org.mariadb.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void cerrarConexion(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void cerrarRecursos(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            cerrarConexion(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
