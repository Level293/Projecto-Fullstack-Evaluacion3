package com.Hotel.hoteles.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Hotel.hoteles.model.Comuna;

@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Integer> {

    List<Comuna> findByNombre(String nombre);

    List<Comuna> findByNombreContainingIgnoreCase(String nombre);

    List<Comuna> findByRegion_IdRegion(Integer idRegion);
}