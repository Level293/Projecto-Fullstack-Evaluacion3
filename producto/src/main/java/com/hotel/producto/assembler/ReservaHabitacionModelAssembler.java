package com.hotel.producto.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.hotel.producto.Controller.v2.ReservaHabitacionController;
import com.hotel.producto.DTO.ReservaHabitacionDTO;

@Component
public class ReservaHabitacionModelAssembler implements RepresentationModelAssembler <ReservaHabitacionDTO, EntityModel<ReservaHabitacionDTO>> {

    @Override
    public EntityModel<ReservaHabitacionDTO> toModel (ReservaHabitacionDTO rh) {
        return EntityModel.of(rh,
            linkTo(methodOn(ReservaHabitacionController.class).porId(rh.getIdReservaHab())).withSelfRel(),
            linkTo(methodOn(ReservaHabitacionController.class).porNoche(rh.getPrecioNoche())).withSelfRel(),
            linkTo(methodOn(ReservaHabitacionController.class).todasLasReservasHabitacion()).withRel("reservasHabitaciones"),
            linkTo(methodOn(ReservaHabitacionController.class).eliminar(rh.getIdReservaHab())).withRel("eliminar")
        );
    }
}
