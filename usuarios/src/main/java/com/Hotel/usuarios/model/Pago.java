package com.Hotel.usuarios.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_pago")
    private Integer idPago; 

    @ManyToOne
    @JoinColumn(name = "ID_reserva", nullable = false) 
    private Reservas reserva; 

    @NotNull(message = "El monto no puede ser nulo")
    @Column(nullable = false)
    private Integer monto;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @NotBlank(message = "El metodo de pago no puede ser nulo")
    @Column(nullable = false, length = 20)
    private String metodo;

    @NotNull(message = "El estado no puede ser nulo") 
    @Column(nullable = false)
    private Boolean estado;


}
