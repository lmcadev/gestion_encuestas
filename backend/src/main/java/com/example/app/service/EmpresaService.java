package com.example.app.service;

import com.example.app.dto.EmpresaDto;

import java.util.List;
import java.util.UUID;

public interface EmpresaService {
    EmpresaDto crear(EmpresaDto dto);
    EmpresaDto actualizar(UUID id, EmpresaDto dto);
    EmpresaDto obtenerPorId(UUID id);
    List<EmpresaDto> listarTodas();
    void eliminar(UUID id);
}
