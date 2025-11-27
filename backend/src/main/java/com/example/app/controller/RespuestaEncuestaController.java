package com.example.app.controller;

import com.example.app.dto.RespuestaEncuestaDto;
import com.example.app.service.RespuestaEncuestaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/respuestas")
public class RespuestaEncuestaController {

    private final RespuestaEncuestaService respuestaEncuestaService;

    public RespuestaEncuestaController(RespuestaEncuestaService respuestaEncuestaService) {
        this.respuestaEncuestaService = respuestaEncuestaService;
    }

    @PostMapping
    public ResponseEntity<RespuestaEncuestaDto> crear(@Valid @RequestBody RespuestaEncuestaDto dto) {
        return ResponseEntity.ok(respuestaEncuestaService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<RespuestaEncuestaDto>> listarTodas() {
        return ResponseEntity.ok(respuestaEncuestaService.listarTodas());
    }

    @GetMapping("/encuesta/{encuestaId}")
    public ResponseEntity<List<RespuestaEncuestaDto>> listarPorEncuesta(@PathVariable UUID encuestaId) {
        return ResponseEntity.ok(respuestaEncuestaService.listarPorEncuesta(encuestaId));
    }

    @GetMapping("/pregunta/{preguntaId}")
    public ResponseEntity<List<RespuestaEncuestaDto>> listarPorPregunta(@PathVariable UUID preguntaId) {
        return ResponseEntity.ok(respuestaEncuestaService.listarPorPregunta(preguntaId));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<RespuestaEncuestaDto>> listarPorCliente(@PathVariable UUID clienteId) {
        return ResponseEntity.ok(respuestaEncuestaService.listarPorCliente(clienteId));
    }
}
