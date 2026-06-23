package com.Hotel.usuarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Hotel.usuarios.model.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {
    
    List<Roles> findByIdroles(Integer idRoles);

    List<Roles> findByNombre(String nombre);


}

