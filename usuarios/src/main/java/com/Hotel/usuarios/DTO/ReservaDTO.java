package com.Hotel.usuarios.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReservaDTO {
    private Integer idReserva;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private String estado;

    private Integer total;
    
}
