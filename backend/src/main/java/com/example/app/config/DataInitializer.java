package com.example.app.config;

import com.example.app.model.*;
import com.example.app.model.enums.*;
import com.example.app.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final EmpresaRepository empresaRepository;
    private final ProductoRepository productoRepository;
    private final EncuestaRepository encuestaRepository;
    private final PreguntaRepository preguntaRepository;
    private final OpcionRespuestaRepository opcionRespuestaRepository;
    private final UsuarioAutorizadoRepository usuarioAutorizadoRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            if (userRepository.count() > 0) {
                log.info("Base de datos ya contiene datos. Omitiendo inicialización.");
                return;
            }

            log.info("========================================");
            log.info("Iniciando población de base de datos...");
            log.info("========================================");

            // 1. Usuarios del sistema
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Arrays.asList("ROLE_ADMIN", "ROLE_USER"));
            userRepository.save(admin);

            User gerente = new User();
            gerente.setUsername("gerente");
            gerente.setPassword(passwordEncoder.encode("gerente123"));
            gerente.setRoles(Arrays.asList("ROLE_USER"));
            userRepository.save(gerente);

            log.info("✓ Usuarios: 2 usuarios creados");

            // 2. Empresas
            Empresa techStore = new Empresa();
            techStore.setNombre("TechStore Colombia");
            techStore.setNit("900123456-7");
            techStore.setDireccion("Calle 100 #15-20, Bogotá");
            techStore.setTelefono("+57 1 234 5678");
            empresaRepository.save(techStore);

            Empresa electronicosMax = new Empresa();
            electronicosMax.setNombre("Electrónicos MAX");
            electronicosMax.setNit("900234567-8");
            electronicosMax.setDireccion("Carrera 43A #14-109, Medellín");
            electronicosMax.setTelefono("+57 4 567 8901");
            empresaRepository.save(electronicosMax);

            Empresa digitalZone = new Empresa();
            digitalZone.setNombre("Digital Zone Cali");
            digitalZone.setNit("900345678-9");
            digitalZone.setDireccion("Avenida 6N #15N-23, Cali");
            digitalZone.setTelefono("+57 2 890 1234");
            empresaRepository.save(digitalZone);

            log.info("✓ Empresas: 3 tiendas creadas");

            // 3. Productos
            List<Producto> productos = new ArrayList<>();
            productos.add(crearProducto("Samsung Galaxy S24"));
            productos.add(crearProducto("iPhone 15 Pro"));
            productos.add(crearProducto("Xiaomi Redmi Note 13"));
            productos.add(crearProducto("MacBook Air M2"));
            productos.add(crearProducto("Lenovo ThinkPad X1"));
            productos.add(crearProducto("HP Pavilion Gaming"));
            productos.add(crearProducto("iPad Pro 12.9"));
            productos.add(crearProducto("Samsung Galaxy Tab S9"));
            productos.add(crearProducto("AirPods Pro"));
            productos.add(crearProducto("Sony WH-1000XM5"));
            productos.add(crearProducto("JBL Tune 770NC"));
            productos.add(crearProducto("Apple Watch Series 9"));
            productos.add(crearProducto("Samsung Galaxy Watch 6"));
            productos.add(crearProducto("Cargador Anker 65W"));
            productos.add(crearProducto("PowerBank Xiaomi 20000mAh"));
            productoRepository.saveAll(productos);
            log.info("✓ Productos: 15 productos electrónicos creados");

            // 4. Usuarios Autorizados (Analistas de encuestas)
            UsuarioAutorizado ua1 = new UsuarioAutorizado();
            ua1.setNombre("Carlos Rodríguez");
            ua1.setEmail("carlos.rodriguez@techstore.com.co");
            ua1.setClave(passwordEncoder.encode("carlos123"));
            ua1.setRol(RolUsuario.ADMIN);
            ua1.setEstado(EstadoUsuario.ACTIVO);
            ua1.setEmpresa(techStore);
            usuarioAutorizadoRepository.save(ua1);

            UsuarioAutorizado ua2 = new UsuarioAutorizado();
            ua2.setNombre("María González");
            ua2.setEmail("maria.gonzalez@electronicosmax.com.co");
            ua2.setClave(passwordEncoder.encode("maria123"));
            ua2.setRol(RolUsuario.ANALISTA);
            ua2.setEstado(EstadoUsuario.ACTIVO);
            ua2.setEmpresa(electronicosMax);
            usuarioAutorizadoRepository.save(ua2);

            UsuarioAutorizado ua3 = new UsuarioAutorizado();
            ua3.setNombre("Andrés Martínez");
            ua3.setEmail("andres.martinez@digitalzone.com.co");
            ua3.setClave(passwordEncoder.encode("andres123"));
            ua3.setRol(RolUsuario.ADMIN);
            ua3.setEstado(EstadoUsuario.ACTIVO);
            ua3.setEmpresa(digitalZone);
            usuarioAutorizadoRepository.save(ua3);

            log.info("✓ Usuarios Autorizados: 3 analistas creados");

            // 5. Encuestas
            Encuesta encuestaSatisfaccion = new Encuesta();
            encuestaSatisfaccion.setTitulo("Encuesta de Satisfacción - Productos Electrónicos");
            encuestaSatisfaccion.setDescripcion("Ayúdanos a mejorar evaluando tu compra reciente");
            encuestaSatisfaccion.setFechaInicio(LocalDate.now());
            encuestaSatisfaccion.setProducto(productos.get(0));
            encuestaSatisfaccion.setCreadoPor(ua1);
            encuestaRepository.save(encuestaSatisfaccion);

            Encuesta encuestaServicio = new Encuesta();
            encuestaServicio.setTitulo("Evaluación de Servicio al Cliente");
            encuestaServicio.setDescripcion("Califica la atención recibida");
            encuestaServicio.setFechaInicio(LocalDate.now().minusDays(15));
            encuestaServicio.setProducto(productos.get(4));
            encuestaServicio.setCreadoPor(ua2);
            encuestaRepository.save(encuestaServicio);

            Encuesta encuestaEntrega = new Encuesta();
            encuestaEntrega.setTitulo("Experiencia de Entrega y Envío");
            encuestaEntrega.setDescripcion("Evalúa el proceso de entrega");
            encuestaEntrega.setFechaInicio(LocalDate.now().minusDays(30));
            encuestaEntrega.setProducto(productos.get(1));
            encuestaEntrega.setCreadoPor(ua3);
            encuestaRepository.save(encuestaEntrega);

            log.info("✓ Encuestas: 3 encuestas creadas");

            // 6. Preguntas - Encuesta Satisfacción
            crearPreguntaConOpciones(encuestaSatisfaccion, 
                "¿Qué tan satisfecho estás con la calidad del producto?", 
                TipoPregunta.OPCION_UNICA, 1, true, "CALIFICACION");

            crearPreguntaConOpciones(encuestaSatisfaccion, 
                "¿El producto cumplió con tus expectativas?", 
                TipoPregunta.OPCION_UNICA, 2, true, "SINO");

            crearPreguntaConOpciones(encuestaSatisfaccion, 
                "¿Recomendarías este producto a otras personas?", 
                TipoPregunta.OPCION_UNICA, 3, true, "RECOMENDACION");

            Pregunta p4 = new Pregunta();
            p4.setTexto("¿Qué aspecto del producto te gustó más?");
            p4.setTipoPregunta(TipoPregunta.ABIERTA);
            p4.setObligatoria(false);
            p4.setOrden(4);
            p4.setEncuesta(encuestaSatisfaccion);
            preguntaRepository.save(p4);

            crearPreguntaConOpciones(encuestaSatisfaccion, 
                "Califica la relación calidad-precio", 
                TipoPregunta.OPCION_UNICA, 5, true, "CALIFICACION");

            // 7. Preguntas - Encuesta Servicio
            crearPreguntaConOpciones(encuestaServicio, 
                "¿Cómo calificarías la atención del personal?", 
                TipoPregunta.OPCION_UNICA, 1, true, "CALIFICACION");

            crearPreguntaConOpciones(encuestaServicio, 
                "¿El asesor resolvió todas tus dudas?", 
                TipoPregunta.OPCION_UNICA, 2, true, "SINO");

            crearPreguntaConOpciones(encuestaServicio, 
                "¿Cuánto tiempo esperaste para ser atendido?", 
                TipoPregunta.OPCION_UNICA, 3, true, "TIEMPO");

            Pregunta s4 = new Pregunta();
            s4.setTexto("Comentarios adicionales sobre el servicio");
            s4.setTipoPregunta(TipoPregunta.ABIERTA);
            s4.setObligatoria(false);
            s4.setOrden(4);
            s4.setEncuesta(encuestaServicio);
            preguntaRepository.save(s4);

            // 8. Preguntas - Encuesta Entrega
            crearPreguntaConOpciones(encuestaEntrega, 
                "¿Tu pedido llegó en el tiempo estimado?", 
                TipoPregunta.OPCION_UNICA, 1, true, "SINO");

            crearPreguntaConOpciones(encuestaEntrega, 
                "¿En qué condición llegó el empaque?", 
                TipoPregunta.OPCION_UNICA, 2, true, "CONDICION");

            crearPreguntaConOpciones(encuestaEntrega, 
                "Califica el servicio de mensajería", 
                TipoPregunta.OPCION_UNICA, 3, true, "CALIFICACION");

            log.info("✓ Preguntas: 15 preguntas con opciones creadas");

            log.info("========================================");
            log.info("✅ Base de datos poblada exitosamente");
            log.info("========================================");
            log.info("Credenciales de acceso:");
            log.info("  • Admin: admin / admin123");
            log.info("  • Gerente: gerente / gerente123");
            log.info("  • Carlos (TechStore): carlos.rodriguez@techstore.com.co / carlos123");
            log.info("  • María (ElectrMax): maria.gonzalez@electronicosmax.com.co / maria123");
            log.info("  • Andrés (DigZone): andres.martinez@digitalzone.com.co / andres123");
            log.info("========================================");
        };
    }

    private Producto crearProducto(String nombre) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        return producto;
    }

    private void crearPreguntaConOpciones(Encuesta encuesta, String texto, 
            TipoPregunta tipo, int orden, boolean obligatoria, String tipoOpciones) {
        Pregunta pregunta = new Pregunta();
        pregunta.setTexto(texto);
        pregunta.setTipoPregunta(tipo);
        pregunta.setOrden(orden);
        pregunta.setObligatoria(obligatoria);
        pregunta.setEncuesta(encuesta);
        preguntaRepository.save(pregunta);

        switch (tipoOpciones) {
            case "CALIFICACION":
                crearOpcionesCalificacion(pregunta);
                break;
            case "SINO":
                crearOpcionesSiNo(pregunta);
                break;
            case "RECOMENDACION":
                crearOpcionesRecomendacion(pregunta);
                break;
            case "TIEMPO":
                crearOpcionesTiempoEspera(pregunta);
                break;
            case "CONDICION":
                crearOpcionesCondicionEmpaque(pregunta);
                break;
        }
    }

    private void crearOpcionesCalificacion(Pregunta pregunta) {
        String[] opciones = {"Muy insatisfecho", "Insatisfecho", "Neutral", "Satisfecho", "Muy satisfecho"};
        for (int i = 0; i < opciones.length; i++) {
            OpcionRespuesta opcion = new OpcionRespuesta();
            opcion.setTexto(opciones[i]);
            opcion.setValor(i + 1);
            opcion.setOrden(i + 1);
            opcion.setPregunta(pregunta);
            opcionRespuestaRepository.save(opcion);
        }
    }

    private void crearOpcionesSiNo(Pregunta pregunta) {
        OpcionRespuesta si = new OpcionRespuesta();
        si.setTexto("Sí");
        si.setValor(1);
        si.setOrden(1);
        si.setPregunta(pregunta);
        opcionRespuestaRepository.save(si);

        OpcionRespuesta no = new OpcionRespuesta();
        no.setTexto("No");
        no.setValor(0);
        no.setOrden(2);
        no.setPregunta(pregunta);
        opcionRespuestaRepository.save(no);
    }

    private void crearOpcionesRecomendacion(Pregunta pregunta) {
        String[] opciones = {"Definitivamente no", "Probablemente no", "No estoy seguro", "Probablemente sí", "Definitivamente sí"};
        for (int i = 0; i < opciones.length; i++) {
            OpcionRespuesta opcion = new OpcionRespuesta();
            opcion.setTexto(opciones[i]);
            opcion.setValor(i + 1);
            opcion.setOrden(i + 1);
            opcion.setPregunta(pregunta);
            opcionRespuestaRepository.save(opcion);
        }
    }

    private void crearOpcionesTiempoEspera(Pregunta pregunta) {
        String[] opciones = {"Menos de 5 minutos", "5-10 minutos", "10-20 minutos", "20-30 minutos", "Más de 30 minutos"};
        for (int i = 0; i < opciones.length; i++) {
            OpcionRespuesta opcion = new OpcionRespuesta();
            opcion.setTexto(opciones[i]);
            opcion.setValor(i + 1);
            opcion.setOrden(i + 1);
            opcion.setPregunta(pregunta);
            opcionRespuestaRepository.save(opcion);
        }
    }

    private void crearOpcionesCondicionEmpaque(Pregunta pregunta) {
        String[] opciones = {"Perfecto estado", "Pequeños daños", "Daños moderados", "Muy dañado"};
        for (int i = 0; i < opciones.length; i++) {
            OpcionRespuesta opcion = new OpcionRespuesta();
            opcion.setTexto(opciones[i]);
            opcion.setValor(4 - i);
            opcion.setOrden(i + 1);
            opcion.setPregunta(pregunta);
            opcionRespuestaRepository.save(opcion);
        }
    }
}
