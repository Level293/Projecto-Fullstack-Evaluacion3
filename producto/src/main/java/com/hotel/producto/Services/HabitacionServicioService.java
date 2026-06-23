package com.hotel.producto.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.producto.DTO.HabitacionServicioDTO;
import com.hotel.producto.Repository.HabitacionServicioRepository;
import com.hotel.producto.model.HabitacionServicio;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class HabitacionServicioService {

    @Autowired
    public HabitacionServicioRepository habitacionServicioRepository;

    public List<HabitacionServicioDTO> obtenerTodo() {
        return habitacionServicioRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public HabitacionServicioDTO buscarPorId(Integer id) {
        HabitacionServicio hs = habitacionServicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El registro de servicio en habitación no existe"));
        return convertirADTO(hs);
    }

    public HabitacionServicio guardarHabitacionServicio(HabitacionServicio habitacionServicio) {
        return habitacionServicioRepository.save(habitacionServicio);
    }

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

    private HabitacionServicioDTO convertirADTO(HabitacionServicio hs) {
        HabitacionServicioDTO dto = new HabitacionServicioDTO();
        dto.setIdHabservicio(hs.getIdHabServicio());

        // Lógica para llenar los datos legibles en el DTO
        if (hs.getHabitacion() != null) {
            dto.setNumeroHabitacion(hs.getHabitacion().getNumero());
        }

        if (hs.getServicio() != null) {
            dto.setNombreServicio(hs.getServicio().getNombre());
            dto.setPrecioServicio(hs.getServicio().getPrecio());
        }

        return dto;
    }
}
