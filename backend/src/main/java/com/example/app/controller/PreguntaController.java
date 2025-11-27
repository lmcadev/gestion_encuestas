package com.example.app.controller;

import com.example.app.dto.PreguntaDto;
import com.example.app.service.PreguntaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/preguntas")
public class PreguntaController {

    private final PreguntaService preguntaService;

    public PreguntaController(PreguntaService preguntaService) {
        this.preguntaService = preguntaService;
    }

    @PostMapping
    public ResponseEntity<PreguntaDto> crear(@Valid @RequestBody PreguntaDto dto) {
        return ResponseEntity.ok(preguntaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PreguntaDto> actualizar(@PathVariable UUID id, @Valid @RequestBody PreguntaDto dto) {
        return ResponseEntity.ok(preguntaService.actualizar(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PreguntaDto> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(preguntaService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<PreguntaDto>> listarTodas() {
        return ResponseEntity.ok(preguntaService.listarTodas());
    }

    @GetMapping("/encuesta/{encuestaId}")
    public ResponseEntity<List<PreguntaDto>> listarPorEncuesta(@PathVariable UUID encuestaId) {
        return ResponseEntity.ok(preguntaService.listarPorEncuesta(encuestaId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        preguntaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
