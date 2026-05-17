package com.pqrs.controller;

import com.pqrs.entity.FundUsuario;
import com.pqrs.service.FundUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fundusuario")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FundUsuarioController {

    @Autowired
    private FundUsuarioService fundUsuarioService;

    // CREATE
    @PostMapping
    public ResponseEntity<FundUsuario> crear(@RequestBody FundUsuario fundUsuario) {
        fundUsuario.setFechaCreacion(LocalDateTime.now());
        FundUsuario nuevo = fundUsuarioService.crear(fundUsuario);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    // READ - Obtener todos
    @GetMapping
    public ResponseEntity<List<FundUsuario>> obtenerTodos() {
        List<FundUsuario> usuarios = fundUsuarioService.obtenerTodos();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    // READ - Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<FundUsuario> obtenerPorId(@PathVariable Integer id) {
        Optional<FundUsuario> usuario = fundUsuarioService.obtenerPorId(id);
        return usuario.map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // READ - Obtener por Email
    @GetMapping("/email/{email}")
    public ResponseEntity<FundUsuario> obtenerPorEmail(@PathVariable String email) {
        Optional<FundUsuario> usuario = fundUsuarioService.obtenerPorEmail(email);
        return usuario.map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<FundUsuario> actualizar(@PathVariable Integer id, @RequestBody FundUsuario fundUsuario) {
        FundUsuario actualizado = fundUsuarioService.actualizar(id, fundUsuario);
        if (actualizado != null) {
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (fundUsuarioService.eliminar(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
