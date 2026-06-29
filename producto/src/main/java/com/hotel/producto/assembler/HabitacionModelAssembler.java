package com.hotel.producto.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.hotel.producto.Controller.v2.HabitacionController;
import com.hotel.producto.DTO.HabitacionDTO;

@Component
public class HabitacionModelAssembler implements RepresentationModelAssembler<HabitacionDTO, EntityModel<HabitacionDTO>> {

    @Override
    public EntityModel<HabitacionDTO> toModel(HabitacionDTO habitacion) {
        return EntityModel.of(habitacion,
            linkTo(methodOn(HabitacionController.class).porId(habitacion.getIdHabitacion())).withSelfRel(),
            // Corrección aquí: apuntar al nuevo mapeo calificado de estado
            linkTo(methodOn(HabitacionController.class).porEstado(habitacion.getEstado())).withRel("por-estado"),
            linkTo(methodOn(HabitacionController.class).todasLasHabitaciones()).withRel("habitaciones"),
            linkTo(methodOn(HabitacionController.class).eliminar(habitacion.getIdHabitacion())).withRel("eliminar")
        );
    }
}