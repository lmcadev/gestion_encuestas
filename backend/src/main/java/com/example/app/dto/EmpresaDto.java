package com.example.app.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

/**
 * DTO para trasladar datos de empresa.
 */
public class EmpresaDto {
    private UUID id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String nit;

    @NotBlank
    private String direccion;

    @NotBlank
    private String telefono;

    public EmpresaDto() {
    }

    public EmpresaDto(UUID id, String nombre, String nit, String direccion, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
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

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
