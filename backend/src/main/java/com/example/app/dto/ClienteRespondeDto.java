package com.example.app.dto;

import com.example.app.model.enums.CanalOrigen;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

/**
 * DTO para los clientes que responden encuestas.
 */
public class ClienteRespondeDto {
    private UUID id;

    @NotBlank
    private String nombre;

    @Email
    private String email;

    private String telefono;

    private CanalOrigen canalOrigen;

    public ClienteRespondeDto() {
    }

    public ClienteRespondeDto(UUID id, String nombre, String email, String telefono, CanalOrigen canalOrigen) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.canalOrigen = canalOrigen;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public CanalOrigen getCanalOrigen() {
        return canalOrigen;
    }

    public void setCanalOrigen(CanalOrigen canalOrigen) {
        this.canalOrigen = canalOrigen;
    }
}
