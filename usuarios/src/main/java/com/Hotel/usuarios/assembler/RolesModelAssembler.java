package com.Hotel.usuarios.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.Hotel.usuarios.DTO.RolesDTO;
import com.Hotel.usuarios.controller.v2.RolesController;

@Component
public class RolesModelAssembler implements RepresentationModelAssembler <RolesDTO, EntityModel<RolesDTO>>{

    @Override
    public EntityModel<RolesDTO> toModel(RolesDTO roles) {
        return EntityModel.of(roles,
            linkTo(methodOn(RolesController.class).porId(roles.getIdRoles())).withSelfRel(),
            linkTo(methodOn(RolesController.class).porNombre(roles.getNombre())).withSelfRel(),
            linkTo(methodOn(RolesController.class).todosLosRoles()).withRel("roles"),
            linkTo(methodOn(RolesController.class).eliminar(roles.getIdRoles())).withRel("eliminar")
        );
    }
}
