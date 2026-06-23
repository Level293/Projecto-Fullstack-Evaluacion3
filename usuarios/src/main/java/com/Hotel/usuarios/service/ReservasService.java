package com.Hotel.usuarios.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.Hotel.usuarios.DTO.ReservaDTO;
import com.Hotel.usuarios.model.Reservas;
import com.Hotel.usuarios.repository.ReservasRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReservasService {

    @Autowired
    public ReservasRepository reservasRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;


    public List<ReservaDTO> obtenerTodas() {
        return reservasRepository.findAll().stream()
                .map(this::convertirADTO) 
                .collect(Collectors.toList());
    }

    public ReservaDTO buscarPorId(Integer id_reserva) {
        Reservas reservas = reservasRepository.findById(id_reserva)
                .orElseThrow(() -> new RuntimeException("La reserva no existe"));
        return convertirADTO(reservas); 
    }

    public List<ReservaDTO> buscarPorEstado(String estado){
        return reservasRepository.findByEstado(estado).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Guardamos una Reserva
    public Reservas guardarReservas(Reservas reservas) {
        return reservasRepository.save(reservas);
    }

    // Eliminamos una reserva por su id
    public String eliminar(Integer id) {
        try {
            Reservas reservas = reservasRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("La reserva con ID " + id + " no existe."));
            reservasRepository.delete(reservas);
            return "La reserva '" + id + "' ha sido eliminada exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private ReservaDTO convertirADTO(Reservas reservas) {
        ReservaDTO dto = new ReservaDTO();
        dto.setIdReserva(reservas.getIdReserva());
        dto.setFechaInicio(reservas.getFechaInicio());
        dto.setFechaFin(reservas.getFechaFin());
        dto.setEstado(reservas.getEstado());
        dto.setTotal(reservas.getTotal());
        
        return dto;
    }
}









