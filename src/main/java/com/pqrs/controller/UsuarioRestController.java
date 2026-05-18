package com.pqrs.controller;

import com.pqrs.dao.FundUsuarioDAO;
import com.pqrs.dto.ApiResponseDTO;
import com.pqrs.dto.CreateUserDTO;
import com.pqrs.dto.UsuarioResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioRestController {
    
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<UsuarioResponseDTO>>> obtenerTodos() {
        try {
            List<UsuarioResponseDTO> usuarios = FundUsuarioDAO.obtenerTodosJSON();
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Usuarios obtenidos correctamente", usuarios));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error al obtener usuarios: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UsuarioResponseDTO>> obtenerPorId(@PathVariable int id) {
        try {
            UsuarioResponseDTO usuario = FundUsuarioDAO.obtenerPorIdJSON(id);
            if (usuario != null) {
                return ResponseEntity.ok(new ApiResponseDTO<>(true, "Usuario encontrado", usuario));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, "Usuario no encontrado"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error al obtener usuario: " + e.getMessage()));
        }
    }
    
    @GetMapping("/buscar/identificacion/{identificacion}")
    public ResponseEntity<ApiResponseDTO<List<UsuarioResponseDTO>>> obtenerPorIdentificacion(
            @PathVariable String identificacion) {
        try {
            List<UsuarioResponseDTO> usuarios = FundUsuarioDAO.obtenerPorIdentificacionJSON(identificacion);
            if (!usuarios.isEmpty()) {
                return ResponseEntity.ok(new ApiResponseDTO<>(true, "Usuarios encontrados", usuarios));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, "No se encontraron usuarios con esa identificación"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error al buscar usuario: " + e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponseDTO<String>> crear(@RequestBody CreateUserDTO dto) {
        try {
            // Validaciones básicas
            if (dto.getIdentificacion() == null || dto.getIdentificacion().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(false, "La identificación no puede estar vacía"));
            }
            if (dto.getPrimerApellido() == null || dto.getPrimerApellido().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(false, "El primer apellido no puede estar vacío"));
            }
            if (dto.getPrimerNombre() == null || dto.getPrimerNombre().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(false, "El primer nombre no puede estar vacío"));
            }
            
            boolean creado = FundUsuarioDAO.crear(
                dto.getTpd(),
                dto.getIdentificacion(),
                dto.getDv(),
                dto.getPrimerApellido(),
                dto.getSegundoApellido(),
                dto.getPrimerNombre(),
                dto.getSegundoNombre(),
                dto.getFechaNacimiento(),
                dto.getSexo(),
                dto.getTipoSangre()
            );
            
            if (creado) {
                return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO<>(true, "Usuario creado exitosamente"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al crear el usuario"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error al crear usuario: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<String>> actualizar(
            @PathVariable int id,
            @RequestBody CreateUserDTO dto) {
        try {
            // Validaciones básicas
            if (dto.getIdentificacion() == null || dto.getIdentificacion().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(false, "La identificación no puede estar vacía"));
            }
            if (dto.getPrimerApellido() == null || dto.getPrimerApellido().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(false, "El primer apellido no puede estar vacío"));
            }
            if (dto.getPrimerNombre() == null || dto.getPrimerNombre().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(false, "El primer nombre no puede estar vacío"));
            }
            
            boolean actualizado = FundUsuarioDAO.actualizar(
                id,
                dto.getTpd(),
                dto.getIdentificacion(),
                dto.getDv(),
                dto.getPrimerApellido(),
                dto.getSegundoApellido(),
                dto.getPrimerNombre(),
                dto.getSegundoNombre(),
                dto.getFechaNacimiento(),
                dto.getSexo(),
                dto.getTipoSangre()
            );
            
            if (actualizado) {
                return ResponseEntity.ok(new ApiResponseDTO<>(true, "Usuario actualizado exitosamente"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, "Usuario no encontrado o no se pudo actualizar"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error al actualizar usuario: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<String>> eliminar(@PathVariable int id) {
        try {
            boolean eliminado = FundUsuarioDAO.eliminar(id);
            if (eliminado) {
                return ResponseEntity.ok(new ApiResponseDTO<>(true, "Usuario eliminado exitosamente"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, "Usuario no encontrado o no se pudo eliminar"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error al eliminar usuario: " + e.getMessage()));
        }
    }
}
