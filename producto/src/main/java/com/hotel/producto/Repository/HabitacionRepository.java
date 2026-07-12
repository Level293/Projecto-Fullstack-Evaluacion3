package com.hotel.producto.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hotel.producto.model.Habitacion;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Integer> {

    List<Habitacion> findByEstado(String estado);

    Habitacion findByNumero(Integer numero);

    List<Habitacion> findByIdHotel(Integer idHotel);

    boolean existsByNumero(Integer numero);
}