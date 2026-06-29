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

import com.Hotel.usuarios.DTO.PagoDTO;
import com.Hotel.usuarios.assembler.PagosModelAssembler;
import com.Hotel.usuarios.model.Pago;
import com.Hotel.usuarios.repository.PagoRepository;
import com.Hotel.usuarios.service.PagoService;

import jakarta.validation.Valid;

@RestController("PagosControllerV2")
@RequestMapping("api/v2/pagos")
public class PagosController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private PagosModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<PagoDTO>>> todosLosPagos() {
        List<EntityModel<PagoDTO>> pagos = pagoService.obtenerTodo().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        if (pagos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Corregido: Auto-referencia apunta a PagosController
        return ResponseEntity.ok(CollectionModel.of(
            pagos,
            linkTo(methodOn(PagosController.class).todosLosPagos()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PagoDTO>> porId(@PathVariable Integer id) {
        try {
            PagoDTO dto = pagoService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/metodo/{metodo}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<PagoDTO>>> porMetodo(@PathVariable String metodo) {
        try {
            List<EntityModel<PagoDTO>> pagosPorMetodo = pagoService.buscarPorMetodo(metodo).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

            if (pagosPorMetodo.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(CollectionModel.of(
                pagosPorMetodo,
                linkTo(methodOn(PagosController.class).porMetodo(metodo)).withSelfRel()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PagoDTO>> registrar(@Valid @RequestBody Pago pagos) {
        try {
            // Guarda la entidad utilizando tu servicio de pagos
            Pago entidadGuardada = pagoService.guardarPago(pagos);
            
            // Busca la entidad guardada mediante su ID para transformarlo a un DTO limpio
            PagoDTO newPagoDTO = pagoService.buscarPorId(entidadGuardada.getIdPago());
            
            return ResponseEntity
                .created(linkTo(methodOn(PagosController.class).porId(newPagoDTO.getIdPago())).toUri())
                .body(assembler.toModel(newPagoDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (pagoRepository.existsById(id)) {
            pagoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}