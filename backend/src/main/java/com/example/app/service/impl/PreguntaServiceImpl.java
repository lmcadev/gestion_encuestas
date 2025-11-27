package com.example.app.service.impl;

import com.example.app.dto.PreguntaDto;
import com.example.app.exception.CustomException;
import com.example.app.model.Encuesta;
import com.example.app.model.Pregunta;
import com.example.app.repository.EncuestaRepository;
import com.example.app.repository.PreguntaRepository;
import com.example.app.service.PreguntaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PreguntaServiceImpl implements PreguntaService {

    private final PreguntaRepository preguntaRepository;
    private final EncuestaRepository encuestaRepository;

    public PreguntaServiceImpl(PreguntaRepository preguntaRepository, EncuestaRepository encuestaRepository) {
        this.preguntaRepository = preguntaRepository;
        this.encuestaRepository = encuestaRepository;
    }

    @Override
    @Transactional
    public PreguntaDto crear(PreguntaDto dto) {
        Encuesta encuesta = buscarEncuesta(dto.getEncuestaId());
        Pregunta pregunta = new Pregunta();
        pregunta.setTexto(dto.getTexto());
        pregunta.setTipoPregunta(dto.getTipoPregunta());
        pregunta.setOrden(dto.getOrden());
        pregunta.setObligatoria(Boolean.TRUE.equals(dto.getObligatoria()));
        pregunta.setEncuesta(encuesta);
        return mapToDto(preguntaRepository.save(pregunta));
    }

    @Override
    @Transactional
    public PreguntaDto actualizar(UUID id, PreguntaDto dto) {
        Pregunta pregunta = buscarPorId(id);
        pregunta.setTexto(dto.getTexto());
        pregunta.setTipoPregunta(dto.getTipoPregunta());
        pregunta.setOrden(dto.getOrden());
        pregunta.setObligatoria(Boolean.TRUE.equals(dto.getObligatoria()));
        if (dto.getEncuestaId() != null && (pregunta.getEncuesta() == null || !pregunta.getEncuesta().getId().equals(dto.getEncuestaId()))) {
            pregunta.setEncuesta(buscarEncuesta(dto.getEncuestaId()));
        }
        return mapToDto(pregunta);
    }

    @Override
    public PreguntaDto obtenerPorId(UUID id) {
        return mapToDto(buscarPorId(id));
    }

    @Override
    public List<PreguntaDto> listarTodas() {
        return preguntaRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PreguntaDto> listarPorEncuesta(UUID encuestaId) {
        return preguntaRepository.findByEncuesta_IdOrderByOrdenAsc(encuestaId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminar(UUID id) {
        Pregunta pregunta = buscarPorId(id);
        preguntaRepository.delete(pregunta);
    }

    private Pregunta buscarPorId(UUID id) {
        return preguntaRepository.findById(id)
                .orElseThrow(() -> new CustomException("Pregunta no encontrada"));
    }

    private Encuesta buscarEncuesta(UUID id) {
        return encuestaRepository.findById(id)
                .orElseThrow(() -> new CustomException("Encuesta no encontrada"));
    }

    private PreguntaDto mapToDto(Pregunta pregunta) {
        PreguntaDto.EncuestaSimpleDto encuestaDto = null;
        if (pregunta.getEncuesta() != null) {
            encuestaDto = new PreguntaDto.EncuestaSimpleDto(
                    pregunta.getEncuesta().getId(),
                    pregunta.getEncuesta().getTitulo()
            );
        }
        
        return new PreguntaDto(
                pregunta.getId(),
                pregunta.getTexto(),
                pregunta.getTipoPregunta(),
                pregunta.getOrden(),
                pregunta.isObligatoria(),
                pregunta.getEncuesta() != null ? pregunta.getEncuesta().getId() : null,
                encuestaDto
        );
    }
}
