package com.hotel.producto.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.hotel.producto.DTO.ServicioDTO;
import com.hotel.producto.Repository.ServicioRepository;
import com.hotel.producto.model.Servicio;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class ServicioService {

    @Autowired
    public ServicioRepository servicioRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;
    public List<ServicioDTO> obtenerTodo() {
        return servicioRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ServicioDTO buscarPorId(Integer id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El servicio no existe"));
        return convertirADTO(servicio);
    }

    public Servicio guardarServicio(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    public String eliminar(Integer id) {
        try {
            Servicio servicio = servicioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado."));
            servicioRepository.delete(servicio);
            return "Servicio '" + servicio.getNombre() + "' eliminado.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private ServicioDTO convertirADTO(Servicio servicio) {
        ServicioDTO dto = new ServicioDTO();
        dto.setIdServicio(servicio.getIdServicio());
        dto.setNombre(servicio.getNombre());
        dto.setPrecio(servicio.getPrecio());
        return dto;
    }
}
