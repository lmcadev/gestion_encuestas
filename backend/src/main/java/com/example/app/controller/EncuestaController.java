package com.example.app.controller;

import com.example.app.dto.EncuestaCompletaDto;
import com.example.app.dto.EncuestaDto;
import com.example.app.service.EncuestaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/encuestas")
public class EncuestaController {

    private final EncuestaService encuestaService;

    public EncuestaController(EncuestaService encuestaService) {
        this.encuestaService = encuestaService;
    }

    @PostMapping
    public ResponseEntity<EncuestaDto> crear(@Valid @RequestBody EncuestaDto dto) {
        return ResponseEntity.ok(encuestaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EncuestaDto> actualizar(@PathVariable UUID id, @Valid @RequestBody EncuestaDto dto) {
        return ResponseEntity.ok(encuestaService.actualizar(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<EncuestaDto>> listar() {
        return ResponseEntity.ok(encuestaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EncuestaDto> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(encuestaService.obtenerPorId(id));
    }

    @GetMapping("/{id}/completa")
    public ResponseEntity<EncuestaCompletaDto> obtenerCompleta(@PathVariable UUID id) {
        return ResponseEntity.ok(encuestaService.obtenerCompleta(id));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<EncuestaDto>> listarPorProducto(@PathVariable UUID productoId) {
        return ResponseEntity.ok(encuestaService.listarPorProducto(productoId));
    }

    @GetMapping("/creador/{usuarioId}")
    public ResponseEntity<List<EncuestaDto>> listarPorCreador(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(encuestaService.listarPorCreador(usuarioId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        encuestaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
