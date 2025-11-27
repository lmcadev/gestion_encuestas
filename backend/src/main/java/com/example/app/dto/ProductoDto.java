package com.example.app.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

/**
 * DTO para productos.
 */
public class ProductoDto {
    private UUID id;

    @NotBlank
    private String nombre;

    public ProductoDto() {
    }

    public ProductoDto(UUID id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
