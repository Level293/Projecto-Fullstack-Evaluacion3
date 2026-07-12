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

import com.Hotel.hoteles.DTO.ComunaDTO;
import com.Hotel.hoteles.Service.ComunaService;
import com.Hotel.hoteles.assembler.ComunaModelAssembler;
import com.Hotel.hoteles.model.Comuna;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController("ComunaControllerV2")
@RequestMapping("api/v2/comunas")
public class ComunaController {

    @Autowired
    private ComunaService comunaService;

    @Autowired
    private ComunaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ComunaDTO>>> todasLasComunas() {
        List<EntityModel<ComunaDTO>> comuna = comunaService.obtenerTodo().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        if (comuna.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
            comuna,
            linkTo(methodOn(ComunaController.class).todasLasComunas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ComunaDTO>> porId(@PathVariable Integer id) {
        try {
            ComunaDTO dto = comunaService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

