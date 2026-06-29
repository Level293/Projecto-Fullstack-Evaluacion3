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

import com.hotel.producto.DTO.ReservaHabitacionDTO;
import com.hotel.producto.Services.ReservaHabitacionService;
import com.hotel.producto.model.ReservaHabitacion;

@RestController
@RequestMapping("/api/v1/reserva-habitacion")
public class ReservaHabitacionController {

    @Autowired
    public ReservaHabitacionService reservaHabService;

    @GetMapping
    public ResponseEntity<List<ReservaHabitacionDTO>> todosLasReservasHab(){
        List<ReservaHabitacionDTO> reservaHab = reservaHabService.obtenerTodos();
        if (reservaHab.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reservaHab, HttpStatus.OK);
    
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaHabitacionDTO> buscarPorId(@PathVariable Integer id){
        try {
            ReservaHabitacionDTO reservaHab = reservaHabService.buscarPorId(id);
            return new ResponseEntity<>(reservaHab, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/precio/{precioNoche}")
     public ResponseEntity<List<ReservaHabitacionDTO>> buscarPorPrecioNoche(@PathVariable Integer precioNoche) {
        List<ReservaHabitacionDTO> reservas = reservaHabService.buscarPorPrecioNoche(precioNoche);
        if (reservas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reservas, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<ReservaHabitacion> agregarReservaHab(@RequestBody ReservaHabitacion reservaHab) {
        try{
            ReservaHabitacion guardado = reservaHabService.guardarReservaHab(reservaHab);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarReservaHab(@PathVariable Integer id) {
        String resultado = reservaHabService.eliminar(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}