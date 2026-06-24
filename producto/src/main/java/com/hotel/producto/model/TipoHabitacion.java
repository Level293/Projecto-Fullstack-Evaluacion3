package com.hotel.producto.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tipo_habitacion")
public class TipoHabitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo")
    private Integer idTipo;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "capacidad")
    private Integer capacidad;

    @Column(name = "precio")
    private Integer precio;

    @OneToMany(mappedBy = "tipoHabitacion")
    private List<Habitacion> habitaciones;
}


