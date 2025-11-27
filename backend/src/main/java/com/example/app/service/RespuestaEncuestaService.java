package com.example.app.service;

import com.example.app.dto.RespuestaEncuestaDto;

import java.util.List;
import java.util.UUID;

public interface RespuestaEncuestaService {
    RespuestaEncuestaDto crear(RespuestaEncuestaDto dto);
    List<RespuestaEncuestaDto> listarTodas();
    List<RespuestaEncuestaDto> listarPorEncuesta(UUID encuestaId);
    List<RespuestaEncuestaDto> listarPorPregunta(UUID preguntaId);
    List<RespuestaEncuestaDto> listarPorCliente(UUID clienteId);
}
