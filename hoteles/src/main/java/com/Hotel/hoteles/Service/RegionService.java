package com.Hotel.hoteles.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Hotel.hoteles.DTO.RegionDTO;
import com.Hotel.hoteles.Repository.RegionRepository;
import com.Hotel.hoteles.model.Region;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegionService {

    @Autowired
    public RegionRepository regionRepository;

    public List<RegionDTO> obtenerTodo() {
        return regionRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public RegionDTO buscarPorId(Integer id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La region no existe"));
        return convertirADTO(region);
    }

    public Region guardarRegion(Region region) {
        return regionRepository.save(region);
    }

    private RegionDTO convertirADTO(Region region) {
        RegionDTO dto = new RegionDTO();
        dto.setIdRegion(region.getIdRegion());
        dto.setNombre(region.getNombre());
        return dto;
    }
}