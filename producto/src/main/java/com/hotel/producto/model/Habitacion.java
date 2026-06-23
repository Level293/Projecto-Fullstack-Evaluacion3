package com.hotel.producto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "habitacion")
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Habitacion")
    private Integer idHabitacion;
    
    @NotNull(message = "El ID del hotel es obligatorio")
    @Column(name = "id_hotel", nullable = false)
    private Integer idHotel;

    @ManyToOne
    @JoinColumn(name = "id_tipo_hab", nullable = false)
    private TipoHabitacion tipoHabitacion;

    @NotNull(message = "El numero de habitacion no puede ser nulo")
    @Column(nullable = false)
    private Integer numero;

    @NotBlank(message = "El estado no puede estar en blanco")
    @Column(nullable = false, length = 20)
    private String estado;

}
