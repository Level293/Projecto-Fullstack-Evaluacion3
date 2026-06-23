package com.hotel.producto.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotel.producto.model.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
  
    List<Servicio> findByNombre(String nombre);

    List<Servicio> findByNombreContainingIgnoreCase(String nombre);

    List<Servicio> findByPrecioLessThanEqual(Double precio);

    List<Servicio> findByPrecioBetween(Double precioMin, Double precioMax);


}
