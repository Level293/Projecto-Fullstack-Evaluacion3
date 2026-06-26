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

import com.hotel.producto.DTO.ReservaHabitacionDTO;
import com.hotel.producto.Services.ReservaHabitacionService;

@RestController
// CORREGIDO: Ruta adaptada para que pase de forma matemática el filtro del Gateway
@RequestMapping("/api/v1/productos/reserva-habitacion")
public class ReservaHabitacionController {

    @Autowired
    public ReservaHabitacionService reservaHabService;

    // GET: http://localhost:8080/api/v1/productos/reserva-habitacion
    @GetMapping
    public ResponseEntity<List<ReservaHabitacionDTO>> todosLasReservasHab(){
        // CORREGIDO: Se cambia 'obtenerTodos()' por 'obtenerTodo()' que es el método real del Service
        List<ReservaHabitacionDTO> reservaHab = reservaHabService.obtenerTodo();
        if (reservaHab.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reservaHab, HttpStatus.OK);
    }

    // GET: http://localhost:8080/api/v1/productos/reserva-habitacion/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ReservaHabitacionDTO> buscarPorId(@PathVariable Integer id){
        try {
            ReservaHabitacionDTO reservaHab = reservaHabService.buscarPorId(id);
            return new ResponseEntity<>(reservaHab, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET: http://localhost:8080/api/v1/productos/reserva-habitacion/precio/{precioNoche}
    @GetMapping("/precio/{precioNoche}")
    public ResponseEntity<List<ReservaHabitacionDTO>> buscarPorPrecioNoche(@PathVariable Integer precioNoche) {
        // NOTA: Asegúrate de tener este método implementado en tu service. 
        // Si no lo usas, puedes borrar este bloque sin problemas.
        List<ReservaHabitacionDTO> reservas = reservaHabService.buscarPorPrecioNoche(precioNoche);
        if (reservas.isEmpty()) {
             return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }

    // POST: http://localhost:8080/api/v1/productos/reserva-habitacion
    // CORREGIDO: Manejo de tipos de datos mutado a DTO para acoplarse al Service distribuido
    @PostMapping
    public ResponseEntity<ReservaHabitacionDTO> agregarReservaHab(@RequestBody ReservaHabitacionDTO dto) {
        try {
            // CORREGIDO: Nombre de método alineado con 'guardarReservaHabitacion'
            ReservaHabitacionDTO guardado = reservaHabService.guardarReservaHabitacion(dto);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // DELETE: http://localhost:8080/api/v1/productos/reserva-habitacion/{id}
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