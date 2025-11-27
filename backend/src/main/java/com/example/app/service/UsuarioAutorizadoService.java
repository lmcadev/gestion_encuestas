package com.example.app.service;

import com.example.app.dto.UsuarioAutorizadoDto;

import java.util.List;
import java.util.UUID;

public interface UsuarioAutorizadoService {
    UsuarioAutorizadoDto crear(UsuarioAutorizadoDto dto);
    UsuarioAutorizadoDto actualizar(UUID id, UsuarioAutorizadoDto dto);
    UsuarioAutorizadoDto obtenerPorId(UUID id);
    List<UsuarioAutorizadoDto> listarTodos();
    void eliminar(UUID id);
}
