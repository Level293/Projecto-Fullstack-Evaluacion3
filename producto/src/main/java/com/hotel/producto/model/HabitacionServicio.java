package com.hotel.producto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hab_servicio")
public class HabitacionServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Hab_Servicio")
    private Integer idHabServicio;

    @ManyToOne
    @JoinColumn(name = "ID_habitacion", nullable = false)
    private Habitacion habitacion;

    @ManyToOne
    @JoinColumn(name = "ID_servicio", nullable = false)
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "id_tipo") 
    private TipoHabitacion tipoHabitacion;
    
}
