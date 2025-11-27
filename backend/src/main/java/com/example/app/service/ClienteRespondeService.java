package com.example.app.service;

import com.example.app.dto.ClienteRespondeDto;

import java.util.List;
import java.util.UUID;

public interface ClienteRespondeService {
    ClienteRespondeDto crear(ClienteRespondeDto dto);
    ClienteRespondeDto actualizar(UUID id, ClienteRespondeDto dto);
    ClienteRespondeDto obtenerPorId(UUID id);
    List<ClienteRespondeDto> listarTodos();
    void eliminar(UUID id);
}
