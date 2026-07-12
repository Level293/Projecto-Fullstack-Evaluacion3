package com.Hotel.usuarios.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.Hotel.usuarios.DTO.UsuarioDTO;
import com.Hotel.usuarios.controller.v2.UsuariosController;



@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler <UsuarioDTO, EntityModel<UsuarioDTO>> {

    @Override
    public EntityModel<UsuarioDTO> toModel(UsuarioDTO usuario) {
        return EntityModel.of(usuario,
            linkTo(methodOn(UsuariosController.class).porId(usuario.getIdUsuario())).withSelfRel(),
            linkTo(methodOn(UsuariosController.class).porCorreo(usuario.getCorreo())).withSelfRel(),
            linkTo(methodOn(UsuariosController.class).todosLosUsuarios()).withRel("usuarios"),
            linkTo(methodOn(UsuariosController.class).eliminar(usuario.getIdUsuario())).withRel("eliminar")
            
        );
    }

}
