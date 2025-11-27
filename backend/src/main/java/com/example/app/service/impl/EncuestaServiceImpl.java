package com.example.app.service.impl;

import com.example.app.dto.EncuestaCompletaDto;
import com.example.app.dto.EncuestaDto;
import com.example.app.exception.CustomException;
import com.example.app.model.Encuesta;
import com.example.app.model.Producto;
import com.example.app.model.UsuarioAutorizado;
import com.example.app.repository.EncuestaRepository;
import com.example.app.repository.ProductoRepository;
import com.example.app.repository.UsuarioAutorizadoRepository;
import com.example.app.service.EncuestaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EncuestaServiceImpl implements EncuestaService {

    private final EncuestaRepository encuestaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioAutorizadoRepository usuarioAutorizadoRepository;

    public EncuestaServiceImpl(EncuestaRepository encuestaRepository,
                               ProductoRepository productoRepository,
                               UsuarioAutorizadoRepository usuarioAutorizadoRepository) {
        this.encuestaRepository = encuestaRepository;
        this.productoRepository = productoRepository;
        this.usuarioAutorizadoRepository = usuarioAutorizadoRepository;
    }

    @Override
    @Transactional
    public EncuestaDto crear(EncuestaDto dto) {
        Producto producto = buscarProducto(dto.getProductoId());
        UsuarioAutorizado usuario = buscarUsuario(dto.getCreadoPorId());

        Encuesta encuesta = new Encuesta();
        encuesta.setTitulo(dto.getTitulo());
        encuesta.setDescripcion(dto.getDescripcion());
        encuesta.setFechaInicio(dto.getFechaInicio() != null ? dto.getFechaInicio() : LocalDate.now());
        encuesta.setProducto(producto);
        encuesta.setCreadoPor(usuario);
        return mapToDto(encuestaRepository.save(encuesta));
    }

    @Override
    @Transactional
    public EncuestaDto actualizar(UUID id, EncuestaDto dto) {
        Encuesta encuesta = buscarPorId(id);
        encuesta.setTitulo(dto.getTitulo());
        encuesta.setDescripcion(dto.getDescripcion());
        encuesta.setFechaInicio(dto.getFechaInicio());
        if (dto.getProductoId() != null && (encuesta.getProducto() == null || !encuesta.getProducto().getId().equals(dto.getProductoId()))) {
            encuesta.setProducto(buscarProducto(dto.getProductoId()));
        }
        if (dto.getCreadoPorId() != null && (encuesta.getCreadoPor() == null || !encuesta.getCreadoPor().getId().equals(dto.getCreadoPorId()))) {
            encuesta.setCreadoPor(buscarUsuario(dto.getCreadoPorId()));
        }
        return mapToDto(encuesta);
    }

    @Override
    public EncuestaDto obtenerPorId(UUID id) {
        return mapToDto(buscarPorId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public EncuestaCompletaDto obtenerCompleta(UUID id) {
        Encuesta encuesta = buscarPorId(id);
        
        // Mapear producto
        EncuestaCompletaDto.ProductoSimpleDto productoDto = null;
        if (encuesta.getProducto() != null) {
            productoDto = new EncuestaCompletaDto.ProductoSimpleDto(
                encuesta.getProducto().getId(),
                encuesta.getProducto().getNombre()
            );
        }
        
        // Mapear preguntas con opciones
        List<EncuestaCompletaDto.PreguntaCompletaDto> preguntasDto = encuesta.getPreguntas().stream()
            .map(p -> {
                List<EncuestaCompletaDto.OpcionRespuestaSimpleDto> opcionesDto = p.getOpciones().stream()
                    .map(o -> new EncuestaCompletaDto.OpcionRespuestaSimpleDto(
                        o.getId(),
                        o.getTexto(),
                        o.getValor(),
                        o.getOrden()
                    ))
                    .collect(Collectors.toList());
                
                return new EncuestaCompletaDto.PreguntaCompletaDto(
                    p.getId(),
                    p.getTexto(),
                    p.getTipoPregunta().toString(),
                    p.isObligatoria(),
                    p.getOrden(),
                    opcionesDto
                );
            })
            .collect(Collectors.toList());
        
        return new EncuestaCompletaDto(
            encuesta.getId(),
            encuesta.getTitulo(),
            encuesta.getDescripcion(),
            encuesta.getFechaInicio(),
            productoDto,
            preguntasDto
        );
    }

    @Override
    public List<EncuestaDto> listarTodas() {
        return encuestaRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EncuestaDto> listarPorProducto(UUID productoId) {
        return encuestaRepository.findByProducto_Id(productoId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EncuestaDto> listarPorCreador(UUID usuarioId) {
        return encuestaRepository.findByCreadoPor_Id(usuarioId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminar(UUID id) {
        Encuesta encuesta = buscarPorId(id);
        encuestaRepository.delete(encuesta);
    }

    private Encuesta buscarPorId(UUID id) {
        return encuestaRepository.findById(id)
                .orElseThrow(() -> new CustomException("Encuesta no encontrada"));
    }

    private Producto buscarProducto(UUID id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new CustomException("Producto no encontrado"));
    }

    private UsuarioAutorizado buscarUsuario(UUID id) {
        return usuarioAutorizadoRepository.findById(id)
                .orElseThrow(() -> new CustomException("Usuario autorizado no encontrado"));
    }

    private EncuestaDto mapToDto(Encuesta encuesta) {
        return new EncuestaDto(
                encuesta.getId(),
                encuesta.getTitulo(),
                encuesta.getDescripcion(),
                encuesta.getFechaInicio(),
                encuesta.getProducto() != null ? encuesta.getProducto().getId() : null,
                encuesta.getCreadoPor() != null ? encuesta.getCreadoPor().getId() : null
        );
    }
}
