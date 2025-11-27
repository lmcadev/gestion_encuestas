package com.example.app.service;

import com.example.app.dto.OpcionRespuestaDto;

import java.util.List;
import java.util.UUID;

public interface OpcionRespuestaService {
    OpcionRespuestaDto crear(OpcionRespuestaDto dto);
    OpcionRespuestaDto actualizar(UUID id, OpcionRespuestaDto dto);
    OpcionRespuestaDto obtenerPorId(UUID id);
    List<OpcionRespuestaDto> listarPorPregunta(UUID preguntaId);
    void eliminar(UUID id);
}
