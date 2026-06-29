package com.Hotel.usuarios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Hotel.usuarios.model.Usuario;
import com.Hotel.usuarios.repository.RolesRepository;
import com.Hotel.usuarios.repository.UsuarioRepository;

@Component
public class UsuarioValidaciones {

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean validarRegrasNegocio(Usuario usuario) {
        if (usuario.getCorreo() != null && !usuario.getCorreo().contains("@")) {
            return false;
        }
        
        return true;
    }

    public boolean existeRolAsignado(Usuario usuario) {
        if (usuario.getRoles() == null || usuario.getRoles().getIdroles() == null) { 
            return false;
        }
        return rolesRepository.existsById(usuario.getRoles().getIdroles());
    }

    public boolean elCorreoYaExiste(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return false;
        }
        return usuarioRepository.existsByCorreo(correo);
    }
}