package com.hotel.producto.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.producto.DTO.HabitacionServicioDTO;
import com.hotel.producto.Repository.HabitacionServicioRepository;
import com.hotel.producto.Repository.HabitacionRepository; // Agregado para persistencia local
import com.hotel.producto.Repository.ServicioRepository;     // Agregado para persistencia local
import com.hotel.producto.model.Habitacion;
import com.hotel.producto.model.Servicio;
import com.hotel.producto.model.HabitacionServicio;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class HabitacionServicioService {

    @Autowired
    private HabitacionServicioRepository habitacionServicioRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    // Obtener todos los registros mapeados a DTO
    public List<HabitacionServicioDTO> obtenerTodo() {
        return habitacionServicioRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    // Buscar por ID mapeado a DTO
    public HabitacionServicioDTO buscarPorId(Integer id) {
        HabitacionServicio hs = habitacionServicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El registro de servicio en habitación no existe"));
        return convertirADTO(hs);
    }

    // CORREGIDO: Guarda usando DTO para acoplarse perfectamente al Controller
    public HabitacionServicioDTO guardarHabitacionServicio(HabitacionServicioDTO dto) {
        HabitacionServicio habitacionServicio = new HabitacionServicio();
        
        // 1. Validar y asociar la Habitación de forma Local
        if (dto.getNumeroHabitacion() != null) {
            Habitacion hab = habitacionRepository.findById(dto.getNumeroHabitacion())
                    .orElseThrow(() -> new RuntimeException("La habitación asociada no existe"));
            habitacionServicio.setHabitacion(hab);
        }

        // 2. Validar y asociar el Servicio de forma Local
        if (dto.getIdHabservicio() != null) {
            Servicio serv = servicioRepository.findById(dto.getIdHabservicio())
                    .orElseThrow(() -> new RuntimeException("El servicio asociado no existe"));
            habitacionServicio.setServicio(serv);
        }

        HabitacionServicio guardado = habitacionServicioRepository.save(habitacionServicio);
        return convertirADTO(guardado);
    }

    // Eliminar registro
    public String eliminar(Integer id) {
        try {
            HabitacionServicio hs = habitacionServicioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! El registro con ID " + id + " no existe."));
            habitacionServicioRepository.delete(hs);
            return "El servicio ha sido retirado de la habitación exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    // Tu mapeo local impecable (No requiere WebClient al ser la misma Base de Datos)
    private HabitacionServicioDTO convertirADTO(HabitacionServicio hs) {
        HabitacionServicioDTO dto = new HabitacionServicioDTO();
        dto.setIdHabservicio(hs.getIdHabServicio());

        if (hs.getHabitacion() != null) {
            dto.setNumeroHabitacion(hs.getHabitacion().getIdHabitacion()); // Id para la lógica interna
            dto.setNumeroHabitacion(hs.getHabitacion().getNumero());
        }

        if (hs.getServicio() != null) {
            dto.setIdHabservicio(hs.getServicio().getIdServicio());       // Id para la lógica interna
            dto.setNombreServicio(hs.getServicio().getNombre());
            dto.setPrecioServicio(hs.getServicio().getPrecio());
        }

        return dto;
    }
}