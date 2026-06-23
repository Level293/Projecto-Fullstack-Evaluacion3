package com.hotel.producto.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotel.producto.model.TipoHabitacion;

@Repository
public interface TipoHabitacionRepository extends JpaRepository<TipoHabitacion, Integer>{

    
    List<TipoHabitacion> findByNombre(String nombre);

    List<TipoHabitacion> findByNombreContainingIgnoreCase(String nombre);

    List<TipoHabitacion> findByPrecio(Integer precio);

}
