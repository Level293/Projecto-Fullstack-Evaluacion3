package com.hotel.producto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reserva_habitacion")
public class ReservaHabitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Reserva_Hab")
    private Integer idReservaHab;

    @NotNull(message = "El precio por noche no puede ser nulo")
    @Column(nullable = false)
    private Integer precioNoche;

    
    @NotNull(message = "El ID de la reserva es obligatorio")
    @Column(name = "ID_reserva", nullable = false) 
    private Integer idReserva;

    @ManyToOne 
    @JoinColumn(name = "ID_Habitacion", nullable = false) 
    private Habitacion habitacion;

}
