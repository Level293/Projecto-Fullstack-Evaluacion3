package com.Hotel.usuarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Hotel.usuarios.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
    List<Usuario> findByIdUsuario(Integer idUsuario);

    List<Usuario> findByCorreo(String correo);

    @Query("SELECT u FROM Usuario u WHERE u.idUsuario = :id")
    Usuario buscarPorId(@Param("id") Integer id);

    boolean existsByCorreo(String correo);
}