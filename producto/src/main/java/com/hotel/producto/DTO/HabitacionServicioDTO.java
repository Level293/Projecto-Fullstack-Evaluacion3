package com.hotel.producto.DTO;

import lombok.Data;

@Data
public class HabitacionServicioDTO {

    private Integer IdHabservicio;

    private Integer numeroHabitacion;

    private String nombreServicio;

    private Double precioServicio;
}
