package com.pqrs.repository;

import com.pqrs.entity.FundUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FundUsuarioRepository extends JpaRepository<FundUsuario, Integer> {
    Optional<FundUsuario> findByEmail(String email);
}
