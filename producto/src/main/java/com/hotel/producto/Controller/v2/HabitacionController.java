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

import com.hotel.producto.DTO.HabitacionDTO;
import com.hotel.producto.Repository.HabitacionRepository;
import com.hotel.producto.Services.HabitacionService;
import com.hotel.producto.assembler.HabitacionModelAssembler;

import jakarta.validation.Valid;

@RestController("HabitacionControllerV2")
@RequestMapping("api/v2/habitaciones")
public class HabitacionController {

    @Autowired
    private HabitacionService habitacionService;

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private HabitacionModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<HabitacionDTO>>> todasLasHabitaciones() {
        List<EntityModel<HabitacionDTO>> habitacion = habitacionService.obtenerTodasLasHabitaciones().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        if (habitacion.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
            habitacion,
            linkTo(methodOn(HabitacionController.class).todasLasHabitaciones()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<HabitacionDTO>> porId(@PathVariable Integer id) {
        try {
            HabitacionDTO dto = habitacionService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/estado/{estado}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<HabitacionDTO>>> porEstado(@PathVariable String estado) {
        try {
            List<EntityModel<HabitacionDTO>> habitacionesPorEstado = habitacionService.buscarPorEstado(estado).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

            if (habitacionesPorEstado.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(CollectionModel.of(
                habitacionesPorEstado,
                linkTo(methodOn(HabitacionController.class).porEstado(estado)).withSelfRel()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<HabitacionDTO>> registrar(@Valid @RequestBody HabitacionDTO habitacionDTO) {
        try {
            HabitacionDTO newHabitacion = habitacionService.guardarHabitacion(habitacionDTO);
            return ResponseEntity
                .created(linkTo(methodOn(HabitacionController.class).porId(newHabitacion.getIdHabitacion())).toUri())
                .body(assembler.toModel(newHabitacion));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (habitacionRepository.existsById(id)) {
            habitacionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}