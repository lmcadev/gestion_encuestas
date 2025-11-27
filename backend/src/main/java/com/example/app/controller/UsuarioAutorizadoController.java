package com.example.app.controller;

import com.example.app.dto.UsuarioAutorizadoDto;
import com.example.app.service.UsuarioAutorizadoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios-autorizados")
public class UsuarioAutorizadoController {

    private final UsuarioAutorizadoService usuarioAutorizadoService;

    public UsuarioAutorizadoController(UsuarioAutorizadoService usuarioAutorizadoService) {
        this.usuarioAutorizadoService = usuarioAutorizadoService;
    }

    @PostMapping
    public ResponseEntity<UsuarioAutorizadoDto> crear(@Valid @RequestBody UsuarioAutorizadoDto dto) {
        return ResponseEntity.ok(usuarioAutorizadoService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioAutorizadoDto> actualizar(@PathVariable UUID id, @Valid @RequestBody UsuarioAutorizadoDto dto) {
        return ResponseEntity.ok(usuarioAutorizadoService.actualizar(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioAutorizadoDto>> listar() {
        return ResponseEntity.ok(usuarioAutorizadoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioAutorizadoDto> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(usuarioAutorizadoService.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        usuarioAutorizadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
