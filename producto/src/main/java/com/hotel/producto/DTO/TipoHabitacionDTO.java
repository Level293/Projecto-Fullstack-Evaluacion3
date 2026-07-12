package com.hotel.producto.DTO;

import lombok.Data;

@Data
public class TipoHabitacionDTO {

    private Integer idTipo;

    private String nombre;

    private String descripcion;

    private Integer capacidad;
    
    private Integer precio;
}
