package com.hotel.producto.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotel.producto.model.Habitacion;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Integer> {

    // Buscar habitaciones por estado (ej. "DISPONIBLE")
    List<Habitacion> findByEstado(String estado);

    // Buscar una habitación específica por su número
    Habitacion findByNumero(Integer numero);

    // Buscar todas las habitaciones de un hotel específico (usando el ID del hotel)
    List<Habitacion> findByIdHotel(Integer idHotel);


    
}
