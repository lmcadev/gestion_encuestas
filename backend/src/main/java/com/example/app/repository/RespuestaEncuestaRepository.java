package com.example.app.repository;

import com.example.app.model.RespuestaEncuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RespuestaEncuestaRepository extends JpaRepository<RespuestaEncuesta, UUID> {
    List<RespuestaEncuesta> findByEncuesta_Id(UUID encuestaId);
    List<RespuestaEncuesta> findByPregunta_Id(UUID preguntaId);
    List<RespuestaEncuesta> findByCliente_Id(UUID clienteId);
}
