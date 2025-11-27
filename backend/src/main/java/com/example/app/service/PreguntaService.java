package com.example.app.service;

import com.example.app.dto.PreguntaDto;

import java.util.List;
import java.util.UUID;

public interface PreguntaService {
    PreguntaDto crear(PreguntaDto dto);
    PreguntaDto actualizar(UUID id, PreguntaDto dto);
    PreguntaDto obtenerPorId(UUID id);
    List<PreguntaDto> listarTodas();
    List<PreguntaDto> listarPorEncuesta(UUID encuestaId);
    void eliminar(UUID id);
}
