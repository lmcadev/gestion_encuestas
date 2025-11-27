package com.example.app.dto;

import com.example.app.model.enums.EstadoUsuario;
import com.example.app.model.enums.RolUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO para usuarios de negocio autorizados.
 */
public class UsuarioAutorizadoDto {
    private UUID id;

    @NotBlank
    private String nombre;

    @Email
    @NotBlank
    private String email;

    private String clave;

    @NotNull
    private RolUsuario rol;

    @NotNull
    private EstadoUsuario estado;

    @NotNull
    private UUID empresaId;

    public UsuarioAutorizadoDto() {
    }

    public UsuarioAutorizadoDto(UUID id, String nombre, String email, String clave, RolUsuario rol, EstadoUsuario estado, UUID empresaId) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.clave = clave;
        this.rol = rol;
        this.estado = estado;
        this.empresaId = empresaId;
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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }

    public UUID getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(UUID empresaId) {
        this.empresaId = empresaId;
    }
}
