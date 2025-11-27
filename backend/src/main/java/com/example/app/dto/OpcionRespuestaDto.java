package com.example.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO para opciones de respuesta.
 */
public class OpcionRespuestaDto {
    private UUID id;

    @NotBlank
    private String texto;

    private Integer valor;
    private Integer orden;

    @NotNull
    private UUID preguntaId;

    public OpcionRespuestaDto() {
    }

    public OpcionRespuestaDto(UUID id, String texto, Integer valor, Integer orden, UUID preguntaId) {
        this.id = id;
        this.texto = texto;
        this.valor = valor;
        this.orden = orden;
        this.preguntaId = preguntaId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public UUID getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(UUID preguntaId) {
        this.preguntaId = preguntaId;
    }
}
