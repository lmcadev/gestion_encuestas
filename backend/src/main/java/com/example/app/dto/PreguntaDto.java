package com.example.app.dto;

import com.example.app.model.enums.TipoPregunta;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO para preguntas.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PreguntaDto {
    private UUID id;

    @NotBlank
    private String texto;

    @NotNull
    private TipoPregunta tipoPregunta;

    @NotNull
    private Integer orden;

    @NotNull
    private Boolean obligatoria;

    @NotNull
    private UUID encuestaId;
    
    private EncuestaSimpleDto encuesta;

    public PreguntaDto() {
    }

    public PreguntaDto(UUID id, String texto, TipoPregunta tipoPregunta, Integer orden, Boolean obligatoria, UUID encuestaId) {
        this.id = id;
        this.texto = texto;
        this.tipoPregunta = tipoPregunta;
        this.orden = orden;
        this.obligatoria = obligatoria;
        this.encuestaId = encuestaId;
    }
    
    public PreguntaDto(UUID id, String texto, TipoPregunta tipoPregunta, Integer orden, Boolean obligatoria, UUID encuestaId, EncuestaSimpleDto encuesta) {
        this.id = id;
        this.texto = texto;
        this.tipoPregunta = tipoPregunta;
        this.orden = orden;
        this.obligatoria = obligatoria;
        this.encuestaId = encuestaId;
        this.encuesta = encuesta;
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

    public TipoPregunta getTipoPregunta() {
        return tipoPregunta;
    }

    public void setTipoPregunta(TipoPregunta tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Boolean getObligatoria() {
        return obligatoria;
    }

    public void setObligatoria(Boolean obligatoria) {
        this.obligatoria = obligatoria;
    }

    public UUID getEncuestaId() {
        return encuestaId;
    }

    public void setEncuestaId(UUID encuestaId) {
        this.encuestaId = encuestaId;
    }

    public EncuestaSimpleDto getEncuesta() {
        return encuesta;
    }

    public void setEncuesta(EncuestaSimpleDto encuesta) {
        this.encuesta = encuesta;
    }
    
    public static class EncuestaSimpleDto {
        private UUID id;
        private String titulo;
        
        public EncuestaSimpleDto() {}
        
        public EncuestaSimpleDto(UUID id, String titulo) {
            this.id = id;
            this.titulo = titulo;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }
    }
}
