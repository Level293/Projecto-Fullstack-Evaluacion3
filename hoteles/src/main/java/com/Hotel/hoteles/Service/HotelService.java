package com.Hotel.hoteles.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Hotel.hoteles.DTO.HotelDTO;
import com.Hotel.hoteles.Repository.HotelRepository;
import com.Hotel.hoteles.model.Hotel;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public List<HotelDTO> obtenerTodo() {
        return hotelRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public HotelDTO buscarPorId(Integer id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El hotel no existe"));
        return convertirADTO(hotel);
    }

    public List<HotelDTO> buscarPorNombre(String nombre) {
        return hotelRepository.findByNombre(nombre).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public HotelDTO guardarHotel(Hotel hotel) {
        Hotel guardado = hotelRepository.save(hotel);
        return convertirADTO(guardado);
    }

    private HotelDTO convertirADTO(Hotel hotel) {
        if (hotel == null) return null;
        HotelDTO dto = new HotelDTO();
       
        dto.setIdHotel(hotel.getIdHotel());
        dto.setNombre(hotel.getNombre());      
        dto.setDireccion(hotel.getDireccion());
        
        return dto;
    }
}