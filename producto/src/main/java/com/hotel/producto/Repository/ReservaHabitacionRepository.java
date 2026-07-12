package com.hotel.producto.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotel.producto.model.ReservaHabitacion;

@Repository
public interface ReservaHabitacionRepository extends JpaRepository<ReservaHabitacion, Integer> {

    // 1. Buscar todas las habitaciones asociadas a una reserva específica
    // Ideal para mostrarle al cliente qué habitaciones reservó
    List<ReservaHabitacion> findByIdReserva(Integer idReserva);

    // 2. Buscar el historial de una habitación específica
    // Ideal para saber cuántas veces se ha reservado una habitación
    List<ReservaHabitacion> findByHabitacion_IdHabitacion(Integer idHabitacion);

    // 3. Buscar por precio exacto
    List<ReservaHabitacion> findByPrecioNoche(Integer precioNoche);
}
