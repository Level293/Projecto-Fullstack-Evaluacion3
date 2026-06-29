package com.Hotel.usuarios.controller.v2;

import com.Hotel.usuarios.repository.UsuarioRepository;
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

import com.Hotel.usuarios.DTO.UsuarioDTO;
import com.Hotel.usuarios.assembler.UsuarioModelAssembler;
import com.Hotel.usuarios.model.Usuario;
import com.Hotel.usuarios.service.UsuarioService;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController("UsuariosControllerV2")
@RequestMapping("api/v2/usuarios")
public class UsuariosController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<UsuarioDTO>>> todosLosUsuarios() {
        List<EntityModel<UsuarioDTO>> usuarios = usuarioService.obtenerTodos().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
            usuarios,
            linkTo(methodOn(UsuariosController.class).todosLosUsuarios()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<UsuarioDTO>> porId(@PathVariable Integer id) {
        try {
            UsuarioDTO dto = usuarioService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Cambiado el tipo de retorno a CollectionModel porque tu servicio devuelve una List<UsuarioDTO>
    @GetMapping(value = "/correo/{correo}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<UsuarioDTO>>> porCorreo(@PathVariable String correo) {
        try {
            List<EntityModel<UsuarioDTO>> usuariosPorCorreo = usuarioService.buscarPorCorreo(correo).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

            if (usuariosPorCorreo.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(CollectionModel.of(
                usuariosPorCorreo,
                linkTo(methodOn(UsuariosController.class).porCorreo(correo)).withSelfRel()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<UsuarioDTO>> registrar(@Valid @RequestBody Usuario usuario) {
        try {
            // CORRECCIÓN 2: Capturamos el retorno real de la capa Service como entidad 'Usuario'
            Usuario entidadGuardada = usuarioService.guardarUsuario(usuario);
            
            // CORRECCIÓN 3: Buscamos por el ID real de la entidad guardada para pasarlo a DTO limpio.
            UsuarioDTO newUsuarioDTO = usuarioService.buscarPorId(entidadGuardada.getIdUsuario());
            
            return ResponseEntity
                .created(linkTo(methodOn(UsuariosController.class).porId(newUsuarioDTO.getIdUsuario())).toUri())
                .body(assembler.toModel(newUsuarioDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}