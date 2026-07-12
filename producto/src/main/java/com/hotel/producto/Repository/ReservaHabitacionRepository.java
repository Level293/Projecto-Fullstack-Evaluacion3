package com.hotel.producto.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotel.producto.model.ReservaHabitacion;

@Repository
public interface ReservaHabitacionRepository extends JpaRepository<ReservaHabitacion, Integer> {

    List<ReservaHabitacion> findByIdReserva(Integer idReserva);

    List<ReservaHabitacion> findByHabitacion_IdHabitacion(Integer idHabitacion);

    List<ReservaHabitacion> findByPrecioNoche(Integer precioNoche);
}
