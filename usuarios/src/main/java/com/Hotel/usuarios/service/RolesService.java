package com.Hotel.usuarios.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.Hotel.usuarios.DTO.RolesDTO;
import com.Hotel.usuarios.model.Roles;
import com.Hotel.usuarios.repository.RolesRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RolesService {

    @Autowired
    public RolesRepository rolesRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;


    public List<RolesDTO> obtenerTodas() {
        return rolesRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public RolesDTO buscarPorId(Integer id_roles) {
        List<Roles> listaRoles = rolesRepository.findByIdroles(id_roles);
        if (listaRoles.isEmpty()) {
            throw new RuntimeException("El rol con ID " + id_roles + " no existe.");
        }
        Roles roles = listaRoles.get(0);
        return convertirADTO(roles);
    }

    public List<RolesDTO> buscarPorNombre(String nombre){
        return rolesRepository.findByNombre(nombre).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Roles guardarRol(Roles roles) {
        return rolesRepository.save(roles);
    }

    public String eliminar(Integer id) {
        try {
            List<Roles> listaRoles = rolesRepository.findByIdroles(id);
            if (listaRoles.isEmpty()) {
                throw new RuntimeException("El rol con ID " + id + " no existe.");
            }
            
            Roles roles = listaRoles.get(0);
            rolesRepository.delete(roles);
            return "El rol '" + roles.getNombre() + "' ha sido eliminado exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private RolesDTO convertirADTO(Roles roles) {
    RolesDTO dto = new RolesDTO();
    
    dto.setIdRoles(roles.getIdroles());
    dto.setNombre(roles.getNombre());

    if (roles.getUsuario() != null) {
        dto.setNombreUsuarios(roles.getUsuario().getNombre());
    } else {
        dto.setNombreUsuarios("No hay usuarios asignados al rol");
    }
    
    return dto;
    }

}