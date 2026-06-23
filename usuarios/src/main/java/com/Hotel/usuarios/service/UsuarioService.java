package com.Hotel.usuarios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.Hotel.usuarios.DTO.UsuarioDTO;
import com.Hotel.usuarios.model.Usuario;
import com.Hotel.usuarios.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;


    public List<UsuarioDTO> obtenerTodos(){
        return usuarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public UsuarioDTO buscarPorId(Integer id_usuario){
        Usuario usuario = usuarioRepository.findById(id_usuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertirADTO(usuario);
    }

    public List<UsuarioDTO> buscarPorCorreo(String correo){
        return usuarioRepository.findByCorreo(correo).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public String eliminar(Integer id) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("El usuario con ID " + id + " no existe."));
            usuarioRepository.delete(usuario);
            return "El usuario '" + usuario.getNombre() + "' ha sido eliminado exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setCorreo(usuario.getCorreo());

        if (usuario.getRoles() != null) {
            dto.setNombreRol(usuario.getRoles().getNombre());
        }else{
            dto.setNombreRol("No hay rol asignado :(");
        }
        
        return dto;
    }

}

