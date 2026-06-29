package com.hotel.producto.Controller.v2;

import com.hotel.producto.DTO.TipoHabitacionDTO;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.producto.Repository.TipoHabitacionRepository;
import com.hotel.producto.Services.TipoHabitacionService;
import com.hotel.producto.assembler.TipoHabitacionModelAssembler;
import com.hotel.producto.model.TipoHabitacion;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController("TipoHabitacionControllerV2")
@RequestMapping("api/v2/tipohabitacion")
public class TipoHabitacionController {

    @Autowired
    private TipoHabitacionService tipoHabitacionService;

    @Autowired
    private TipoHabitacionRepository tipoHabitacionRepository;

    @Autowired
    private TipoHabitacionModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<TipoHabitacionDTO>>> todosLosTipos() {
        List<EntityModel<TipoHabitacionDTO>> th = tipoHabitacionService.obtenerTodo().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        if (th.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
            th,
            linkTo(methodOn(TipoHabitacionController.class).todosLosTipos()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TipoHabitacionDTO>> porId(@PathVariable Integer id) {
        try {
            TipoHabitacionDTO dto = tipoHabitacionService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel((dto)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TipoHabitacionDTO>> registrar(@Valid @RequestBody TipoHabitacion th) {
        try {
            TipoHabitacionDTO newTh = tipoHabitacionService.guardarTipo(th);
            return ResponseEntity
                .created(linkTo(methodOn(TipoHabitacionController.class).porId(newTh.getIdTipo())).toUri())
                .body(assembler.toModel(newTh));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (tipoHabitacionRepository.existsById(id)) {
            tipoHabitacionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
