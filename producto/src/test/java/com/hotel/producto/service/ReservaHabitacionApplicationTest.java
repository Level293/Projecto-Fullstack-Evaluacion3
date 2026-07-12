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
    private ReservaHabitacionRepository reservaHabitacionRepository;

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
        ReservaHabitacion reservaAGuardar = new ReservaHabitacion();
        reservaAGuardar.setPrecioNoche(65000);
        reservaAGuardar.setIdReserva(12);

        ReservaHabitacion reservaPersistida = new ReservaHabitacion();
        reservaPersistida.setIdReservaHab(5);
        reservaPersistida.setPrecioNoche(65000);
        reservaPersistida.setIdReserva(12);

        when(reservaHabitacionRepository.save(any(ReservaHabitacion.class))).thenReturn(reservaPersistida);

        ReservaHabitacion resultado = reservaHabitacionService.guardarReservaHab(reservaAGuardar);

        assertNotNull(resultado);
        assertEquals(5, resultado.getIdReservaHab());
        verify(reservaHabitacionRepository, times(1)).save(any(ReservaHabitacion.class));
    }
}