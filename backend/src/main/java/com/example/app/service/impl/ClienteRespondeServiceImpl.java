package com.example.app.service.impl;

import com.example.app.dto.ClienteRespondeDto;
import com.example.app.exception.CustomException;
import com.example.app.model.ClienteResponde;
import com.example.app.repository.ClienteRespondeRepository;
import com.example.app.service.ClienteRespondeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClienteRespondeServiceImpl implements ClienteRespondeService {

    private final ClienteRespondeRepository clienteRespondeRepository;

    public ClienteRespondeServiceImpl(ClienteRespondeRepository clienteRespondeRepository) {
        this.clienteRespondeRepository = clienteRespondeRepository;
    }

    @Override
    @Transactional
    public ClienteRespondeDto crear(ClienteRespondeDto dto) {
        ClienteResponde cliente = new ClienteResponde();
        cliente.setNombre(dto.getNombre());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setCanalOrigen(dto.getCanalOrigen());
        return mapToDto(clienteRespondeRepository.save(cliente));
    }

    @Override
    @Transactional
    public ClienteRespondeDto actualizar(UUID id, ClienteRespondeDto dto) {
        ClienteResponde cliente = buscarPorId(id);
        cliente.setNombre(dto.getNombre());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setCanalOrigen(dto.getCanalOrigen());
        return mapToDto(cliente);
    }

    @Override
    public ClienteRespondeDto obtenerPorId(UUID id) {
        return mapToDto(buscarPorId(id));
    }

    @Override
    public List<ClienteRespondeDto> listarTodos() {
        return clienteRespondeRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminar(UUID id) {
        ClienteResponde cliente = buscarPorId(id);
        clienteRespondeRepository.delete(cliente);
    }

    private ClienteResponde buscarPorId(UUID id) {
        return clienteRespondeRepository.findById(id)
                .orElseThrow(() -> new CustomException("Cliente no encontrado"));
    }

    private ClienteRespondeDto mapToDto(ClienteResponde cliente) {
        return new ClienteRespondeDto(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getCanalOrigen()
        );
    }
}
