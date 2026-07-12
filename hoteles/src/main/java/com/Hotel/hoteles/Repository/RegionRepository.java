package com.Hotel.hoteles.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Hotel.hoteles.model.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {

    List<Region> findByNombre(String nombre);

    List<Region> findByNombreContainingIgnoreCase(String nombre);

    List<Region> findAllByOrderByNombreAsc();
}