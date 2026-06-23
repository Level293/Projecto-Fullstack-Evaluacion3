package com.Hotel.usuarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Hotel.usuarios.model.Pago;

@Repository
public interface PagoRepository extends JpaRepository <Pago , Integer> {

    List<Pago> findByReserva_idReserva(Integer idReserva);
    
    List<Pago> findByMetodo(String metodo);
    
}
