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
import org.springframework.web.bind.annotation.RequestBody; // CORRECCIÓN: Importación correcta de Spring Framework
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.producto.DTO.ServicioDTO;
import com.hotel.producto.Repository.ServicioRepository;
import com.hotel.producto.Services.ServicioService;
import com.hotel.producto.assembler.ServicioModelAssembler;
import com.hotel.producto.model.Servicio;

import jakarta.validation.Valid;

@RestController("ServicioControllerV2")
@RequestMapping("api/v2/servicio")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private ServicioModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ServicioDTO>>> todosLosServicios() {
        List<EntityModel<ServicioDTO>> servicio = servicioService.obtenerTodo().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        if (servicio.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
            servicio,
            linkTo(methodOn(ServicioController.class).todosLosServicios()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ServicioDTO>> porId(@PathVariable Integer id) {
        try {
            ServicioDTO dto = servicioService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ServicioDTO>> registrar(@Valid @RequestBody Servicio servicio) {
        try {
            // CORRECCIÓN 1: Se captura el retorno como 'Servicio' (Entidad) que es lo que devuelve tu capa Service
            Servicio entidadGuardada = servicioService.guardarServicio(servicio);
            
            // CORRECCIÓN 2: Convertimos la entidad guardada a DTO mediante tu método buscarPorId
            // (Si tu entidad usa getId() en vez de getIdServicio(), ajusta ese getter)
            ServicioDTO newServicioDTO = servicioService.buscarPorId(entidadGuardada.getIdServicio());
            
            // CORRECCIÓN 3: Pasamos el DTO correcto al constructor de la URI y al body del assembler
            return ResponseEntity
                .created(linkTo(methodOn(ServicioController.class).porId(newServicioDTO.getIdServicio())).toUri())
                .body(assembler.toModel(newServicioDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (servicioRepository.existsById(id)) {
            servicioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}