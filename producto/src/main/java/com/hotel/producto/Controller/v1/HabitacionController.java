package com.hotel.producto.Controller.v1;

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
        public ResponseEntity<List<HabitacionDTO>> todosLasHabitaciones() {
            List<HabitacionDTO> habitaciones = habitacionService.obtenerTodo();
            if (habitaciones.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(habitaciones);
        }

    @GetMapping("/{id}")
        public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
            try {
                HabitacionDTO habitacion = habitacionService.buscarPorId(id);
                return ResponseEntity.ok(habitacion);
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); 
            }
        }

    @GetMapping("/estado/{estado}")
        public ResponseEntity<List<HabitacionDTO>> buscarPorEstado(@PathVariable String estado) {
            List<HabitacionDTO> habitaciones = habitacionService.buscarPorEstado(estado);
            if (habitaciones.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(habitaciones);
        }

    @PostMapping
        public ResponseEntity<?> agregarHabitacion(@RequestBody HabitacionDTO habitacionDTO) {
            try {
                HabitacionDTO guardada = habitacionService.guardarHabitacion(habitacionDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(guardada); 
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno en el servidor al procesar la habitación");
            }
        }

    @DeleteMapping("/{id}")
        public ResponseEntity<String> eliminarHabitacion(@PathVariable Integer id) {
            String resultado = habitacionService.eliminar(id);

            if (resultado.contains("exitosamente")) {
                return ResponseEntity.ok(resultado);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
            }
        }

}
