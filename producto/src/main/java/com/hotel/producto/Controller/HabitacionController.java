package com.hotel.producto.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; // Importación necesaria para la actualización
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; // Importación para recibir el estado por parámetro
import org.springframework.web.bind.annotation.RestController;

import com.hotel.producto.DTO.HabitacionDTO;
import com.hotel.producto.Services.HabitacionService;

@RestController
@RequestMapping("/api/v1/productos") 
public class HabitacionController {

    @Autowired
    public HabitacionService habitacionService;

    // GET: http://localhost:8080/api/v1/productos
    @GetMapping
    public ResponseEntity<List<HabitacionDTO>> todosLasHabitaciones(){
        List<HabitacionDTO> habitacion = habitacionService.obtenerTodo();
        if (habitacion.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(habitacion, HttpStatus.OK);
    }

    // GET: http://localhost:8080/api/v1/productos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<HabitacionDTO> buscarPorId(@PathVariable Integer id){
        try {
            HabitacionDTO habitacion = habitacionService.buscarPorId(id);
            return new ResponseEntity<>(habitacion, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET: http://localhost:8080/api/v1/productos/estado/{estado}
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<HabitacionDTO>> buscarPorEstado(@PathVariable String estado) {
        List<HabitacionDTO> habitaciones = habitacionService.buscarPorEstado(estado);
        
        if (habitaciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(habitaciones, HttpStatus.OK);
    }

    // POST: http://localhost:8080/api/v1/productos
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

    // PUT: http://localhost:8080/api/v1/productos/{id}/estado?nuevoEstado=OCUPADA
    // ¡IMPLEMENTADO!: Conecta con la lógica del Service para cambiar estados dinámicamente
    @PutMapping("/{id}/estado")
    public ResponseEntity<HabitacionDTO> actualizarEstado(
            @PathVariable Integer id, 
            @RequestParam String nuevoEstado) {
        try {
            HabitacionDTO actualizado = habitacionService.actualizarEstado(id, nuevoEstado);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE: http://localhost:8080/api/v1/productos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarHabitacion(@PathVariable Integer id) {
        String resultado = habitacionService.eliminar(id);

        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}