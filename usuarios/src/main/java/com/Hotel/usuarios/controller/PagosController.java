package com.Hotel.usuarios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Hotel.usuarios.DTO.PagoDTO;
import com.Hotel.usuarios.model.Pago;
import com.Hotel.usuarios.service.PagoService;



@RestController
@RequestMapping("/api/v1/pagos")
public class PagosController {

    @Autowired
    public PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<PagoDTO>> todoLosPagos(){
        List<PagoDTO> pagos = pagoService.obtenerTodo();
        if(pagos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(pagos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> buscarPorId(@PathVariable Integer id) {
        try {
            PagoDTO pago = pagoService.buscarPorId(id);
            return new ResponseEntity<>(pago, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/metodo/{metodo}")
    public ResponseEntity<List<PagoDTO>> buscarPorMetodo(@PathVariable String metodo) {
        List<PagoDTO> pagos = pagoService.buscarPorMetodo(metodo);
        
        if (pagos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(pagos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Pago> agregarPago(@RequestBody Pago pago) {
        try {
            Pago guardado = pagoService.guardarPago(pago);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        
    }
}