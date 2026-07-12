package com.Hotel.usuarios.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.Hotel.usuarios.DTO.PagoDTO;
import com.Hotel.usuarios.controller.v2.PagosController;

@Component
public class PagosModelAssembler implements RepresentationModelAssembler <PagoDTO, EntityModel<PagoDTO>> {

    @Override
    public EntityModel<PagoDTO> toModel(PagoDTO pagos) {
        return EntityModel.of(pagos,
            linkTo(methodOn(PagosController.class).porId(pagos.getIdReserva())).withSelfRel(),
            linkTo(methodOn(PagosController.class).porMetodo(pagos.getMetodo())).withSelfRel(),
            linkTo(methodOn(PagosController.class).todosLosPagos()).withRel("pagos"),
            linkTo(methodOn(PagosController.class).eliminar(pagos.getIdPago())).withRel("eliminar")
        );
    }
}
