package com.hotel.producto.DTO;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ReservaExternoDTO {

    private Integer idReserva;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private String estado;

    private Integer total;

    private Integer idUsuario;
}
