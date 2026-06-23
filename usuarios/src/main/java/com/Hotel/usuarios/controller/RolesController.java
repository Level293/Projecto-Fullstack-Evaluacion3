package com.Hotel.usuarios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Hotel.usuarios.DTO.RolesDTO;
import com.Hotel.usuarios.model.Roles;
import com.Hotel.usuarios.service.RolesService;



@RestController
@RequestMapping("/api/v1/roles")
public class RolesController {

    @Autowired
    public RolesService rolesService;

    @GetMapping
    public ResponseEntity<List<RolesDTO>> todosLosRoles(){
        List<RolesDTO> roles = rolesService.obtenerTodas();
        if (roles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RolesDTO> buscarPorId(@PathVariable Integer id) {
        try {
            RolesDTO roles = rolesService.buscarPorId(id);
            return new ResponseEntity<>(roles, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<RolesDTO>> buscarPorNombre(@PathVariable String nombre) {
        List<RolesDTO> roles = rolesService.buscarPorNombre(nombre);
        if (roles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Roles> agregarRoles(@RequestBody Roles roles) {
        try {
            Roles guardado = rolesService.guardarRol(roles);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarRoles(@PathVariable Integer id) {
        String resultado = rolesService.eliminar(id);
        
        // Si el mensaje contiene "exitosamente", es un éxito
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
    
}

