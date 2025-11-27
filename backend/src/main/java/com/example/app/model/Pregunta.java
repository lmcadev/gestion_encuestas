package com.example.app.model;

import com.example.app.model.enums.TipoPregunta;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Pregunta incluida en una encuesta.
 */
@Entity
@Table(name = "preguntas")
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_pregunta")
    private UUID id;

    @Column(nullable = false)
    private String texto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pregunta", nullable = false)
    private TipoPregunta tipoPregunta;

    @Column(nullable = false)
    private int orden;

    @Column(nullable = false)
    private boolean obligatoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encuesta_id")
    private Encuesta encuesta;

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpcionRespuesta> opciones = new ArrayList<>();

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RespuestaEncuesta> respuestas = new ArrayList<>();

    public Pregunta() {
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

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public boolean isObligatoria() {
        return obligatoria;
    }

    public void setObligatoria(boolean obligatoria) {
        this.obligatoria = obligatoria;
    }

    public Encuesta getEncuesta() {
        return encuesta;
    }

    public void setEncuesta(Encuesta encuesta) {
        this.encuesta = encuesta;
    }

    public List<OpcionRespuesta> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<OpcionRespuesta> opciones) {
        this.opciones = opciones;
    }

    public List<RespuestaEncuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<RespuestaEncuesta> respuestas) {
        this.respuestas = respuestas;
    }
}
