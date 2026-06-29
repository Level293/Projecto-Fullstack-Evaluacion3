package com.Hotel.usuarios.controller.v2;

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

import com.Hotel.usuarios.DTO.ReservaDTO;
import com.Hotel.usuarios.assembler.ReservasModelAssembler;
import com.Hotel.usuarios.model.Reservas;
import com.Hotel.usuarios.repository.ReservasRepository;
import com.Hotel.usuarios.service.ReservasService;

import jakarta.validation.Valid;

@RestController("ReservasControllerv2")
@RequestMapping("api/v2/reservas")
public class ReservasController {

    @Autowired
    private ReservasService reservasService;

    @Autowired
    private ReservasRepository reservasRepository;

    @Autowired
    private ReservasModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ReservaDTO>>> todasLasReservas() {
        List<EntityModel<ReservaDTO>> reservas = reservasService.obtenerTodas().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        if (reservas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
            reservas,
            linkTo(methodOn(ReservasController.class).todasLasReservas()).withSelfRel()
        ));
    }

    // CORRECCIÓN 1: Se cambió el tipo de retorno de 'Reservas' a 'ReservaDTO' para que coincida con el assembler
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ReservaDTO>> porId(@PathVariable Integer id) {
        try {
            ReservaDTO dto = reservasService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // CORRECCIÓN 2: Ruta cambiada a '/estado/{estado}' para evitar ambigüedad con '/{id}'
    // Cambiado el retorno a CollectionModel porque el servicio devuelve una Lista de reservas
    @GetMapping(value = "/estado/{estado}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ReservaDTO>>> porEstado(@PathVariable String estado) {
        try {
            List<EntityModel<ReservaDTO>> reservasPorEstado = reservasService.buscarPorEstado(estado).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

            if (reservasPorEstado.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(CollectionModel.of(
                reservasPorEstado,
                linkTo(methodOn(ReservasController.class).porEstado(estado)).withSelfRel()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ReservaDTO>> registrar(@Valid @RequestBody Reservas reservas) {
        try {
            // CORRECCIÓN 3: Capturamos la respuesta como la entidad pura 'Reservas' que envía tu servicio
            Reservas entidadGuardada = reservasService.guardarReservas(reservas);
            
            // Convertimos la entidad guardada a DTO haciendo uso de tu método buscarPorId
            // (Si en tu entidad el método es getId() en vez de getIdReserva(), cámbialo abajo)
            ReservaDTO newReservaDTO = reservasService.buscarPorId(entidadGuardada.getIdReserva());
            
            return ResponseEntity
                .created(linkTo(methodOn(ReservasController.class).porId(newReservaDTO.getIdReserva())).toUri())
                .body(assembler.toModel(newReservaDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (reservasRepository.existsById(id)) {
            reservasRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}