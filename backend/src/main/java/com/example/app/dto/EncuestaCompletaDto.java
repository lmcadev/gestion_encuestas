package com.example.app.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class EncuestaCompletaDto {
    private UUID id;
    private String titulo;
    private String descripcion;
    private LocalDate fechaInicio;
    private ProductoSimpleDto producto;
    private List<PreguntaCompletaDto> preguntas;

    public EncuestaCompletaDto() {
    }

    public EncuestaCompletaDto(UUID id, String titulo, String descripcion, LocalDate fechaInicio,
                                ProductoSimpleDto producto, List<PreguntaCompletaDto> preguntas) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.producto = producto;
        this.preguntas = preguntas;
    }

    // Getters y Setters
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public ProductoSimpleDto getProducto() {
        return producto;
    }

    public void setProducto(ProductoSimpleDto producto) {
        this.producto = producto;
    }

    public List<PreguntaCompletaDto> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<PreguntaCompletaDto> preguntas) {
        this.preguntas = preguntas;
    }

    // DTO interno para producto
    public static class ProductoSimpleDto {
        private UUID id;
        private String nombre;

        public ProductoSimpleDto() {
        }

        public ProductoSimpleDto(UUID id, String nombre) {
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

    // DTO interno para pregunta
    public static class PreguntaCompletaDto {
        private UUID id;
        private String texto;
        private String tipoPregunta;
        private Boolean obligatoria;
        private Integer orden;
        private List<OpcionRespuestaSimpleDto> opciones;

        public PreguntaCompletaDto() {
        }

        public PreguntaCompletaDto(UUID id, String texto, String tipoPregunta, Boolean obligatoria,
                                   Integer orden, List<OpcionRespuestaSimpleDto> opciones) {
            this.id = id;
            this.texto = texto;
            this.tipoPregunta = tipoPregunta;
            this.obligatoria = obligatoria;
            this.orden = orden;
            this.opciones = opciones;
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

        public String getTipoPregunta() {
            return tipoPregunta;
        }

        public void setTipoPregunta(String tipoPregunta) {
            this.tipoPregunta = tipoPregunta;
        }

        public Boolean getObligatoria() {
            return obligatoria;
        }

        public void setObligatoria(Boolean obligatoria) {
            this.obligatoria = obligatoria;
        }

        public Integer getOrden() {
            return orden;
        }

        public void setOrden(String orden) {
            this.orden = Integer.valueOf(orden);
        }

        public List<OpcionRespuestaSimpleDto> getOpciones() {
            return opciones;
        }

        public void setOpciones(List<OpcionRespuestaSimpleDto> opciones) {
            this.opciones = opciones;
        }
    }

    // DTO interno para opción de respuesta
    public static class OpcionRespuestaSimpleDto {
        private UUID id;
        private String texto;
        private Integer valor;
        private Integer orden;

        public OpcionRespuestaSimpleDto() {
        }

        public OpcionRespuestaSimpleDto(UUID id, String texto, Integer valor, Integer orden) {
            this.id = id;
            this.texto = texto;
            this.valor = valor;
            this.orden = orden;
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
    }
}
