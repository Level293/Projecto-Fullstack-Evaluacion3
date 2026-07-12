package com.Hotel.hoteles.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.Hotel.hoteles.DTO.ComunaDTO;
import com.Hotel.hoteles.controller.v2.ComunaController;

@Component
public class ComunaModelAssembler implements RepresentationModelAssembler<ComunaDTO, EntityModel<ComunaDTO>> {

    @Override
    public EntityModel<ComunaDTO> toModel(ComunaDTO comuna){
        return EntityModel.of(comuna,
            linkTo(methodOn(ComunaController.class).porId(comuna.getIdComuna())).withSelfRel(),
            linkTo(methodOn(ComunaController.class).todasLasComunas()).withRel("comunas")
        );
    }
}
