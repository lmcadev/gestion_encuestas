package com.example.app.service;

import com.example.app.dto.EncuestaCompletaDto;
import com.example.app.dto.EncuestaDto;

import java.util.List;
import java.util.UUID;

public interface EncuestaService {
    EncuestaDto crear(EncuestaDto dto);
    EncuestaDto actualizar(UUID id, EncuestaDto dto);
    EncuestaDto obtenerPorId(UUID id);
    EncuestaCompletaDto obtenerCompleta(UUID id);
    List<EncuestaDto> listarTodas();
    List<EncuestaDto> listarPorProducto(UUID productoId);
    List<EncuestaDto> listarPorCreador(UUID usuarioId);
    void eliminar(UUID id);
}
