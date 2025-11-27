package com.example.app.service.impl;

import com.example.app.dto.UsuarioAutorizadoDto;
import com.example.app.exception.CustomException;
import com.example.app.model.Empresa;
import com.example.app.model.UsuarioAutorizado;
import com.example.app.repository.EmpresaRepository;
import com.example.app.repository.UsuarioAutorizadoRepository;
import com.example.app.service.UsuarioAutorizadoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioAutorizadoServiceImpl implements UsuarioAutorizadoService {

    private final UsuarioAutorizadoRepository usuarioAutorizadoRepository;
    private final EmpresaRepository empresaRepository;

    public UsuarioAutorizadoServiceImpl(UsuarioAutorizadoRepository usuarioAutorizadoRepository,
                                        EmpresaRepository empresaRepository) {
        this.usuarioAutorizadoRepository = usuarioAutorizadoRepository;
        this.empresaRepository = empresaRepository;
    }

    @Override
    @Transactional
    public UsuarioAutorizadoDto crear(UsuarioAutorizadoDto dto) {
        if (usuarioAutorizadoRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new CustomException("Ya existe un usuario autorizado con ese correo");
        }
        Empresa empresa = buscarEmpresa(dto.getEmpresaId());
        UsuarioAutorizado usuario = new UsuarioAutorizado();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setClave(dto.getClave());
        usuario.setRol(dto.getRol());
        usuario.setEstado(dto.getEstado());
        usuario.setEmpresa(empresa);
        return mapToDto(usuarioAutorizadoRepository.save(usuario));
    }

    @Override
    @Transactional
    public UsuarioAutorizadoDto actualizar(UUID id, UsuarioAutorizadoDto dto) {
        UsuarioAutorizado usuario = buscarPorId(id);
        if (!usuario.getEmail().equals(dto.getEmail()) &&
                usuarioAutorizadoRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new CustomException("Ya existe un usuario autorizado con ese correo");
        }
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(dto.getRol());
        usuario.setEstado(dto.getEstado());
        if (StringUtils.hasText(dto.getClave())) {
            usuario.setClave(dto.getClave());
        }
        if (!usuario.getEmpresa().getId().equals(dto.getEmpresaId())) {
            Empresa empresa = buscarEmpresa(dto.getEmpresaId());
            usuario.setEmpresa(empresa);
        }
        return mapToDto(usuario);
    }

    @Override
    public UsuarioAutorizadoDto obtenerPorId(UUID id) {
        return mapToDto(buscarPorId(id));
    }

    @Override
    public List<UsuarioAutorizadoDto> listarTodos() {
        return usuarioAutorizadoRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminar(UUID id) {
        UsuarioAutorizado usuario = buscarPorId(id);
        usuarioAutorizadoRepository.delete(usuario);
    }

    private UsuarioAutorizado buscarPorId(UUID id) {
        return usuarioAutorizadoRepository.findById(id)
                .orElseThrow(() -> new CustomException("Usuario autorizado no encontrado"));
    }

    private Empresa buscarEmpresa(UUID id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new CustomException("Empresa no encontrada"));
    }

    private UsuarioAutorizadoDto mapToDto(UsuarioAutorizado usuario) {
        return new UsuarioAutorizadoDto(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                null,
                usuario.getRol(),
                usuario.getEstado(),
                usuario.getEmpresa() != null ? usuario.getEmpresa().getId() : null
        );
    }
}
