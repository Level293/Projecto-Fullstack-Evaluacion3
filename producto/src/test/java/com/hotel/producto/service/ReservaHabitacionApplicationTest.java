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
import com.hotel.producto.DTO.HabitacionDTO;
import com.hotel.producto.Repository.ReservaHabitacionRepository;
import com.hotel.producto.Repository.HabitacionRepository; // AGREGADO: Necesario para el flujo de guardado
import com.hotel.producto.model.ReservaHabitacion;
import com.hotel.producto.model.Habitacion;
import com.hotel.producto.Services.ReservaHabitacionService;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class ReservaHabitacionApplicationTest {

    @Mock
    private ReservaHabitacionRepository reservaHabitacionRepository; 

    @Mock
    private HabitacionRepository habitacionRepository; // AGREGADO: Mock requerido por la validación interna del Service

    @InjectMocks
    private ReservaHabitacionService reservaHabitacionService; 

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorId_Exitoso() {
        Integer idReservaHabSimulado = 1;
        Integer precioAleatorio = faker.number().numberBetween(45000, 120000);
        Integer idReservaPadre = faker.number().numberBetween(100, 500);

        Habitacion habitacionFalsa = new Habitacion();
        habitacionFalsa.setIdHabitacion(101);
        habitacionFalsa.setNumero(202);
        habitacionFalsa.setEstado("Ocupada");

        ReservaHabitacion reservaHabFalsa = new ReservaHabitacion();
        // NOTA: Asegúrate de si tu modelo usa 'setIdReservaHab' o 'setIdReservaHabitacion' según el Service
        reservaHabFalsa.setIdReservaHab(idReservaHabSimulado); 
        reservaHabFalsa.setPrecioNoche(precioAleatorio);
        reservaHabFalsa.setIdReserva(idReservaPadre);
        reservaHabFalsa.setHabitacion(habitacionFalsa);

        when(reservaHabitacionRepository.findById(idReservaHabSimulado)).thenReturn(Optional.of(reservaHabFalsa));

        ReservaHabitacionDTO resultado = reservaHabitacionService.buscarPorId(idReservaHabSimulado);

        assertNotNull(resultado, "El DTO de la reserva de habitación no debería ser nulo");
        assertEquals(precioAleatorio, resultado.getPrecioNoche(), "El precio de la noche debe coincidir con el simulado");
        assertEquals(idReservaPadre, resultado.getIdReserva(), "El ID de la reserva padre debe coincidir");
        
        verify(reservaHabitacionRepository, times(1)).findById(idReservaHabSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Integer idInexistente = 99;
        when(reservaHabitacionRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            reservaHabitacionService.buscarPorId(idInexistente);
        });

        verify(reservaHabitacionRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testGuardarReservaHab_Exitoso() {
        // GIVEN: Preparamos los DTOs de entrada simulando los datos de Postman
        HabitacionDTO habitacionDTO = new HabitacionDTO();
        habitacionDTO.setIdHabitacion(101);

        ReservaHabitacionDTO dtoAGuardar = new ReservaHabitacionDTO();
        dtoAGuardar.setPrecioNoche(65000);
        dtoAGuardar.setIdReserva(12);
        dtoAGuardar.setHabitacion(habitacionDTO);

        // Simulamos las respuestas de las entidades JPA de los Repositorios
        Habitacion habitacionExistente = new Habitacion();
        habitacionExistente.setIdHabitacion(101);

        ReservaHabitacion reservaPersistida = new ReservaHabitacion();
        reservaPersistida.setIdReservaHab(5); // ID asignada por la Base de Datos simulada
        reservaPersistida.setPrecioNoche(65000);
        reservaPersistida.setIdReserva(12);
        reservaPersistida.setHabitacion(habitacionExistente);

        // Entrenamos los mocks para responder correctamente a las llamadas internas del Service
        when(habitacionRepository.findById(101)).thenReturn(Optional.of(habitacionExistente));
        when(reservaHabitacionRepository.save(any(ReservaHabitacion.class))).thenReturn(reservaPersistida);

        // WHEN: Ejecutamos el método con su nuevo nombre y firma DTO
        ReservaHabitacionDTO resultado = reservaHabitacionService.guardarReservaHabitacion(dtoAGuardar);

        // THEN: Validamos el DTO de salida
        assertNotNull(resultado);
        assertEquals(5, resultado.getIdReservaHab());
        assertEquals(65000, resultado.getPrecioNoche());
        
        verify(habitacionRepository, times(1)).findById(101);
        verify(reservaHabitacionRepository, times(1)).save(any(ReservaHabitacion.class));
    }
}