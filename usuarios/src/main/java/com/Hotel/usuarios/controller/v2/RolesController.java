package com.Hotel.usuarios.controller.v2;

import com.Hotel.usuarios.repository.RolesRepository;

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

import com.Hotel.usuarios.DTO.RolesDTO;
import com.Hotel.usuarios.assembler.RolesModelAssembler;
import com.Hotel.usuarios.model.Roles;
import com.Hotel.usuarios.service.RolesService;

import jakarta.validation.Valid;

@RestController("RolesControllerV2")
@RequestMapping("api/v2/roles")
public class RolesController {

    @Autowired
    private RolesService rolesService;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private RolesModelAssembler assembler;


    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<RolesDTO>>> todosLosRoles() {
        List<EntityModel<RolesDTO>> roles = rolesService.obtenerTodas().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        if (roles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
            roles,
            linkTo(methodOn(RolesController.class).todosLosRoles()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RolesDTO>> porId(@PathVariable Integer id) {
        try {
            RolesDTO dto = rolesService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/nombre/{nombre}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<RolesDTO>>> porNombre(@PathVariable String nombre) {
        try {
            List<EntityModel<RolesDTO>> rolesPorNombre = rolesService.buscarPorNombre(nombre).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

            if (rolesPorNombre.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(CollectionModel.of(
                rolesPorNombre,
                linkTo(methodOn(RolesController.class).porNombre(nombre)).withSelfRel()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RolesDTO>> registrar(@Valid @RequestBody Roles roles) {
        try {
            Roles entidadGuardada = rolesService.guardarRol(roles);
            
            RolesDTO newRolDTO = rolesService.buscarPorId(entidadGuardada.getIdroles());
            
            return ResponseEntity
                .created(linkTo(methodOn(RolesController.class).porId(newRolDTO.getIdRoles())).toUri())
                .body(assembler.toModel(newRolDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (rolesRepository.existsById(id)) {
            rolesRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}