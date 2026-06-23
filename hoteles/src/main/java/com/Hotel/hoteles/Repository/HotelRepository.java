package com.Hotel.hoteles.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Hotel.hoteles.model.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {

    List<Hotel> findByNombre(String nombre);
   
    List<Hotel> findByDireccion(String direccion);
}