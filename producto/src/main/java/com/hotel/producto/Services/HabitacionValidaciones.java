package com.hotel.producto.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hotel.producto.DTO.HabitacionDTO;
import com.hotel.producto.Repository.HabitacionRepository;
import com.hotel.producto.Repository.TipoHabitacionRepository;

@Component 
public class HabitacionValidaciones {

    @Autowired
    private TipoHabitacionRepository tipoHabitacionRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    public boolean validarCamposHabitacion(HabitacionDTO dto) {
        if (dto.getNumero() == null || dto.getNumero() <= 0) {
            return false;
        }
        if (dto.getEstado() == null || dto.getEstado().trim().isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean existeTipoHabitacion(Integer idTipoHab) {
        if (idTipoHab == null) {
            return false;
        }
        return tipoHabitacionRepository.existsById(idTipoHab);
    }

    public boolean elNumeroYaExiste(Integer numero) {
        if (numero == null) {
            return false;
        }
        return habitacionRepository.existsByNumero(numero);
    }

    // Soporta el precio decimal de TipoHabitacion
    public boolean elPrecioEsValido(Integer precio) {
        if (precio == null || precio <= 0) {
            return false;
        }
        return true;
    }
}