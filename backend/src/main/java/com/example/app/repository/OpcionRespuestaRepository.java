package com.example.app.repository;

import com.example.app.model.OpcionRespuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OpcionRespuestaRepository extends JpaRepository<OpcionRespuesta, UUID> {
    List<OpcionRespuesta> findByPregunta_IdOrderByOrdenAsc(UUID preguntaId);
}
