package com.hotel.producto.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.hotel.producto.Controller.v2.TipoHabitacionController;
import com.hotel.producto.DTO.TipoHabitacionDTO;

@Component
public class TipoHabitacionModelAssembler implements RepresentationModelAssembler <TipoHabitacionDTO, EntityModel<TipoHabitacionDTO>>{

    @Override
    public EntityModel<TipoHabitacionDTO> toModel(TipoHabitacionDTO th) {
        return EntityModel.of(th,
            linkTo(methodOn(TipoHabitacionController.class).porId(th.getIdTipo())).withSelfRel(),
            linkTo(methodOn(TipoHabitacionController.class).todosLosTipos()).withRel("tipohabitaciones"),
            linkTo(methodOn(TipoHabitacionController.class).eliminar(th.getIdTipo())).withRel("eliminar")
        );
    }
}
