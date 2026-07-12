package com.Hotel.hoteles.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.Hotel.hoteles.DTO.RegionDTO;
import com.Hotel.hoteles.controller.v2.RegionController;

@Component
public class RegionModelAssembler implements RepresentationModelAssembler <RegionDTO, EntityModel<RegionDTO>>{

    @Override
    public EntityModel<RegionDTO> toModel(RegionDTO region){
        return EntityModel.of(region,
            linkTo(methodOn(RegionController.class).porId(region.getIdRegion())).withSelfRel(),
            linkTo(methodOn(RegionController.class).todasLasRegiones()).withRel("regiones")
        );
    }
}
