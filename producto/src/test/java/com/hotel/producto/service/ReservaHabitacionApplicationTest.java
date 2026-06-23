package com.hotel.producto.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hotel.producto.DTO.ReservaHabitacionDTO;
import com.hotel.producto.Repository.ReservaHabitacionRepository;
import com.hotel.producto.model.ReservaHabitacion;
import com.hotel.producto.model.Habitacion;
import com.hotel.producto.Services.ReservaHabitacionService;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class ReservaHabitacionApplicationTest {

    @Mock
    private ReservaHabitacionRepository reservaHabitacionRepository; // Simulamos acceso a la tabla reserva_habitacion

    @InjectMocks
    private ReservaHabitacionService reservaHabitacionService; // Inyectamos el Mock en el servicio real

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testBuscarPorId_Exitoso() {
        // GIVEN: Escenario con datos aleatorios simulados para una reserva de habitación
        Integer idReservaHabSimulado = 1;
        Integer precioAleatorio = faker.number().numberBetween(45000, 120000);
        Integer idReservaPadre = faker.number().numberBetween(100, 500);

        // Creamos la habitación relacionada requerida por el modelo
        Habitacion habitacionFalsa = new Habitacion();
        habitacionFalsa.setIdHabitacion(101);
        habitacionFalsa.setNumero(202);
        habitacionFalsa.setEstado("Ocupada");

        // Construimos la entidad ReservaHabitacion
        ReservaHabitacion reservaHabFalsa = new ReservaHabitacion();
        reservaHabFalsa.setIdReservaHab(idReservaHabSimulado);
        reservaHabFalsa.setPrecioNoche(precioAleatorio);
        reservaHabFalsa.setIdReserva(idReservaPadre);
        reservaHabFalsa.setHabitacion(habitacionFalsa);

        // Entrenamos al repositorio para retornar la reserva armada
        when(reservaHabitacionRepository.findById(idReservaHabSimulado)).thenReturn(Optional.of(reservaHabFalsa));

        // WHEN: Ejecutamos el método del servicio
        ReservaHabitacionDTO resultado = reservaHabitacionService.buscarPorId(idReservaHabSimulado);

        // THEN: Validamos la transformación exitosa a DTO
        assertNotNull(resultado, "El DTO de la reserva de habitación no debería ser nulo");
        assertEquals(precioAleatorio, resultado.getPrecioNoche(), "El precio de la noche debe coincidir con el simulado");
        assertEquals(idReservaPadre, resultado.getIdReserva(), "El ID de la reserva padre debe coincidir");
        
        verify(reservaHabitacionRepository, times(1)).findById(idReservaHabSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Integer idInexistente = 99;
        when(reservaHabitacionRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Valida que el servicio arroje un RuntimeException si la reserva no existe
        assertThrows(RuntimeException.class, () -> {
            reservaHabitacionService.buscarPorId(idInexistente);
        });

        verify(reservaHabitacionRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testGuardarReservaHab_Exitoso() {
        // GIVEN: Una entidad que mandamos a guardar
        ReservaHabitacion reservaAGuardar = new ReservaHabitacion();
        reservaAGuardar.setPrecioNoche(65000);
        reservaAGuardar.setIdReserva(12);

        // Entidad simulada que responde la base de datos con una ID autogenerada (ej. ID 5)
        ReservaHabitacion reservaPersistida = new ReservaHabitacion();
        reservaPersistida.setIdReservaHab(5);
        reservaPersistida.setPrecioNoche(65000);
        reservaPersistida.setIdReserva(12);

        when(reservaHabitacionRepository.save(any(ReservaHabitacion.class))).thenReturn(reservaPersistida);

        // WHEN: Ejecutamos el guardado en el servicio
        ReservaHabitacion resultado = reservaHabitacionService.guardarReservaHab(reservaAGuardar);

        // THEN: Validamos que se asigne la ID simulada de forma exitosa
        assertNotNull(resultado);
        assertEquals(5, resultado.getIdReservaHab());
        verify(reservaHabitacionRepository, times(1)).save(any(ReservaHabitacion.class));
    }
}