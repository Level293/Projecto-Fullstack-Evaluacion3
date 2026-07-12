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

import com.Hotel.hoteles.DTO.RegionDTO;
import com.Hotel.hoteles.Service.RegionService;
import com.Hotel.hoteles.assembler.RegionModelAssembler;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController("RegionControllerV2")
@RequestMapping("api/v2/regiones")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @Autowired
    private RegionModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<RegionDTO>>> todasLasRegiones() {
        List<EntityModel<RegionDTO>> region = regionService.obtenerTodo().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        if (region.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
            region,
            linkTo(methodOn(RegionController.class).todasLasRegiones()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RegionDTO>> porId(@PathVariable Integer id) {
        try {
            RegionDTO dto = regionService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}