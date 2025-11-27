package com.example.app.service.impl;

import com.example.app.dto.EstadisticasDto;
import com.example.app.model.*;
import com.example.app.repository.*;
import com.example.app.service.EstadisticasService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EstadisticasServiceImpl implements EstadisticasService {

    private static final Logger logger = LoggerFactory.getLogger(EstadisticasServiceImpl.class);

    private final EncuestaRepository encuestaRepository;
    private final ClienteRespondeRepository clienteRespondeRepository;
    private final RespuestaEncuestaRepository respuestaEncuestaRepository;
    private final ProductoRepository productoRepository;
    private final PreguntaRepository preguntaRepository;

    public EstadisticasServiceImpl(
            EncuestaRepository encuestaRepository,
            ClienteRespondeRepository clienteRespondeRepository,
            RespuestaEncuestaRepository respuestaEncuestaRepository,
            ProductoRepository productoRepository,
            PreguntaRepository preguntaRepository) {
        this.encuestaRepository = encuestaRepository;
        this.clienteRespondeRepository = clienteRespondeRepository;
        this.respuestaEncuestaRepository = respuestaEncuestaRepository;
        this.productoRepository = productoRepository;
        this.preguntaRepository = preguntaRepository;
    }

    @Override
    public EstadisticasDto obtenerEstadisticas() {
        // KPIs principales
        EstadisticasDto.KpiDto kpis = calcularKpis();

        // Satisfacción por producto
        List<EstadisticasDto.SatisfaccionProductoDto> satisfaccionPorProducto = calcularSatisfaccionPorProducto();

        // Aspectos mejor calificados
        List<EstadisticasDto.AspectoCalificadoDto> aspectosMejorCalificados = calcularAspectosMejorCalificados();

        // Comentarios recientes
        List<EstadisticasDto.ComentarioRecienteDto> comentariosRecientes = obtenerComentariosRecientes();

        return new EstadisticasDto(kpis, satisfaccionPorProducto, aspectosMejorCalificados, comentariosRecientes);
    }

    private EstadisticasDto.KpiDto calcularKpis() {
        // Total de encuestas únicas respondidas
        Long totalClientes = clienteRespondeRepository.count();
        
        // Satisfacción promedio (preguntas con calificación numérica)
        List<RespuestaEncuesta> todasRespuestas = respuestaEncuestaRepository.findAll();
        Double satisfaccionPromedio = todasRespuestas.stream()
            .filter(r -> r.getValor() != null)
            .map(r -> {
                try {
                    return Double.parseDouble(r.getValor());
                } catch (NumberFormatException e) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .filter(v -> v >= 1.0 && v <= 5.0) // Solo valores de 1-5
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);

        // Tasa de recomendación (porcentaje de respuestas >= 4)
        long totalCalificaciones = todasRespuestas.stream()
            .map(r -> {
                try {
                    return Double.parseDouble(r.getValor());
                } catch (NumberFormatException e) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .filter(v -> v >= 1.0 && v <= 5.0)
            .count();

        long recomendaciones = todasRespuestas.stream()
            .map(r -> {
                try {
                    return Double.parseDouble(r.getValor());
                } catch (NumberFormatException e) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .filter(v -> v >= 4.0)
            .count();

        Double tasaRecomendacion = totalCalificaciones > 0 
            ? (recomendaciones * 100.0 / totalCalificaciones) 
            : 0.0;

        // Tasa de completitud (porcentaje de respuestas completadas)
        long totalPreguntasEsperadas = clienteRespondeRepository.count() * 
            preguntaRepository.count();
        long respuestasCompletadas = respuestaEncuestaRepository.count();
        
        Double tasaCompletitud = totalPreguntasEsperadas > 0 
            ? (respuestasCompletadas * 100.0 / totalPreguntasEsperadas) 
            : 0.0;

        return new EstadisticasDto.KpiDto(
            totalClientes,
            Math.round(satisfaccionPromedio * 10.0) / 10.0,
            Math.round(tasaRecomendacion * 10.0) / 10.0,
            Math.round(tasaCompletitud * 10.0) / 10.0
        );
    }

    private List<EstadisticasDto.SatisfaccionProductoDto> calcularSatisfaccionPorProducto() {
        List<Producto> productos = productoRepository.findAll();
        
        logger.info("Calculando satisfacción para {} productos", productos.size());
        
        return productos.stream()
            .map(producto -> {
                // Obtener encuestas del producto
                List<Encuesta> encuestas = encuestaRepository.findByProducto_Id(producto.getId());
                
                logger.info("Producto: {} - Encuestas: {}", producto.getNombre(), encuestas.size());
                
                // Contar respuestas
                long totalRespuestas = encuestas.stream()
                    .flatMap(e -> {
                        List<RespuestaEncuesta> respuestas = respuestaEncuestaRepository.findByEncuesta_Id(e.getId());
                        logger.info("  Encuesta {} tiene {} respuestas", e.getId(), respuestas.size());
                        return respuestas.stream();
                    })
                    .count();

                logger.info("  Total respuestas para {}: {}", producto.getNombre(), totalRespuestas);

                // Calcular promedio
                Double promedio = encuestas.stream()
                    .flatMap(e -> respuestaEncuestaRepository.findByEncuesta_Id(e.getId()).stream())
                    .map(r -> {
                        try {
                            return Double.parseDouble(r.getValor());
                        } catch (NumberFormatException ex) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .filter(v -> v >= 1.0 && v <= 5.0)
                    .mapToDouble(v -> v)
                    .average()
                    .orElse(0.0);

                return new EstadisticasDto.SatisfaccionProductoDto(
                    producto.getNombre(),
                    totalRespuestas,
                    Math.round(promedio * 10.0) / 10.0
                );
            })
            .sorted((a, b) -> Double.compare(b.getPromedio(), a.getPromedio()))
            .limit(5)
            .collect(Collectors.toList());
    }

    private List<EstadisticasDto.AspectoCalificadoDto> calcularAspectosMejorCalificados() {
        List<Pregunta> preguntas = preguntaRepository.findAll();
        
        return preguntas.stream()
            .map(pregunta -> {
                List<RespuestaEncuesta> respuestas = respuestaEncuestaRepository.findByPregunta_Id(pregunta.getId());
                
                long totalRespuestas = respuestas.size();

                Double promedio = respuestas.stream()
                    .map(r -> {
                        try {
                            return Double.parseDouble(r.getValor());
                        } catch (NumberFormatException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .filter(v -> v >= 1.0 && v <= 5.0)
                    .mapToDouble(v -> v)
                    .average()
                    .orElse(0.0);

                return new EstadisticasDto.AspectoCalificadoDto(
                    pregunta.getTexto(),
                    totalRespuestas,
                    Math.round(promedio * 10.0) / 10.0
                );
            })
            .filter(a -> a.getRespuestas() > 0)
            .sorted((a, b) -> Double.compare(b.getPromedio(), a.getPromedio()))
            .limit(5)
            .collect(Collectors.toList());
    }

    private List<EstadisticasDto.ComentarioRecienteDto> obtenerComentariosRecientes() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        
        // Obtener respuestas de texto más recientes
        List<RespuestaEncuesta> respuestasTexto = respuestaEncuestaRepository.findAll().stream()
            .filter(r -> {
                // Filtrar respuestas que sean texto (no números)
                try {
                    Double.parseDouble(r.getValor());
                    return false;
                } catch (NumberFormatException e) {
                    return r.getValor() != null && r.getValor().length() > 10;
                }
            })
            .sorted((a, b) -> b.getId().compareTo(a.getId())) // Ordenar por ID descendente (más recientes)
            .limit(3)
            .collect(Collectors.toList());

        return respuestasTexto.stream()
            .map(respuesta -> {
                String nombreCliente = respuesta.getCliente() != null 
                    ? respuesta.getCliente().getNombre() 
                    : "Usuario";
                
                String fecha = LocalDateTime.now().format(formatter);

                return new EstadisticasDto.ComentarioRecienteDto(
                    nombreCliente,
                    fecha,
                    respuesta.getValor()
                );
            })
            .collect(Collectors.toList());
    }
}
