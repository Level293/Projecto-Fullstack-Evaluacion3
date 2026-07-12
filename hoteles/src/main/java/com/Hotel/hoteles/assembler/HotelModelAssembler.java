package com.Hotel.hoteles.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.Hotel.hoteles.DTO.HotelDTO;
import com.Hotel.hoteles.controller.v2.HotelController;

@Component
public class HotelModelAssembler implements RepresentationModelAssembler<HotelDTO, EntityModel<HotelDTO>> {

    @Override
    public EntityModel<HotelDTO> toModel(HotelDTO hotel){
        return EntityModel.of(hotel,
            linkTo(methodOn(HotelController.class).porId(hotel.getIdHotel())).withSelfRel(),
            linkTo(methodOn(HotelController.class).porNombre(hotel.getNombre())).withSelfRel(),
            linkTo(methodOn(HotelController.class).todosLosHoteles()).withRel("hoteles")
        );
    }
}
