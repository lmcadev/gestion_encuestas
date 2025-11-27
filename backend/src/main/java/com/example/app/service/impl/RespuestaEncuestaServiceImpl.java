package com.example.app.service.impl;

import com.example.app.dto.RespuestaEncuestaDto;
import com.example.app.exception.CustomException;
import com.example.app.model.ClienteResponde;
import com.example.app.model.Encuesta;
import com.example.app.model.Pregunta;
import com.example.app.model.RespuestaEncuesta;
import com.example.app.repository.ClienteRespondeRepository;
import com.example.app.repository.EncuestaRepository;
import com.example.app.repository.PreguntaRepository;
import com.example.app.repository.RespuestaEncuestaRepository;
import com.example.app.service.RespuestaEncuestaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RespuestaEncuestaServiceImpl implements RespuestaEncuestaService {

    private final RespuestaEncuestaRepository respuestaEncuestaRepository;
    private final ClienteRespondeRepository clienteRespondeRepository;
    private final EncuestaRepository encuestaRepository;
    private final PreguntaRepository preguntaRepository;

    public RespuestaEncuestaServiceImpl(RespuestaEncuestaRepository respuestaEncuestaRepository,
                                        ClienteRespondeRepository clienteRespondeRepository,
                                        EncuestaRepository encuestaRepository,
                                        PreguntaRepository preguntaRepository) {
        this.respuestaEncuestaRepository = respuestaEncuestaRepository;
        this.clienteRespondeRepository = clienteRespondeRepository;
        this.encuestaRepository = encuestaRepository;
        this.preguntaRepository = preguntaRepository;
    }

    @Override
    @Transactional
    public RespuestaEncuestaDto crear(RespuestaEncuestaDto dto) {
        ClienteResponde cliente = buscarCliente(dto.getClienteId());
        Encuesta encuesta = buscarEncuesta(dto.getEncuestaId());
        Pregunta pregunta = buscarPregunta(dto.getPreguntaId());

        RespuestaEncuesta respuesta = new RespuestaEncuesta();
        respuesta.setCliente(cliente);
        respuesta.setEncuesta(encuesta);
        respuesta.setPregunta(pregunta);
        respuesta.setFecha(dto.getFecha() != null ? dto.getFecha() : LocalDate.now());
        respuesta.setValor(dto.getValor());
        return mapToDto(respuestaEncuestaRepository.save(respuesta));
    }

    @Override
    public List<RespuestaEncuestaDto> listarTodas() {
        return respuestaEncuestaRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RespuestaEncuestaDto> listarPorEncuesta(UUID encuestaId) {
        return respuestaEncuestaRepository.findByEncuesta_Id(encuestaId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RespuestaEncuestaDto> listarPorPregunta(UUID preguntaId) {
        return respuestaEncuestaRepository.findByPregunta_Id(preguntaId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RespuestaEncuestaDto> listarPorCliente(UUID clienteId) {
        return respuestaEncuestaRepository.findByCliente_Id(clienteId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ClienteResponde buscarCliente(UUID id) {
        return clienteRespondeRepository.findById(id)
                .orElseThrow(() -> new CustomException("Cliente no encontrado"));
    }

    private Encuesta buscarEncuesta(UUID id) {
        return encuestaRepository.findById(id)
                .orElseThrow(() -> new CustomException("Encuesta no encontrada"));
    }

    private Pregunta buscarPregunta(UUID id) {
        return preguntaRepository.findById(id)
                .orElseThrow(() -> new CustomException("Pregunta no encontrada"));
    }

    private RespuestaEncuestaDto mapToDto(RespuestaEncuesta respuesta) {
        return new RespuestaEncuestaDto(
                respuesta.getId(),
                respuesta.getCliente() != null ? respuesta.getCliente().getId() : null,
                respuesta.getEncuesta() != null ? respuesta.getEncuesta().getId() : null,
                respuesta.getPregunta() != null ? respuesta.getPregunta().getId() : null,
                respuesta.getFecha(),
                respuesta.getValor(),
                respuesta.getCliente() != null ? respuesta.getCliente().getNombre() : null,
                respuesta.getPregunta() != null ? respuesta.getPregunta().getTexto() : null,
                respuesta.getEncuesta() != null ? respuesta.getEncuesta().getTitulo() : null
        );
    }
}
