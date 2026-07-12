package com.hotel.producto.DTO;

import lombok.Data;

@Data
public class HabitacionDTO {

    private Integer idHabitacion;

    private Integer idHotel;

    private Integer numero;

    private String estado;
    
    private TipoHabitacionDTO tipoHabitacion;
}
