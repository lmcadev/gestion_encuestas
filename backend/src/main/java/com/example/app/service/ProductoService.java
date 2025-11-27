package com.example.app.service;

import com.example.app.dto.ProductoDto;

import java.util.List;
import java.util.UUID;

public interface ProductoService {
    ProductoDto crear(ProductoDto dto);
    ProductoDto actualizar(UUID id, ProductoDto dto);
    ProductoDto obtenerPorId(UUID id);
    List<ProductoDto> listarTodos();
    void eliminar(UUID id);
}
