package com.hotel.producto.DTO;

import lombok.Data;

@Data
public class ReservaHabitacionDTO {

    private Integer idReservaHab;

    private Integer precioNoche;
    
    private Integer idReserva; 
    
    private HabitacionDTO habitacion;
}
