package com.Hotel.usuarios.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.Hotel.usuarios.DTO.ReservaDTO;
import com.Hotel.usuarios.controller.v2.ReservasController;

@Component
public class ReservasModelAssembler implements RepresentationModelAssembler <ReservaDTO, EntityModel<ReservaDTO>> {

    @Override
    public EntityModel<ReservaDTO> toModel(ReservaDTO reserva) {
        return EntityModel.of(reserva,
            linkTo(methodOn(ReservasController.class).porId(reserva.getIdReserva())).withSelfRel(),
            linkTo(methodOn(ReservasController.class).porEstado(reserva.getEstado())).withSelfRel(),
            linkTo(methodOn(ReservasController.class).todasLasReservas()).withRel("reservas"),
            linkTo(methodOn(ReservasController.class).eliminar(reserva.getIdReserva())).withRel("eliminar")
        );
    }

}
