package com.hotel.producto.Controller.v2;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotel.producto.DTO.HabitacionServicioDTO;
import com.hotel.producto.Repository.HabitacionServicioRepository;
import com.hotel.producto.Services.HabitacionServicioService;
import com.hotel.producto.assembler.HabitacionServicioModelAssembler;
import com.hotel.producto.model.HabitacionServicio;

import jakarta.validation.Valid;

@RestController("HabitacionServicioControllerV2")
@RequestMapping("api/v2/habitacionservicio")
public class HabitacionServicioController {

    @Autowired
    private HabitacionServicioService habitacionServicioService;

    @Autowired
    private HabitacionServicioRepository habitacionServicioRepository;
    
    @Autowired
    private HabitacionServicioModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<HabitacionServicioDTO>>> todosLosServiciosDeHabitacion() {
        List<EntityModel<HabitacionServicioDTO>> hs = habitacionServicioService.obtenerTodo().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        if (hs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
            hs,
            linkTo(methodOn(HabitacionServicioController.class).todosLosServiciosDeHabitacion()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<HabitacionServicioDTO>> porId(@PathVariable Integer id) {
        try {
            HabitacionServicioDTO dto = habitacionServicioService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<HabitacionServicioDTO>> registrar(@Valid @RequestBody HabitacionServicio hs) {
        try {
            HabitacionServicio entidadGuardada = habitacionServicioService.guardarHabitacionServicio(hs);
            
            HabitacionServicioDTO dtoResponse = habitacionServicioService.buscarPorId(entidadGuardada.getIdHabServicio());

            return ResponseEntity
                .created(linkTo(methodOn(HabitacionServicioController.class).porId(dtoResponse.getIdHabservicio())).toUri())
                .body(assembler.toModel(dtoResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (habitacionServicioRepository.existsById(id)) {
            habitacionServicioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}