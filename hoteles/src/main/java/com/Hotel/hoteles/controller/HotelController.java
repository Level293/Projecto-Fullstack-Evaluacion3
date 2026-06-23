package com.Hotel.hoteles.controller;

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


import com.Hotel.hoteles.DTO.HotelDTO;
import com.Hotel.hoteles.Service.HotelService;
import com.Hotel.hoteles.model.Hotel;



@RestController
@RequestMapping("/api/v1/hoteles")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<HotelDTO>> todosLosHoteles() {
        List<HotelDTO> hoteles = hotelService.obtenerTodo();
        if (hoteles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(hoteles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> buscarPorId(@PathVariable Integer id) {
        try {
            HotelDTO hotel = hotelService.buscarPorId(id);
            return new ResponseEntity<>(hotel, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<HotelDTO> agregarHotel(@RequestBody Hotel hotel) {
        try {
            HotelDTO guardado = hotelService.guardarHotel(hotel);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}