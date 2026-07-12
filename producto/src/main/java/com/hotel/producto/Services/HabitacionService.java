package com.hotel.producto.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.hotel.producto.DTO.HabitacionDTO;
import com.hotel.producto.DTO.TipoHabitacionDTO;
import com.hotel.producto.Repository.HabitacionRepository;
import com.hotel.producto.Repository.TipoHabitacionRepository;
import com.hotel.producto.model.Habitacion;
import com.hotel.producto.model.TipoHabitacion;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class HabitacionService {

    @Autowired
    public HabitacionRepository habitacionRepository;

    @Autowired
    public TipoHabitacionRepository tipoHabitacionRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private HabitacionValidaciones habitacionValidaciones;
    
    public List<HabitacionDTO> obtenerTodo() {
        return habitacionRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public HabitacionDTO buscarPorId(Integer id_habitacion) {
        Habitacion habitacion = habitacionRepository.findById(id_habitacion)
                .orElseThrow(() -> new RuntimeException("La habitacion no existe"));
        return convertirADTO(habitacion);
    }

    public HabitacionDTO guardarHabitacion(HabitacionDTO dto) {
        if (!habitacionValidaciones.validarCamposHabitacion(dto)) {
            throw new RuntimeException("Datos de la habitación inválidos o incompletos");
        }

        if (habitacionValidaciones.elNumeroYaExiste(dto.getNumero())) {
            throw new RuntimeException("El número de habitación ya está registrado en el sistema");
        }

        Habitacion habitacion = new Habitacion();
        habitacion.setIdHotel(dto.getIdHotel());
        habitacion.setNumero(dto.getNumero());
        habitacion.setEstado(dto.getEstado());

        if (dto.getTipoHabitacion() != null && dto.getTipoHabitacion().getIdTipo() != null) {
            TipoHabitacion tipo = tipoHabitacionRepository.findById(dto.getTipoHabitacion().getIdTipo())
                    .orElseThrow(() -> new RuntimeException("Tipo de habitación no encontrado en el sistema"));
            habitacion.setTipoHabitacion(tipo);
        } else {
            throw new RuntimeException("El tipo de habitación es obligatorio");
        }

        Habitacion nuevaHabitacion = habitacionRepository.save(habitacion);
        return convertirADTO(nuevaHabitacion);
    }

    public String eliminar(Integer id) {
        try {
            Habitacion habitacion = habitacionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! La habitacion con ID " + id + " no existe."));
            habitacionRepository.delete(habitacion);
            return "La habitacion " + habitacion.getNumero() + "' ha sido retirado del hotel exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
    
    public List<HabitacionDTO> buscarPorEstado(String estado) {
        return habitacionRepository.findByEstado(estado).stream()
                .map(this::convertirADTO)
                .toList();
    }

    private HabitacionDTO convertirADTO(Habitacion hab) {
        HabitacionDTO dto = new HabitacionDTO();
        dto.setIdHotel(hab.getIdHotel());
        dto.setIdHabitacion(hab.getIdHabitacion());
        dto.setNumero(hab.getNumero());
        dto.setEstado(hab.getEstado());

        if (hab.getTipoHabitacion() != null) {
            TipoHabitacionDTO tipoDto = new TipoHabitacionDTO();
            tipoDto.setIdTipo(hab.getTipoHabitacion().getIdTipo());
            tipoDto.setNombre(hab.getTipoHabitacion().getNombre());
            tipoDto.setCapacidad(hab.getTipoHabitacion().getCapacidad());
            tipoDto.setPrecio(hab.getTipoHabitacion().getPrecio());
            dto.setTipoHabitacion(tipoDto);
        }
        return dto;
    }

    public List<HabitacionDTO> obtenerTodasLasHabitaciones() {
        throw new UnsupportedOperationException("Unimplemented method 'obtenerTodasLasHabitaciones'");
    }

    public HabitacionDTO guardar(HabitacionDTO dtoIngreso) {
        throw new UnsupportedOperationException("Unimplemented method 'guardar'");
    }

    public HabitacionDTO actualizarEstado(Integer idHabitacion, String string) {
        throw new UnsupportedOperationException("Unimplemented method 'actualizarEstado'");
    }
}