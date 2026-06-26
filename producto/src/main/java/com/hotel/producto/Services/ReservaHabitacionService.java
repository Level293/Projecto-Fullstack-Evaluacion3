package com.hotel.producto.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.hotel.producto.DTO.ReservaHabitacionDTO;
import com.hotel.producto.DTO.ReservaExternoDTO;
import com.hotel.producto.DTO.HabitacionDTO;
import com.hotel.producto.Repository.ReservaHabitacionRepository;
import com.hotel.producto.Repository.HabitacionRepository;
import com.hotel.producto.model.ReservaHabitacion;
import com.hotel.producto.model.Habitacion;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class ReservaHabitacionService {

    @Autowired
    private ReservaHabitacionRepository reservaHabitacionRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private HabitacionService habitacionService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    // Obtener todas las habitaciones reservadas
    public List<ReservaHabitacionDTO> obtenerTodo() {
        return reservaHabitacionRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    // Buscar por ID
    public ReservaHabitacionDTO buscarPorId(Integer id) {
        ReservaHabitacion rh = reservaHabitacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El registro de reserva de habitación no existe"));
        return convertirADTO(rh);
    }

    // Guardar una nueva asignación de habitación a una reserva
    public ReservaHabitacionDTO guardarReservaHabitacion(ReservaHabitacionDTO dto) {
        ReservaHabitacion rh = new ReservaHabitacion();
        rh.setIdReserva(dto.getIdReserva());
        rh.setPrecioNoche(dto.getPrecioNoche());

        if (dto.getHabitacion() != null && dto.getHabitacion().getIdHabitacion() != null) {
            Habitacion hab = habitacionRepository.findById(dto.getHabitacion().getIdHabitacion())
                    .orElseThrow(() -> new RuntimeException("La habitación seleccionada no existe"));
            rh.setHabitacion(hab);
        }

        ReservaHabitacion guardado = reservaHabitacionRepository.save(rh);
        return convertirADTO(guardado);
    }

    // Eliminar el registro
    public String eliminar(Integer id) {
        try {
            ReservaHabitacion rh = reservaHabitacionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible retirar! El registro con ID " + id + " no existe."));
            reservaHabitacionRepository.delete(rh);
            return "La habitación ha sido desvinculada de la reserva exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    // Mapeo Híbrido: Resuelve la habitación localmente y busca la cabecera de la reserva por WebClient
    private ReservaHabitacionDTO convertirADTO(ReservaHabitacion rh) {
        ReservaHabitacionDTO dto = new ReservaHabitacionDTO();
        dto.setIdReservaHab(rh.getIdReserva());
        dto.setPrecioNoche(rh.getPrecioNoche());
        dto.setIdReserva(rh.getIdReserva());

        // 1. ASOCIACIÓN LOCAL ENRIQUECIDA: Mapea la habitación usando su propio service
        if (rh.getHabitacion() != null) {
            HabitacionDTO habDto = habitacionService.buscarPorId(rh.getHabitacion().getIdHabitacion());
            dto.setHabitacion(habDto);
        }

        // 2. ASOCIACIÓN EXTERNA: Viaja hacia 'usuarios-service' usando el método onStatus original
        if (rh.getIdReserva() != null) {
            try {
                ReservaExternoDTO reservaRecuperada = webClientBuilder.build()
                        .get()
                        .uri("http://usuarios-service/api/v1/usuarios/reservas/" + rh.getIdReserva()) 
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                        .bodyToMono(ReservaExternoDTO.class)
                        .block(); 

                dto.setReservaGeneral(reservaRecuperada); 

            } catch (Exception e) {
                dto.setReservaGeneral(null); 
            }
        }

        return dto;
    }

    public List<ReservaHabitacionDTO> buscarPorPrecioNoche(Integer precioNoche) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorPrecioNoche'");
    }
}