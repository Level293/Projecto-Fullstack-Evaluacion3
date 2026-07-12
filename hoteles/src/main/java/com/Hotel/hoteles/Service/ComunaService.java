package com.Hotel.hoteles.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Hotel.hoteles.DTO.ComunaDTO;
import com.Hotel.hoteles.model.Comuna;
import com.Hotel.hoteles.Repository.ComunaRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComunaService {

    @Autowired
    public ComunaRepository comunaRepository;

    public List<ComunaDTO> obtenerTodo() {
        return comunaRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ComunaDTO buscarPorId(Integer id) {
        Comuna comuna = comunaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La comuna no existe"));
        return convertirADTO(comuna);
    }

    public Comuna guardarComuna(Comuna comuna) {
        return comunaRepository.save(comuna);
    }

    private ComunaDTO convertirADTO(Comuna comuna) {
        ComunaDTO dto = new ComunaDTO();
        dto.setIdComuna(comuna.getIdComuna());
        dto.setNombre(comuna.getNombre());
        if (comuna.getRegion() != null) {
            dto.setNombreRegion(comuna.getRegion().getNombre());
        }
        return dto;
    }
}