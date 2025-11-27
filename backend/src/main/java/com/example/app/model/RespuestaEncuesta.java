package com.example.app.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Respuesta capturada para una pregunta de una encuesta.
 */
@Entity
@Table(name = "respuestas_encuesta")
public class RespuestaEncuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_respuesta")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private ClienteResponde cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encuesta_id")
    private Encuesta encuesta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pregunta_id")
    private Pregunta pregunta;

    @Column(name = "fecha_respuesta")
    private LocalDate fecha;

    /**
     * Valor capturado de la respuesta (texto libre o identificador de opción).
     */
    @Column(length = 1000)
    private String valor;

    public RespuestaEncuesta() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ClienteResponde getCliente() {
        return cliente;
    }

    public void setCliente(ClienteResponde cliente) {
        this.cliente = cliente;
    }

    public Encuesta getEncuesta() {
        return encuesta;
    }

    public void setEncuesta(Encuesta encuesta) {
        this.encuesta = encuesta;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
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
}
