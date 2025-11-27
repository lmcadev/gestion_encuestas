package com.example.app.controller;

import com.example.app.dto.EmpresaDto;
import com.example.app.service.EmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/empresas")
@Tag(name = "Empresas", description = "Gestión de empresas del sistema")
@SecurityRequirement(name = "Bearer Authentication")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    @Operation(summary = "Crear empresa", description = "Crea una nueva empresa en el sistema")
    public ResponseEntity<EmpresaDto> crear(@Valid @RequestBody EmpresaDto dto) {
        return ResponseEntity.ok(empresaService.crear(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar empresa", description = "Actualiza los datos de una empresa existente")
    public ResponseEntity<EmpresaDto> actualizar(@PathVariable UUID id, @Valid @RequestBody EmpresaDto dto) {
        return ResponseEntity.ok(empresaService.actualizar(id, dto));
    }

    @GetMapping
    @Operation(summary = "Listar empresas", description = "Obtiene la lista de todas las empresas")
    public ResponseEntity<List<EmpresaDto>> listar() {
        return ResponseEntity.ok(empresaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener empresa", description = "Obtiene los detalles de una empresa por su ID")
    public ResponseEntity<EmpresaDto> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(empresaService.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar empresa", description = "Elimina una empresa del sistema")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        empresaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
