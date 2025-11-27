package com.example.app.service.impl;

import com.example.app.dto.ProductoDto;
import com.example.app.exception.CustomException;
import com.example.app.model.Producto;
import com.example.app.repository.ProductoRepository;
import com.example.app.service.ProductoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional
    public ProductoDto crear(ProductoDto dto) {
        if (productoRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new CustomException("El producto ya existe");
        }
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        return mapToDto(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public ProductoDto actualizar(UUID id, ProductoDto dto) {
        Producto producto = buscarPorId(id);
        producto.setNombre(dto.getNombre());
        return mapToDto(producto);
    }

    @Override
    public ProductoDto obtenerPorId(UUID id) {
        return mapToDto(buscarPorId(id));
    }

    @Override
    public List<ProductoDto> listarTodos() {
        return productoRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminar(UUID id) {
        Producto producto = buscarPorId(id);
        productoRepository.delete(producto);
    }

    private Producto buscarPorId(UUID id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new CustomException("Producto no encontrado"));
    }

    private ProductoDto mapToDto(Producto producto) {
        return new ProductoDto(producto.getId(), producto.getNombre());
    }
}
