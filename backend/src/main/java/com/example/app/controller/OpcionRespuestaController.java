package com.example.app.controller;

import com.example.app.dto.OpcionRespuestaDto;
import com.example.app.service.OpcionRespuestaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/opciones-respuesta")
public class OpcionRespuestaController {

    private final OpcionRespuestaService opcionRespuestaService;

    public OpcionRespuestaController(OpcionRespuestaService opcionRespuestaService) {
        this.opcionRespuestaService = opcionRespuestaService;
    }

    @PostMapping
    public ResponseEntity<OpcionRespuestaDto> crear(@Valid @RequestBody OpcionRespuestaDto dto) {
        return ResponseEntity.ok(opcionRespuestaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OpcionRespuestaDto> actualizar(@PathVariable UUID id, @Valid @RequestBody OpcionRespuestaDto dto) {
        return ResponseEntity.ok(opcionRespuestaService.actualizar(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OpcionRespuestaDto> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(opcionRespuestaService.obtenerPorId(id));
    }

    @GetMapping("/pregunta/{preguntaId}")
    public ResponseEntity<List<OpcionRespuestaDto>> listarPorPregunta(@PathVariable UUID preguntaId) {
        return ResponseEntity.ok(opcionRespuestaService.listarPorPregunta(preguntaId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        opcionRespuestaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
