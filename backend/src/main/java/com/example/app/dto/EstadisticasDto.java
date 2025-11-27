package com.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasDto {
    private KpiDto kpis;
    private List<SatisfaccionProductoDto> satisfaccionPorProducto;
    private List<AspectoCalificadoDto> aspectosMejorCalificados;
    private List<ComentarioRecienteDto> comentariosRecientes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KpiDto {
        private Long totalEncuestas;
        private Double satisfaccionPromedio;
        private Double tasaRecomendacion;
        private Double tasaCompletitud;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SatisfaccionProductoDto {
        private String producto;
        private Long respuestas;
        private Double promedio;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AspectoCalificadoDto {
        private String pregunta;
        private Long respuestas;
        private Double promedio;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComentarioRecienteDto {
        private String usuario;
        private String fecha;
        private String comentario;
    }
}
