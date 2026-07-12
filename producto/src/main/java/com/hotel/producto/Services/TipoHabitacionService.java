package com.hotel.producto.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.hotel.producto.DTO.TipoHabitacionDTO;
import com.hotel.producto.Repository.TipoHabitacionRepository;
import com.hotel.producto.model.TipoHabitacion;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class TipoHabitacionService {

    @Autowired
    private TipoHabitacionRepository tipoHabitacionRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private HabitacionValidaciones habitacionValidaciones;

    public List<TipoHabitacionDTO> obtenerTodo() {
        return tipoHabitacionRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public TipoHabitacionDTO buscarPorId(Integer id) {
        TipoHabitacion tipo = tipoHabitacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de habitacion no existe"));
        return convertirADTO(tipo);
    }

    public TipoHabitacionDTO guardarTipo(TipoHabitacion tipo) {
        if (!habitacionValidaciones.elPrecioEsValido(tipo.getPrecio())) {
            throw new RuntimeException("El precio del tipo de habitación debe ser mayor a cero.");
        }
        
        TipoHabitacion guardado = tipoHabitacionRepository.save(tipo);
        return convertirADTO(guardado);
    }

    public String eliminar(Integer id) {
        if (!tipoHabitacionRepository.existsById(id)) {
            return "ID no encontrado.";
        }
        tipoHabitacionRepository.deleteById(id);
        return "Tipo de habitacion eliminado.";
    }

    private TipoHabitacionDTO convertirADTO(TipoHabitacion tipo) {
        if (tipo == null) return null;
        TipoHabitacionDTO dto = new TipoHabitacionDTO();

        dto.setIdTipo(tipo.getIdTipo());
        dto.setNombre(tipo.getNombre());
        dto.setDescripcion(tipo.getDescripcion());
        dto.setCapacidad(tipo.getCapacidad());
        dto.setPrecio(tipo.getPrecio());

        return dto;
    }
}