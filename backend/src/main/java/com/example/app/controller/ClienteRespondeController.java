package com.example.app.controller;

import com.example.app.dto.ClienteRespondeDto;
import com.example.app.service.ClienteRespondeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRespondeController {

    private final ClienteRespondeService clienteRespondeService;

    public ClienteRespondeController(ClienteRespondeService clienteRespondeService) {
        this.clienteRespondeService = clienteRespondeService;
    }

    @PostMapping
    public ResponseEntity<ClienteRespondeDto> crear(@Valid @RequestBody ClienteRespondeDto dto) {
        return ResponseEntity.ok(clienteRespondeService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteRespondeDto> actualizar(@PathVariable UUID id, @Valid @RequestBody ClienteRespondeDto dto) {
        return ResponseEntity.ok(clienteRespondeService.actualizar(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<ClienteRespondeDto>> listar() {
        return ResponseEntity.ok(clienteRespondeService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteRespondeDto> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(clienteRespondeService.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        clienteRespondeService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
