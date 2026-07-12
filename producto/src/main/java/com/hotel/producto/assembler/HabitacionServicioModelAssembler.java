package com.hotel.producto.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.hotel.producto.Controller.v2.HabitacionServicioController;
import com.hotel.producto.DTO.HabitacionServicioDTO;

@Component
public class HabitacionServicioModelAssembler implements RepresentationModelAssembler <HabitacionServicioDTO, EntityModel<HabitacionServicioDTO>> {

    @Override
    public EntityModel<HabitacionServicioDTO> toModel(HabitacionServicioDTO hs){
        return EntityModel.of(hs,
            linkTo(methodOn(HabitacionServicioController.class).porId(hs.getIdHabservicio())).withSelfRel(),
            linkTo(methodOn(HabitacionServicioController.class).todosLosServiciosDeHabitacion()).withRel("habitacionservicios"),
            linkTo(methodOn(HabitacionServicioController.class).eliminar(hs.getIdHabservicio())).withRel("eliminar")
        );
    }
    
}
