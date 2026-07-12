package com.Hotel.hoteles.controller.v2;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Hotel.hoteles.DTO.HotelDTO;
import com.Hotel.hoteles.Repository.HotelRepository;
import com.Hotel.hoteles.Service.HotelService;
import com.Hotel.hoteles.assembler.HotelModelAssembler;
import com.Hotel.hoteles.model.Hotel;
import com.netflix.discovery.converters.Auto;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController("HotelControllerV2")
@RequestMapping("api/v2/hoteles")
public class HotelController {

    @Autowired
    public HotelService hotelService;

    @Autowired
    public HotelModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<HotelDTO>>> todosLosHoteles() {
        List<EntityModel<HotelDTO>> hotel = hotelService.obtenerTodo().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        if (hotel.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
            hotel,
            linkTo(methodOn(HotelController.class).todosLosHoteles()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<HotelDTO>> porId(@PathVariable Integer id) {
        try {
            HotelDTO dto = hotelService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/nombre/{nombre}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<HotelDTO>>> porNombre(@PathVariable String nombre) {
        try {
            List<EntityModel<HotelDTO>> hotelPorNombre = hotelService.buscarPorNombre(nombre).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

            if (hotelPorNombre.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(CollectionModel.of(
                hotelPorNombre,
                linkTo(methodOn(HotelController.class).porNombre(nombre)).withSelfRel()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
}
