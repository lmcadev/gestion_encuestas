package com.example.app.model;

import com.example.app.model.enums.CanalOrigen;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Datos del cliente que responde encuestas.
 */
@Entity
@Table(name = "clientes_responde")
public class ClienteResponde {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_cliente")
    private UUID id;

    @Column(nullable = false)
    private String nombre;

    private String email;

    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(name = "canal_origen")
    private CanalOrigen canalOrigen;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RespuestaEncuesta> respuestas = new ArrayList<>();

    public ClienteResponde() {
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public CanalOrigen getCanalOrigen() {
        return canalOrigen;
    }

    public void setCanalOrigen(CanalOrigen canalOrigen) {
        this.canalOrigen = canalOrigen;
    }

    public List<RespuestaEncuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<RespuestaEncuesta> respuestas) {
        this.respuestas = respuestas;
    }
}
