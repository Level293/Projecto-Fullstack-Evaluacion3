package com.hotel.producto.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.hotel.producto.Controller.v2.ServicioController;
import com.hotel.producto.DTO.ServicioDTO;

@Component
public class ServicioModelAssembler implements RepresentationModelAssembler <ServicioDTO, EntityModel<ServicioDTO>> {

    @Override
    public EntityModel<ServicioDTO> toModel (ServicioDTO servicio) {
        return EntityModel.of(servicio,
            linkTo(methodOn(ServicioController.class).porId(servicio.getIdServicio())).withSelfRel(),
            linkTo(methodOn(ServicioController.class).todosLosServicios()).withRel("servicios"),
            linkTo(methodOn(ServicioController.class).eliminar(servicio.getIdServicio())).withRel("eliminar")
        );
    }
}
