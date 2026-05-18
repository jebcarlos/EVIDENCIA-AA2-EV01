package com.pqrs;

import com.pqrs.dao.FundUsuarioDAO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Principal {
    
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public static void main(String[] args) {
        boolean salir = false;
        
        while (!salir) {
            mostrarMenu();
            int opcion = obtenerOpcion();
            
            switch (opcion) {
                case 1:
                    crear();
                    break;
                case 2:
                    obtenerTodos();
                    break;
                case 3:
                    obtenerPorId();
                    break;
                case 4:
                    obtenerPorIdentificacion();
                    break;
                case 5:
                    actualizar();
                    break;
                case 6:
                    eliminar();
                    break;
                case 7:
                    salir = true;
                    System.out.println("\n✓ Aplicación cerrada");
                    break;
                default:
                    System.out.println("✗ Opción no válida. Intenta de nuevo.\n");
            }
        }
        scanner.close();
    }
    
    private static void mostrarMenu() {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║     CRUD USUARIOS - TABLA FUNDUSUARIO         ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║ 1. Crear usuario                              ║");
        System.out.println("║ 2. Obtener todos los usuarios                 ║");
        System.out.println("║ 3. Obtener usuario por ID (USUCONSECUTIVO)    ║");
        System.out.println("║ 4. Obtener usuario por Identificación         ║");
        System.out.println("║ 5. Actualizar usuario                         ║");
        System.out.println("║ 6. Eliminar usuario                           ║");
        System.out.println("║ 7. Salir                                      ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.print("Selecciona una opción: ");
    }
    
    private static int obtenerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static void crear() {
        System.out.println("\n--- CREAR NUEVO USUARIO ---");
        
        System.out.print("Tipo de Documento (TPD) [1=CC, 2=CE, etc]: ");
        int tpd;
        try {
            tpd = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("✗ TPD no válido");
            return;
        }
        
        System.out.print("Identificación: ");
        String identificacion = scanner.nextLine().trim();
        if (identificacion.isEmpty()) {
            System.out.println("✗ La identificación no puede estar vacía");
            return;
        }
        
        System.out.print("DV (Dígito de Verificación) [Opcional, presiona Enter para saltar]: ");
        String dvStr = scanner.nextLine().trim();
        Integer dv = null;
        if (!dvStr.isEmpty()) {
            try {
                dv = Integer.parseInt(dvStr);
            } catch (NumberFormatException e) {
                System.out.println("✗ DV no válido");
                return;
            }
        }
        
        System.out.print("Primer Apellido: ");
        String primerApellido = scanner.nextLine().trim();
        if (primerApellido.isEmpty()) {
            System.out.println("✗ El primer apellido no puede estar vacío");
            return;
        }
        
        System.out.print("Segundo Apellido [Opcional]: ");
        String segundoApellido = scanner.nextLine().trim();
        if (segundoApellido.isEmpty()) segundoApellido = null;
        
        System.out.print("Primer Nombre: ");
        String primerNombre = scanner.nextLine().trim();
        if (primerNombre.isEmpty()) {
            System.out.println("✗ El primer nombre no puede estar vacío");
            return;
        }
        
        System.out.print("Segundo Nombre [Opcional]: ");
        String segundoNombre = scanner.nextLine().trim();
        if (segundoNombre.isEmpty()) segundoNombre = null;
        
        System.out.print("Fecha de Nacimiento [Opcional, formato: yyyy-MM-dd]: ");
        String fechaStr = scanner.nextLine().trim();
        LocalDate fechaNacimiento = null;
        if (!fechaStr.isEmpty()) {
            try {
                fechaNacimiento = LocalDate.parse(fechaStr, dateFormatter);
            } catch (Exception e) {
                System.out.println("✗ Formato de fecha no válido. Use yyyy-MM-dd");
                return;
            }
        }
        
        System.out.print("Sexo [M/F/O] [Opcional]: ");
        String sexo = scanner.nextLine().trim();
        if (sexo.isEmpty()) sexo = null;
        
        System.out.print("Tipo de Sangre [0=O-, 1=O+, 2=A-, 3=A+, 4=B-, 5=B+, 6=AB-, 7=AB+] [Opcional]: ");
        String tipoSangreStr = scanner.nextLine().trim();
        Integer tipoSangre = null;
        if (!tipoSangreStr.isEmpty()) {
            try {
                tipoSangre = Integer.parseInt(tipoSangreStr);
            } catch (NumberFormatException e) {
                System.out.println("✗ Tipo de sangre no válido");
                return;
            }
        }
        
        if (FundUsuarioDAO.crear(tpd, identificacion, dv, primerApellido, segundoApellido, 
                                  primerNombre, segundoNombre, fechaNacimiento, sexo, tipoSangre)) {
            System.out.println("\n✓ Usuario creado exitosamente");
        } else {
            System.out.println("\n✗ Error al crear el usuario");
        }
    }
    
    private static void obtenerTodos() {
        System.out.println("\n--- OBTENER TODOS LOS USUARIOS ---");
        FundUsuarioDAO.obtenerTodos();
    }
    
    private static void obtenerPorId() {
        System.out.println("\n--- OBTENER USUARIO POR ID (USUCONSECUTIVO) ---");
        System.out.print("ID del usuario: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            FundUsuarioDAO.obtenerPorId(id);
        } catch (NumberFormatException e) {
            System.out.println("✗ ID no válido");
        }
    }
    
    private static void obtenerPorIdentificacion() {
        System.out.println("\n--- OBTENER USUARIO POR IDENTIFICACIÓN ---");
        System.out.print("Número de identificación: ");
        String identificacion = scanner.nextLine().trim();
        if (identificacion.isEmpty()) {
            System.out.println("✗ La identificación no puede estar vacía");
            return;
        }
        FundUsuarioDAO.obtenerPorIdentificacion(identificacion);
    }
    
    private static void actualizar() {
        System.out.println("\n--- ACTUALIZAR USUARIO ---");
        System.out.print("ID del usuario a actualizar (USUCONSECUTIVO): ");
        try {
            int usuConsecutivo = Integer.parseInt(scanner.nextLine());
            
            System.out.println("\nDeje en blanco los campos que NO desea modificar.\n");
            
            System.out.print("DV (Dígito de Verificación) [Enter para no modificar]: ");
            String dvStr = scanner.nextLine().trim();
            Integer dv = null;
            if (!dvStr.isEmpty()) {
                try {
                    dv = Integer.parseInt(dvStr);
                } catch (NumberFormatException e) {
                    System.out.println("✗ DV no válido");
                    return;
                }
            }
            
            System.out.print("Primer Apellido [Enter para no modificar]: ");
            String primerApellido = scanner.nextLine().trim();
            if (primerApellido.isEmpty()) primerApellido = null;
            
            System.out.print("Segundo Apellido [Enter para no modificar]: ");
            String segundoApellido = scanner.nextLine().trim();
            if (segundoApellido.isEmpty()) segundoApellido = null;
            
            System.out.print("Primer Nombre [Enter para no modificar]: ");
            String primerNombre = scanner.nextLine().trim();
            if (primerNombre.isEmpty()) primerNombre = null;
            
            System.out.print("Segundo Nombre [Enter para no modificar]: ");
            String segundoNombre = scanner.nextLine().trim();
            if (segundoNombre.isEmpty()) segundoNombre = null;
            
            System.out.print("Fecha de Nacimiento [yyyy-MM-dd] [Enter para no modificar]: ");
            String fechaStr = scanner.nextLine().trim();
            LocalDate fechaNacimiento = null;
            if (!fechaStr.isEmpty()) {
                try {
                    fechaNacimiento = LocalDate.parse(fechaStr, dateFormatter);
                } catch (Exception e) {
                    System.out.println("✗ Formato de fecha no válido. Use yyyy-MM-dd");
                    return;
                }
            }
            
            System.out.print("Sexo [M/F/O] [Enter para no modificar]: ");
            String sexo = scanner.nextLine().trim();
            if (sexo.isEmpty()) sexo = null;
            
            System.out.print("Tipo de Sangre [0=O-, 1=O+, 2=A-, 3=A+, 4=B-, 5=B+, 6=AB-, 7=AB+] [Enter para no modificar]: ");
            String tipoSangreStr = scanner.nextLine().trim();
            Integer tipoSangre = null;
            if (!tipoSangreStr.isEmpty()) {
                try {
                    tipoSangre = Integer.parseInt(tipoSangreStr);
                } catch (NumberFormatException e) {
                    System.out.println("✗ Tipo de sangre no válido");
                    return;
                }
            }
            
            if (FundUsuarioDAO.actualizarCampos(usuConsecutivo, dv,
                                                primerApellido, segundoApellido, primerNombre, 
                                                segundoNombre, fechaNacimiento, sexo, tipoSangre)) {
                System.out.println("\n✓ Usuario actualizado exitosamente");
            } else {
                System.out.println("\n✗ Error al actualizar el usuario");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Datos no válidos");
        }
    }
    
    private static void eliminar() {
        System.out.println("\n--- ELIMINAR USUARIO ---");
        System.out.print("ID del usuario a eliminar (USUCONSECUTIVO): ");
        try {
            int usuConsecutivo = Integer.parseInt(scanner.nextLine());
            
            System.out.print("¿Estás seguro? (s/n): ");
            String confirmacion = scanner.nextLine().trim().toLowerCase();
            
            if (confirmacion.equals("s")) {
                if (FundUsuarioDAO.eliminar(usuConsecutivo)) {
                    System.out.println("\n✓ Usuario eliminado exitosamente");
                } else {
                    System.out.println("\n✗ Error al eliminar el usuario");
                }
            } else {
                System.out.println("✗ Operación cancelada");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ ID no válido");
        }
    }
}
