package com.Hotel.usuarios.DTO;

import lombok.Data;

@Data
public class PagoDTO {


    private Integer idPago;

    private Integer monto;

    private String metodo;

    private Boolean estado;

    private Integer idReserva;
}
