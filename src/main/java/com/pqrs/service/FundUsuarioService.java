package com.pqrs.service;

import com.pqrs.entity.FundUsuario;
import com.pqrs.repository.FundUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FundUsuarioService {

    @Autowired
    private FundUsuarioRepository fundUsuarioRepository;

    // CREATE
    public FundUsuario crear(FundUsuario fundUsuario) {
        return fundUsuarioRepository.save(fundUsuario);
    }

    // READ - Obtener todos
    public List<FundUsuario> obtenerTodos() {
        return fundUsuarioRepository.findAll();
    }

    // READ - Obtener por ID
    public Optional<FundUsuario> obtenerPorId(Integer id) {
        return fundUsuarioRepository.findById(id);
    }

    // READ - Obtener por Email
    public Optional<FundUsuario> obtenerPorEmail(String email) {
        return fundUsuarioRepository.findByEmail(email);
    }

    // UPDATE
    public FundUsuario actualizar(Integer id, FundUsuario fundUsuarioActualizado) {
        Optional<FundUsuario> usuario = fundUsuarioRepository.findById(id);
        if (usuario.isPresent()) {
            FundUsuario u = usuario.get();
            if (fundUsuarioActualizado.getNombre() != null) {
                u.setNombre(fundUsuarioActualizado.getNombre());
            }
            if (fundUsuarioActualizado.getEmail() != null) {
                u.setEmail(fundUsuarioActualizado.getEmail());
            }
            if (fundUsuarioActualizado.getTelefono() != null) {
                u.setTelefono(fundUsuarioActualizado.getTelefono());
            }
            if (fundUsuarioActualizado.getActivo() != null) {
                u.setActivo(fundUsuarioActualizado.getActivo());
            }
            return fundUsuarioRepository.save(u);
        }
        return null;
    }

    // DELETE
    public boolean eliminar(Integer id) {
        if (fundUsuarioRepository.existsById(id)) {
            fundUsuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
