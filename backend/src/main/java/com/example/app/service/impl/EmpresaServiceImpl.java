package com.example.app.service.impl;

import com.example.app.dto.EmpresaDto;
import com.example.app.exception.CustomException;
import com.example.app.model.Empresa;
import com.example.app.repository.EmpresaRepository;
import com.example.app.service.EmpresaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Override
    @Transactional
    public EmpresaDto crear(EmpresaDto dto) {
        if (empresaRepository.existsByNit(dto.getNit())) {
            throw new CustomException("Ya existe una empresa con ese NIT");
        }
        Empresa empresa = mapToEntity(dto);
        return mapToDto(empresaRepository.save(empresa));
    }

    @Override
    @Transactional
    public EmpresaDto actualizar(UUID id, EmpresaDto dto) {
        Empresa empresa = buscarPorId(id);
        empresa.setNombre(dto.getNombre());
        empresa.setNit(dto.getNit());
        empresa.setDireccion(dto.getDireccion());
        empresa.setTelefono(dto.getTelefono());
        return mapToDto(empresa);
    }

    @Override
    public EmpresaDto obtenerPorId(UUID id) {
        return mapToDto(buscarPorId(id));
    }

    @Override
    public List<EmpresaDto> listarTodas() {
        return empresaRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminar(UUID id) {
        Empresa empresa = buscarPorId(id);
        empresaRepository.delete(empresa);
    }

    private Empresa buscarPorId(UUID id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new CustomException("Empresa no encontrada"));
    }

    private EmpresaDto mapToDto(Empresa empresa) {
        return new EmpresaDto(
                empresa.getId(),
                empresa.getNombre(),
                empresa.getNit(),
                empresa.getDireccion(),
                empresa.getTelefono()
        );
    }

    private Empresa mapToEntity(EmpresaDto dto) {
        Empresa empresa = new Empresa();
        empresa.setNombre(dto.getNombre());
        empresa.setNit(dto.getNit());
        empresa.setDireccion(dto.getDireccion());
        empresa.setTelefono(dto.getTelefono());
        return empresa;
    }
}
