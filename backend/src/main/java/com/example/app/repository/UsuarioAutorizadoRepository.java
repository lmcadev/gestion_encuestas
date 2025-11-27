package com.example.app.repository;

import com.example.app.model.UsuarioAutorizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioAutorizadoRepository extends JpaRepository<UsuarioAutorizado, UUID> {
    Optional<UsuarioAutorizado> findByEmail(String email);
}
