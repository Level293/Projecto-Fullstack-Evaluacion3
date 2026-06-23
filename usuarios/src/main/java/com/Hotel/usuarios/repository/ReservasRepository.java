package com.Hotel.usuarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Hotel.usuarios.model.Reservas;

@Repository
public interface ReservasRepository extends JpaRepository <Reservas, Integer>{

    List<Reservas> findByIdReserva(Integer idReserva);

    List<Reservas> findByEstado(String estado);

}

