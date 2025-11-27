package com.example.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO para registrar respuestas de encuestas.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RespuestaEncuestaDto {
    private UUID id;

    @NotNull
    private UUID clienteId;

    @NotNull
    private UUID encuestaId;

    @NotNull
    private UUID preguntaId;

    private LocalDate fecha;
    private String valor;
    
    // Información adicional para visualización
    private String clienteNombre;
    private String preguntaTexto;
    private String encuestaTitulo;

    public RespuestaEncuestaDto() {
    }

    public RespuestaEncuestaDto(UUID id, UUID clienteId, UUID encuestaId, UUID preguntaId, LocalDate fecha, String valor) {
        this.id = id;
        this.clienteId = clienteId;
        this.encuestaId = encuestaId;
        this.preguntaId = preguntaId;
        this.fecha = fecha;
        this.valor = valor;
    }
    
    public RespuestaEncuestaDto(UUID id, UUID clienteId, UUID encuestaId, UUID preguntaId, LocalDate fecha, String valor,
                                String clienteNombre, String preguntaTexto, String encuestaTitulo) {
        this.id = id;
        this.clienteId = clienteId;
        this.encuestaId = encuestaId;
        this.preguntaId = preguntaId;
        this.fecha = fecha;
        this.valor = valor;
        this.clienteNombre = clienteNombre;
        this.preguntaTexto = preguntaTexto;
        this.encuestaTitulo = encuestaTitulo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public void setClienteId(UUID clienteId) {
        this.clienteId = clienteId;
    }

    public UUID getEncuestaId() {
        return encuestaId;
    }

    public void setEncuestaId(UUID encuestaId) {
        this.encuestaId = encuestaId;
    }

    public UUID getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(UUID preguntaId) {
        this.preguntaId = preguntaId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getPreguntaTexto() {
        return preguntaTexto;
    }

    public void setPreguntaTexto(String preguntaTexto) {
        this.preguntaTexto = preguntaTexto;
    }

    public String getEncuestaTitulo() {
        return encuestaTitulo;
    }

    public void setEncuestaTitulo(String encuestaTitulo) {
        this.encuestaTitulo = encuestaTitulo;
    }
}
