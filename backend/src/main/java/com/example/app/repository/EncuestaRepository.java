package com.example.app.repository;

import com.example.app.model.Encuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EncuestaRepository extends JpaRepository<Encuesta, UUID> {
    List<Encuesta> findByProducto_Id(UUID productoId);
    List<Encuesta> findByCreadoPor_Id(UUID usuarioId);
}
