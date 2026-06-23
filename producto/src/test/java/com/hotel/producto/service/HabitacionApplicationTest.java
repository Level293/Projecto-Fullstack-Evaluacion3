package com.hotel.producto.service;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hotel.producto.DTO.HabitacionDTO;
import com.hotel.producto.Repository.HabitacionRepository;
import com.hotel.producto.model.Habitacion;
import com.hotel.producto.Services.HabitacionService;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class HabitacionApplicationTest {

    @Mock
    private HabitacionRepository habitacionRepository;

    @InjectMocks
    private HabitacionService habitacionService;

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testBuscarPorId_Exitoso() {
        Integer idSimulado = 1;
        Habitacion habitacionFalsa = new Habitacion();
        habitacionFalsa.setIdHabitacion(idSimulado);
        habitacionFalsa.setNumero(faker.number().numberBetween(100, 500));
        habitacionFalsa.setEstado("Disponible");

        when(habitacionRepository.findById(idSimulado)).thenReturn(Optional.of(habitacionFalsa));

        HabitacionDTO resultado = habitacionService.buscarPorId(idSimulado);

        assertNotNull(resultado);
        assertEquals(habitacionFalsa.getNumero(), resultado.getNumero());
        verify(habitacionRepository, times(1)).findById(idSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        
        Integer idInexistente = 999;
        when(habitacionRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            habitacionService.buscarPorId(idInexistente);
        });

        verify(habitacionRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testObtenerTodasLasHabitaciones_Exitoso() {
        // GIVEN: Creamos una lista simulada con 2 habitaciones de la DB
        List<Habitacion> listaFalsa = new ArrayList<>();
        
        Habitacion habitacion1 = new Habitacion();
        habitacion1.setIdHabitacion(1);
        habitacion1.setNumero(faker.number().numberBetween(100, 500));
        habitacion1.setEstado("Disponible");
        listaFalsa.add(habitacion1);

    }
}