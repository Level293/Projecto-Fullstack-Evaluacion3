package com.hotel.producto.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.hotel.producto.DTO.HabitacionDTO;
import com.hotel.producto.DTO.TipoHabitacionDTO;
import com.hotel.producto.DTO.HotelExternoDTO; 
import com.hotel.producto.DTO.UsuarioExternoDTO; // <-- Importamos tu nuevo DTO Espejo
import com.hotel.producto.Repository.HabitacionRepository;
import com.hotel.producto.Repository.TipoHabitacionRepository;
import com.hotel.producto.model.Habitacion;
import com.hotel.producto.model.TipoHabitacion;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class HabitacionService {

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private TipoHabitacionRepository tipoHabitacionRepository;
    
    @Autowired
    private WebClient.Builder webClientBuilder;

    // Método para obtener todas las habitaciones mapeadas
    public List<HabitacionDTO> obtenerTodo() {
        List<HabitacionDTO> listaDTOs = new ArrayList<>();
        List<Habitacion> habitacionesReales = habitacionRepository.findAll();
        for (Habitacion h : habitacionesReales) {
            listaDTOs.add(convertirADTO(h));
        }
        return listaDTOs;
    }

    // Buscar habitación por ID
    public HabitacionDTO buscarPorId(Integer id_habitacion) {
        Habitacion habitacion = habitacionRepository.findById(id_habitacion)
                .orElseThrow(() -> new RuntimeException("La habitación no existe en los archivos del hotel"));
        return convertirADTO(habitacion);
    }

    // Guardar una nueva habitación
    public HabitacionDTO guardarHabitacion(HabitacionDTO dto) {
        Habitacion habitacion = new Habitacion();
        habitacion.setIdHotel(dto.getIdHotel());
        habitacion.setNumero(dto.getNumero());
        habitacion.setEstado(dto.getEstado());
        
        // Si manejas la asignación del usuario al guardar/crear de entrada:
        // habitacion.setIdUsuario(dto.getUsuarioActual() != null ? dto.getUsuarioActual().getIdUsuario() : null);

        if (dto.getTipoHabitacion() != null && dto.getTipoHabitacion().getIdTipo() != null) {
            TipoHabitacion tipo = tipoHabitacionRepository.findById(dto.getTipoHabitacion().getIdTipo())
                    .orElseThrow(() -> new RuntimeException("Tipo de habitación no encontrado"));
            habitacion.setTipoHabitacion(tipo);
        }

        Habitacion nuevaHabitacion = habitacionRepository.save(habitacion);
        return convertirADTO(nuevaHabitacion);
    }

    // Eliminar habitación
    public String eliminar(Integer id) {
        try {
            Habitacion habitacion = habitacionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! La habitación con ID " + id + " no existe."));
            habitacionRepository.delete(habitacion);
            return "La habitación número " + habitacion.getNumero() + " ha sido retirada exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
    
    // Buscar habitaciones por estado (Ej: DISPONIBLE, OCUPADA)
    public List<HabitacionDTO> buscarPorEstado(String estado) {
        List<HabitacionDTO> listaDTOs = new ArrayList<>();
        List<Habitacion> habitacionesPorEstado = habitacionRepository.findByEstado(estado);
        for (Habitacion h : habitacionesPorEstado) {
            listaDTOs.add(convertirADTO(h));
        }
        return listaDTOs;
    }

    // Actualizar dinámicamente el estado de una habitación
    public HabitacionDTO actualizarEstado(Integer idHabitacion, String nuevoEstado) {
        Habitacion habitacion = habitacionRepository.findById(idHabitacion)
                .orElseThrow(() -> new RuntimeException("La habitación no existe"));
        habitacion.setEstado(nuevoEstado);
        return convertirADTO(habitacionRepository.save(habitacion));
    }

    // Mapeo unificado: Relación Local + 2 Comunicaciones Externas vía WebClient (Estilo Jedis)
    private HabitacionDTO convertirADTO(Habitacion hab) {
        HabitacionDTO dto = new HabitacionDTO();
        dto.setIdHotel(hab.getIdHotel());
        dto.setIdHabitacion(hab.getIdHabitacion());
        dto.setNumero(hab.getNumero());
        dto.setEstado(hab.getEstado());

        // 1. ASOCIACIÓN LOCAL (Misma Base de Datos): Mapea TipoHabitacion
        if (hab.getTipoHabitacion() != null) {
            TipoHabitacionDTO tipoDto = new TipoHabitacionDTO();
            tipoDto.setIdTipo(hab.getTipoHabitacion().getIdTipo());
            tipoDto.setNombre(hab.getTipoHabitacion().getNombre());
            tipoDto.setCapacidad(hab.getTipoHabitacion().getCapacidad());
            tipoDto.setPrecio(hab.getTipoHabitacion().getPrecio());
            dto.setTipoHabitacion(tipoDto);
        }

        // 2. ASOCIACIÓN EXTERNA 1: Recupera el Hotel desde 'hoteles-service'
        try {
            HotelExternoDTO hotelRecuperado = webClientBuilder.build()
                    .get()
                    .uri("http://hoteles-service/api/v1/hoteles/" + hab.getIdHotel())
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty()) 
                    .bodyToMono(HotelExternoDTO.class)
                    .block(); 

            dto.setHotel(hotelRecuperado); 

        } catch (Exception e) {
            dto.setHotel(null); 
        }

        // 3. ASOCIACIÓN EXTERNA 2: Recupera el Usuario/Huésped desde 'usuarios-service'
        // Solo ejecuta la llamada si la habitación tiene un idUsuario grabado en la Base de Datos
        if (hab.getIdUsuario() != null) {
            try {
                UsuarioExternoDTO usuarioRecuperado = webClientBuilder.build()
                        .get()
                        .uri("http://usuarios-service/api/v1/usuarios/" + hab.getIdUsuario()) // Ruta balanceada a usuarios
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                        .bodyToMono(UsuarioExternoDTO.class)
                        .block();

                dto.setUsuarioActual(usuarioRecuperado); // Adjuntamos el objeto Usuario al DTO

            } catch (Exception e) {
                // Si usuarios-service se cae, la app no se interrumpe y el campo viaja vacío
                dto.setUsuarioActual(null); 
            }
        }

        return dto;
    }

    // Métodos alias requeridos por tus controladores anteriores
    public List<HabitacionDTO> obtenerTodasLasHabitaciones() {
        return obtenerTodo();
    }

    public HabitacionDTO guardar(HabitacionDTO dtoIngreso) {
        return guardarHabitacion(dtoIngreso);
    }
}