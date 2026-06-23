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

import com.hotel.producto.DTO.HabitacionDTO;
import com.hotel.producto.Services.HabitacionService;

@RestController
@RequestMapping("/api/v1/habitacion")
public class HabitacionController {

    @Autowired
    public HabitacionService habitacionService;

    @GetMapping
    public ResponseEntity<List<HabitacionDTO>> todosLasHabitaciones(){
        List<HabitacionDTO> habitacion = habitacionService.obtenerTodo();
        if (habitacion.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(habitacion, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitacionDTO> buscarPorId(@PathVariable Integer id){
        try {
            HabitacionDTO habitacion = habitacionService.buscarPorId(id);
            return new ResponseEntity<>(habitacion, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<HabitacionDTO>> buscarPorEstado(@PathVariable String estado) {
        List<HabitacionDTO> habitaciones = habitacionService.buscarPorEstado(estado);
        
        if (habitaciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(habitaciones, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HabitacionDTO> agregarHabitacion(@RequestBody HabitacionDTO habitacionDTO) {
    try {
        HabitacionDTO guardada = habitacionService.guardarHabitacion(habitacionDTO);
        return new ResponseEntity<>(guardada, HttpStatus.CREATED);
    } catch (Exception e) {
        e.printStackTrace(); 
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarHabitacion(@PathVariable Integer id) {
        String resultado = habitacionService.eliminar(id);

        if (resultado.contains(("exitosamente"))) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

}
