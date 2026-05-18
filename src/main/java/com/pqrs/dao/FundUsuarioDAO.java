package com.pqrs.dao;

import com.pqrs.util.ConexionJDBC;
import com.pqrs.dto.UsuarioResponseDTO;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FundUsuarioDAO {

    // CREATE
    public static boolean crear(int tpd, String identificacion, Integer dv, String primerApellido, 
                                String segundoApellido, String primerNombre, String segundoNombre,
                                LocalDate fechaNacimiento, String sexo, Integer tipoSangre) {
        String sql = "INSERT INTO fundusuario (TPD, IDENTIFICACION, DV, PRIMERAPELLIDO, SEGUNDOAPELLIDO, " +
                     "PRIMERNOMBRE, SEGUNDONOMBRE, FECHANACIMIENTO, SEXO, TIPOSANGRE) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionJDBC.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tpd);
            pstmt.setString(2, identificacion);
            pstmt.setObject(3, dv);
            pstmt.setString(4, primerApellido);
            pstmt.setString(5, segundoApellido);
            pstmt.setString(6, primerNombre);
            pstmt.setString(7, segundoNombre);
            pstmt.setObject(8, fechaNacimiento != null ? java.sql.Date.valueOf(fechaNacimiento) : null);
            pstmt.setString(9, sexo);
            pstmt.setObject(10, tipoSangre);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ - Obtener todos
    public static void obtenerTodos() {
        String sql = "SELECT USUCONSECUTIVO, TPD, IDENTIFICACION, DV, PRIMERAPELLIDO, SEGUNDOAPELLIDO, " +
                     "PRIMERNOMBRE, SEGUNDONOMBRE, FECHANACIMIENTO, SEXO, TIPOSANGRE FROM fundusuario";
        try (Connection conn = ConexionJDBC.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\n╔════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                        LISTA DE TODOS LOS USUARIOS                    ║");
            System.out.println("╚════════════════════════════════════════════════════════════════════════╝");
            
            int contador = 0;
            while (rs.next()) {
                contador++;
                System.out.println("\n─────────────────────────────────────────────────────────");
                System.out.println("Usuario #" + contador);
                System.out.println("─────────────────────────────────────────────────────────");
                System.out.println("ID (USUCONSECUTIVO):    " + rs.getInt("USUCONSECUTIVO"));
                System.out.println("Tipo de Documento:      " + rs.getInt("TPD"));
                System.out.println("Identificación:         " + rs.getString("IDENTIFICACION"));
                System.out.println("Dígito Verificación:    " + (rs.getObject("DV") != null ? rs.getInt("DV") : "N/A"));
                System.out.println("Apellidos:              " + rs.getString("PRIMERAPELLIDO") + 
                                   (rs.getString("SEGUNDOAPELLIDO") != null ? " " + rs.getString("SEGUNDOAPELLIDO") : ""));
                System.out.println("Nombres:                " + rs.getString("PRIMERNOMBRE") + 
                                   (rs.getString("SEGUNDONOMBRE") != null ? " " + rs.getString("SEGUNDONOMBRE") : ""));
                System.out.println("Fecha de Nacimiento:    " + (rs.getDate("FECHANACIMIENTO") != null ? rs.getDate("FECHANACIMIENTO") : "N/A"));
                System.out.println("Sexo:                   " + (rs.getString("SEXO") != null ? rs.getString("SEXO") : "N/A"));
                System.out.println("Tipo de Sangre:         " + (rs.getObject("TIPOSANGRE") != null ? rs.getInt("TIPOSANGRE") : "N/A"));
            }
            
            if (contador == 0) {
                System.out.println("No hay usuarios registrados en la base de datos.");
            } else {
                System.out.println("\n─────────────────────────────────────────────────────────");
                System.out.println("Total de usuarios: " + contador);
                System.out.println("═════════════════════════════════════════════════════════");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ - Obtener por ID
    public static void obtenerPorId(int usuConsecutivo) {
        String sql = "SELECT * FROM fundusuario WHERE USUCONSECUTIVO = ?";
        try (Connection conn = ConexionJDBC.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuConsecutivo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("\n╔════════════════════════════════════════════════════════════╗");
                System.out.println("║              DETALLE DEL USUARIO ID " + usuConsecutivo + "              ║");
                System.out.println("╚════════════════════════════════════════════════════════════╝");
                System.out.println("\n📋 INFORMACIÓN PERSONAL");
                System.out.println("─────────────────────────────────────────────────────────");
                System.out.println("ID (USUCONSECUTIVO):    " + rs.getInt("USUCONSECUTIVO"));
                System.out.println("Tipo de Documento:      " + rs.getInt("TPD"));
                System.out.println("Identificación:         " + rs.getString("IDENTIFICACION"));
                System.out.println("Dígito Verificación:    " + (rs.getObject("DV") != null ? rs.getInt("DV") : "N/A"));
                System.out.println("\n👤 DATOS PERSONALES");
                System.out.println("─────────────────────────────────────────────────────────");
                System.out.println("Apellidos:              " + rs.getString("PRIMERAPELLIDO") + 
                                 (rs.getString("SEGUNDOAPELLIDO") != null ? " " + rs.getString("SEGUNDOAPELLIDO") : ""));
                System.out.println("Nombres:                " + rs.getString("PRIMERNOMBRE") + 
                                 (rs.getString("SEGUNDONOMBRE") != null ? " " + rs.getString("SEGUNDONOMBRE") : ""));
                System.out.println("Fecha de Nacimiento:    " + (rs.getDate("FECHANACIMIENTO") != null ? rs.getDate("FECHANACIMIENTO") : "N/A"));
                System.out.println("Sexo:                   " + (rs.getString("SEXO") != null ? rs.getString("SEXO") : "N/A"));
                System.out.println("\n🏥 INFORMACIÓN MÉDICA");
                System.out.println("─────────────────────────────────────────────────────────");
                System.out.println("Tipo de Sangre:         " + (rs.getObject("TIPOSANGRE") != null ? rs.getInt("TIPOSANGRE") : "N/A"));
                System.out.println("Altura:                 " + (rs.getString("ALTURA") != null ? rs.getString("ALTURA") : "N/A"));
                System.out.println("Estrato:                " + (rs.getObject("ESTRATO") != null ? rs.getInt("ESTRATO") : "N/A"));
                System.out.println("\n📍 INFORMACIÓN ADICIONAL");
                System.out.println("─────────────────────────────────────────────────────────");
                System.out.println("Departamento Nacimiento: " + (rs.getObject("DEPTONACIMIENTO") != null ? rs.getInt("DEPTONACIMIENTO") : "N/A"));
                System.out.println("Municipio Nacimiento:   " + (rs.getObject("MUNICIPIONACIMIENTO") != null ? rs.getInt("MUNICIPIONACIMIENTO") : "N/A"));
                System.out.println("Estado Civil:           " + (rs.getObject("ESTADOCIVIL") != null ? rs.getInt("ESTADOCIVIL") : "N/A"));
                System.out.println("Educación:              " + (rs.getObject("EDUCACION") != null ? rs.getInt("EDUCACION") : "N/A"));
                System.out.println("Ocupación ID:           " + (rs.getObject("OCUPACIONID") != null ? rs.getInt("OCUPACIONID") : "N/A"));
                System.out.println("EPS:                    " + (rs.getObject("EPS") != null ? rs.getInt("EPS") : "N/A"));
                System.out.println("SISBEN:                 " + (rs.getObject("SISBEN") != null ? rs.getInt("SISBEN") : "N/A"));
                System.out.println("═════════════════════════════════════════════════════════");
            } else {
                System.out.println("\n✗ Usuario con ID " + usuConsecutivo + " no encontrado");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // UPDATE - Actualizar todos los campos
    public static boolean actualizar(int usuConsecutivo, int tpd, String identificacion, Integer dv,
                                     String primerApellido, String segundoApellido, String primerNombre, 
                                     String segundoNombre, LocalDate fechaNacimiento, String sexo, Integer tipoSangre) {
        String sql = "UPDATE fundusuario SET TPD = ?, IDENTIFICACION = ?, DV = ?, " +
                     "PRIMERAPELLIDO = ?, SEGUNDOAPELLIDO = ?, PRIMERNOMBRE = ?, SEGUNDONOMBRE = ?, " +
                     "FECHANACIMIENTO = ?, SEXO = ?, TIPOSANGRE = ? WHERE USUCONSECUTIVO = ?";
        try (Connection conn = ConexionJDBC.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tpd);
            pstmt.setString(2, identificacion);
            pstmt.setObject(3, dv);
            pstmt.setString(4, primerApellido);
            pstmt.setString(5, segundoApellido);
            pstmt.setString(6, primerNombre);
            pstmt.setString(7, segundoNombre);
            pstmt.setObject(8, fechaNacimiento != null ? java.sql.Date.valueOf(fechaNacimiento) : null);
            pstmt.setString(9, sexo);
            pstmt.setObject(10, tipoSangre);
            pstmt.setInt(11, usuConsecutivo);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // UPDATE - Actualizar solo campos no nulos (consola)
    public static boolean actualizarCampos(int usuConsecutivo, Integer dv,
                                           String primerApellido, String segundoApellido,
                                           String primerNombre, String segundoNombre,
                                           LocalDate fechaNacimiento, String sexo, Integer tipoSangre) {
        StringBuilder sql = new StringBuilder("UPDATE fundusuario SET ");
        java.util.ArrayList<Object> valores = new java.util.ArrayList<>();
        
        if (dv != null) { sql.append("DV = ?, "); valores.add(dv); }
        if (primerApellido != null) { sql.append("PRIMERAPELLIDO = ?, "); valores.add(primerApellido); }
        if (segundoApellido != null) { sql.append("SEGUNDOAPELLIDO = ?, "); valores.add(segundoApellido); }
        if (primerNombre != null) { sql.append("PRIMERNOMBRE = ?, "); valores.add(primerNombre); }
        if (segundoNombre != null) { sql.append("SEGUNDONOMBRE = ?, "); valores.add(segundoNombre); }
        if (fechaNacimiento != null) { sql.append("FECHANACIMIENTO = ?, "); valores.add(java.sql.Date.valueOf(fechaNacimiento)); }
        if (sexo != null) { sql.append("SEXO = ?, "); valores.add(sexo); }
        if (tipoSangre != null) { sql.append("TIPOSANGRE = ?, "); valores.add(tipoSangre); }
        
        if (valores.isEmpty()) {
            System.out.println("✗ No se especificó ningún campo para actualizar");
            return false;
        }
        
        sql.setLength(sql.length() - 2);
        sql.append(" WHERE USUCONSECUTIVO = ?");
        valores.add(usuConsecutivo);
        
        try (Connection conn = ConexionJDBC.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < valores.size(); i++) {
                pstmt.setObject(i + 1, valores.get(i));
            }
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public static boolean eliminar(int usuConsecutivo) {
        String sql = "DELETE FROM fundusuario WHERE USUCONSECUTIVO = ?";
        try (Connection conn = ConexionJDBC.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuConsecutivo);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // SEARCH - Obtener por identificación
    public static void obtenerPorIdentificacion(String identificacion) {
        String sql = "SELECT * FROM fundusuario WHERE IDENTIFICACION = ?";
        try (Connection conn = ConexionJDBC.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, identificacion);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("\n╔════════════════════════════════════════════════════════════╗");
                System.out.println("║        BÚSQUEDA POR IDENTIFICACIÓN: " + identificacion + "          ║");
                System.out.println("╚════════════════════════════════════════════════════════════╝");
                System.out.println("\n📋 INFORMACIÓN PERSONAL");
                System.out.println("─────────────────────────────────────────────────────────");
                System.out.println("ID (USUCONSECUTIVO):    " + rs.getInt("USUCONSECUTIVO"));
                System.out.println("Tipo de Documento:      " + rs.getInt("TPD"));
                System.out.println("Identificación:         " + rs.getString("IDENTIFICACION"));
                System.out.println("Dígito Verificación:    " + (rs.getObject("DV") != null ? rs.getInt("DV") : "N/A"));
                System.out.println("\n👤 DATOS PERSONALES");
                System.out.println("─────────────────────────────────────────────────────────");
                System.out.println("Apellidos:              " + rs.getString("PRIMERAPELLIDO") + 
                                 (rs.getString("SEGUNDOAPELLIDO") != null ? " " + rs.getString("SEGUNDOAPELLIDO") : ""));
                System.out.println("Nombres:                " + rs.getString("PRIMERNOMBRE") + 
                                 (rs.getString("SEGUNDONOMBRE") != null ? " " + rs.getString("SEGUNDONOMBRE") : ""));
                System.out.println("Fecha de Nacimiento:    " + (rs.getDate("FECHANACIMIENTO") != null ? rs.getDate("FECHANACIMIENTO") : "N/A"));
                System.out.println("Sexo:                   " + (rs.getString("SEXO") != null ? rs.getString("SEXO") : "N/A"));
                System.out.println("\n🏥 INFORMACIÓN MÉDICA");
                System.out.println("─────────────────────────────────────────────────────────");
                System.out.println("Tipo de Sangre:         " + (rs.getObject("TIPOSANGRE") != null ? rs.getInt("TIPOSANGRE") : "N/A"));
                System.out.println("Altura:                 " + (rs.getString("ALTURA") != null ? rs.getString("ALTURA") : "N/A"));
                System.out.println("Estrato:                " + (rs.getObject("ESTRATO") != null ? rs.getInt("ESTRATO") : "N/A"));
                System.out.println("═════════════════════════════════════════════════════════");
            } else {
                System.out.println("\n✗ No se encontró usuario con identificación: " + identificacion);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ========== MÉTODOS JSON PARA API REST ==========
    
    // GET All - JSON
    public static List<UsuarioResponseDTO> obtenerTodosJSON() {
        List<UsuarioResponseDTO> usuarios = new ArrayList<>();
        String sql = "SELECT USUCONSECUTIVO, TPD, IDENTIFICACION, DV, PRIMERAPELLIDO, SEGUNDOAPELLIDO, " +
                     "PRIMERNOMBRE, SEGUNDONOMBRE, FECHANACIMIENTO, SEXO, TIPOSANGRE FROM fundusuario";
        try (Connection conn = ConexionJDBC.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                UsuarioResponseDTO usuario = new UsuarioResponseDTO(
                    rs.getInt("USUCONSECUTIVO"),
                    rs.getInt("TPD"),
                    rs.getString("IDENTIFICACION"),
                    (Integer) rs.getObject("DV"),
                    rs.getString("PRIMERAPELLIDO"),
                    rs.getString("SEGUNDOAPELLIDO"),
                    rs.getString("PRIMERNOMBRE"),
                    rs.getString("SEGUNDONOMBRE"),
                    rs.getDate("FECHANACIMIENTO") != null ? rs.getDate("FECHANACIMIENTO").toLocalDate() : null,
                    rs.getString("SEXO"),
                    (Integer) rs.getObject("TIPOSANGRE")
                );
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }
    
    // GET By ID - JSON
    public static UsuarioResponseDTO obtenerPorIdJSON(int usuConsecutivo) {
        String sql = "SELECT * FROM fundusuario WHERE USUCONSECUTIVO = ?";
        try (Connection conn = ConexionJDBC.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuConsecutivo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new UsuarioResponseDTO(
                    rs.getInt("USUCONSECUTIVO"),
                    rs.getInt("TPD"),
                    rs.getString("IDENTIFICACION"),
                    (Integer) rs.getObject("DV"),
                    rs.getString("PRIMERAPELLIDO"),
                    rs.getString("SEGUNDOAPELLIDO"),
                    rs.getString("PRIMERNOMBRE"),
                    rs.getString("SEGUNDONOMBRE"),
                    rs.getDate("FECHANACIMIENTO") != null ? rs.getDate("FECHANACIMIENTO").toLocalDate() : null,
                    rs.getString("SEXO"),
                    (Integer) rs.getObject("TIPOSANGRE")
                );
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // GET By Identificacion - JSON
    public static List<UsuarioResponseDTO> obtenerPorIdentificacionJSON(String identificacion) {
        List<UsuarioResponseDTO> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM fundusuario WHERE IDENTIFICACION = ?";
        try (Connection conn = ConexionJDBC.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, identificacion);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                UsuarioResponseDTO usuario = new UsuarioResponseDTO(
                    rs.getInt("USUCONSECUTIVO"),
                    rs.getInt("TPD"),
                    rs.getString("IDENTIFICACION"),
                    (Integer) rs.getObject("DV"),
                    rs.getString("PRIMERAPELLIDO"),
                    rs.getString("SEGUNDOAPELLIDO"),
                    rs.getString("PRIMERNOMBRE"),
                    rs.getString("SEGUNDONOMBRE"),
                    rs.getDate("FECHANACIMIENTO") != null ? rs.getDate("FECHANACIMIENTO").toLocalDate() : null,
                    rs.getString("SEXO"),
                    (Integer) rs.getObject("TIPOSANGRE")
                );
                usuarios.add(usuario);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }
}
