package com.hotel.producto.Controller;

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

import com.hotel.producto.DTO.HabitacionServicioDTO;
import com.hotel.producto.Services.HabitacionServicioService;
import com.hotel.producto.model.HabitacionServicio;

@RestController
@RequestMapping("/api/v1/habitacion-servicio")
public class HabitacionServicioController {

    @Autowired
    public HabitacionServicioService habitacionServicioService;

    @GetMapping
    public ResponseEntity<List<HabitacionServicioDTO>> todosLosHabitacionServicios() {
        List<HabitacionServicioDTO> lista = habitacionServicioService.obtenerTodo();
        if (lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitacionServicioDTO> buscarPorId(@PathVariable Integer id) {
        try {
            HabitacionServicioDTO dto = habitacionServicioService.buscarPorId(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<HabitacionServicio> agregarHabitacionServicio(@RequestBody HabitacionServicio hs) {
        try {
            HabitacionServicio guardado = habitacionServicioService.guardarHabitacionServicio(hs);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarHabitacionServicio(@PathVariable Integer id) {
        String resultado = habitacionServicioService.eliminar(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}
