package com.example.app.service.impl;

import com.example.app.dto.OpcionRespuestaDto;
import com.example.app.exception.CustomException;
import com.example.app.model.OpcionRespuesta;
import com.example.app.model.Pregunta;
import com.example.app.repository.OpcionRespuestaRepository;
import com.example.app.repository.PreguntaRepository;
import com.example.app.service.OpcionRespuestaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OpcionRespuestaServiceImpl implements OpcionRespuestaService {

    private final OpcionRespuestaRepository opcionRespuestaRepository;
    private final PreguntaRepository preguntaRepository;

    public OpcionRespuestaServiceImpl(OpcionRespuestaRepository opcionRespuestaRepository,
                                      PreguntaRepository preguntaRepository) {
        this.opcionRespuestaRepository = opcionRespuestaRepository;
        this.preguntaRepository = preguntaRepository;
    }

    @Override
    @Transactional
    public OpcionRespuestaDto crear(OpcionRespuestaDto dto) {
        Pregunta pregunta = buscarPregunta(dto.getPreguntaId());
        OpcionRespuesta opcion = new OpcionRespuesta();
        opcion.setTexto(dto.getTexto());
        opcion.setValor(dto.getValor());
        opcion.setOrden(dto.getOrden());
        opcion.setPregunta(pregunta);
        return mapToDto(opcionRespuestaRepository.save(opcion));
    }

    @Override
    @Transactional
    public OpcionRespuestaDto actualizar(UUID id, OpcionRespuestaDto dto) {
        OpcionRespuesta opcion = buscarPorId(id);
        opcion.setTexto(dto.getTexto());
        opcion.setValor(dto.getValor());
        opcion.setOrden(dto.getOrden());
        if (dto.getPreguntaId() != null && (opcion.getPregunta() == null || !opcion.getPregunta().getId().equals(dto.getPreguntaId()))) {
            opcion.setPregunta(buscarPregunta(dto.getPreguntaId()));
        }
        return mapToDto(opcion);
    }

    @Override
    public OpcionRespuestaDto obtenerPorId(UUID id) {
        return mapToDto(buscarPorId(id));
    }

    @Override
    public List<OpcionRespuestaDto> listarPorPregunta(UUID preguntaId) {
        return opcionRespuestaRepository.findByPregunta_IdOrderByOrdenAsc(preguntaId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminar(UUID id) {
        OpcionRespuesta opcion = buscarPorId(id);
        opcionRespuestaRepository.delete(opcion);
    }

    private OpcionRespuesta buscarPorId(UUID id) {
        return opcionRespuestaRepository.findById(id)
                .orElseThrow(() -> new CustomException("Opción de respuesta no encontrada"));
    }

    private Pregunta buscarPregunta(UUID id) {
        return preguntaRepository.findById(id)
                .orElseThrow(() -> new CustomException("Pregunta no encontrada"));
    }

    private OpcionRespuestaDto mapToDto(OpcionRespuesta opcion) {
        return new OpcionRespuestaDto(
                opcion.getId(),
                opcion.getTexto(),
                opcion.getValor(),
                opcion.getOrden(),
                opcion.getPregunta() != null ? opcion.getPregunta().getId() : null
        );
    }
}
