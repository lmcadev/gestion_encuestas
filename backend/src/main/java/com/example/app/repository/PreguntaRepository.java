package com.example.app.repository;

import com.example.app.model.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, UUID> {
    List<Pregunta> findByEncuesta_IdOrderByOrdenAsc(UUID encuestaId);
}
