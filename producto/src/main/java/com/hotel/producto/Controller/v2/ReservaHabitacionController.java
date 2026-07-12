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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.producto.DTO.ReservaHabitacionDTO;
import com.hotel.producto.Repository.ReservaHabitacionRepository;
import com.hotel.producto.Services.ReservaHabitacionService;
import com.hotel.producto.assembler.ReservaHabitacionModelAssembler;
import com.hotel.producto.model.ReservaHabitacion;

import jakarta.validation.Valid;

@RestController("ReservaHabitacionControllerV2")
@RequestMapping("api/v2/reservahabitacion")
public class ReservaHabitacionController {

    @Autowired
    private ReservaHabitacionService reservaHabitacionService;

    @Autowired
    private ReservaHabitacionRepository reservaHabitacionRepository;

    @Autowired
    private ReservaHabitacionModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ReservaHabitacionDTO>>> todasLasReservasHabitacion() {
        List<EntityModel<ReservaHabitacionDTO>> rh = reservaHabitacionService.obtenerTodos().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        if (rh.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
            rh,
            linkTo(methodOn(ReservaHabitacionController.class).todasLasReservasHabitacion()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ReservaHabitacionDTO>> porId(@PathVariable Integer id) {
        try {
            ReservaHabitacionDTO dto = reservaHabitacionService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/precio/{precioNoche}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ReservaHabitacionDTO>>> porNoche(@PathVariable Integer precioNoche) {
        try {
            List<EntityModel<ReservaHabitacionDTO>> reservas = reservaHabitacionService.buscarPorPrecioNoche(precioNoche).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

            if (reservas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(CollectionModel.of(
                reservas,
                linkTo(methodOn(ReservaHabitacionController.class).porNoche(precioNoche)).withSelfRel()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ReservaHabitacionDTO>> registrar(@Valid @RequestBody ReservaHabitacion rh) {
        try {
            ReservaHabitacion entidadGuardada = reservaHabitacionService.guardarReservaHab(rh);
            
            ReservaHabitacionDTO newRh = reservaHabitacionService.buscarPorId(entidadGuardada.getIdReservaHab());
            
            return ResponseEntity
                .created(linkTo(methodOn(ReservaHabitacionController.class).porId(newRh.getIdReservaHab())).toUri())
                .body(assembler.toModel(newRh));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (reservaHabitacionRepository.existsById(id)) {
            reservaHabitacionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}