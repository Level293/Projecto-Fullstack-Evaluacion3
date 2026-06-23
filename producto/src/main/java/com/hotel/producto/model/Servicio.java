package com.hotel.producto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Servicio")
    private Integer idServicio;

    @NotBlank(message = "El nombre del servicio no puede estar en blanco")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotNull(message = "El precio del servicio no puede ser nulo")
    @Column(nullable = false)
    private Double precio;
}
