package com.hotel.producto.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.producto.DTO.ReservaHabitacionDTO;
import com.hotel.producto.Repository.ReservaHabitacionRepository;
import com.hotel.producto.model.ReservaHabitacion;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class ReservaHabitacionService {

    @Autowired
    public ReservaHabitacionRepository reservaHabRepository;
    
    public List <ReservaHabitacionDTO> obtenerTodos() {
        return reservaHabRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ReservaHabitacionDTO buscarPorId(Integer IdReservaHab) {
        ReservaHabitacion reservaHab = reservaHabRepository.findById(IdReservaHab)
                .orElseThrow(() -> new RuntimeException("La reserva de la habitacion no existe"));
            return convertirADTO(reservaHab);
    }

    public ReservaHabitacion guardarReservaHab(ReservaHabitacion reservaHab) {
        return reservaHabRepository.save(reservaHab);
    }

    public String eliminar(Integer id) {
        try {
            ReservaHabitacion reservaHab = reservaHabRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! La Reserva de Habitacion con ID " + id + " no existe."));
            reservaHabRepository.delete(reservaHab);
            return "La reserva de habitacion " + reservaHab.getHabitacion() + "' ha sido retirado de la reserva exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }    

    public List<ReservaHabitacionDTO> buscarPorPrecioNoche(Integer precioNoche) {
        List<ReservaHabitacion> reservas = reservaHabRepository.findByPrecioNoche(precioNoche);
        return reservas.stream()
                .map(this::convertirADTO)
                .toList();
    }

    private ReservaHabitacionDTO convertirADTO(ReservaHabitacion reservaHab) {
        ReservaHabitacionDTO dto = new ReservaHabitacionDTO();
        dto.setIdReservaHab(reservaHab.getIdReservaHab());
        dto.setPrecioNoche(reservaHab.getPrecioNoche());
        dto.setIdReserva(reservaHab.getIdReserva());

        return dto;
    }

}
