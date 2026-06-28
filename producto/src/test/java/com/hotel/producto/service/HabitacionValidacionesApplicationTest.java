package com.hotel.producto.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hotel.producto.DTO.HabitacionDTO;
import com.hotel.producto.Repository.HabitacionRepository;
import com.hotel.producto.Repository.TipoHabitacionRepository;
import com.hotel.producto.Services.HabitacionValidaciones;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class HabitacionValidacionesApplicationTest {

    @Mock
    private TipoHabitacionRepository tipoHabitacionRepository;

    @Mock
    private HabitacionRepository habitacionRepository;

    @InjectMocks
    private HabitacionValidaciones habitacionValidaciones;

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidarCamposHabitacion_Exitoso() {
        HabitacionDTO dtoValido = new HabitacionDTO();
        dtoValido.setNumero(faker.number().numberBetween(100, 999));
        dtoValido.setEstado("Disponible");

        boolean resultado = habitacionValidaciones.validarCamposHabitacion(dtoValido);

        assertTrue(resultado);
    }

    @Test
    void testValidarCamposHabitacion_NumeroInvalido() {
        HabitacionDTO dtoInvalido = new HabitacionDTO();
        dtoInvalido.setNumero(0);
        dtoInvalido.setEstado("Disponible");

        boolean resultado = habitacionValidaciones.validarCamposHabitacion(dtoInvalido);

        assertFalse(resultado);
    }

    @Test
    void testValidarCamposHabitacion_EstadoVacio() {
        HabitacionDTO dtoInvalido = new HabitacionDTO();
        dtoInvalido.setNumero(faker.number().numberBetween(100, 999));
        dtoInvalido.setEstado("   "); 

        boolean resultado = habitacionValidaciones.validarCamposHabitacion(dtoInvalido);

        assertFalse(resultado);
    }


    @Test
    void testExisteTipoHabitacion_Exitoso() {
        Integer idTipoSimulado = faker.number().positive();
        
        // Simula que la base de datos SÍ encuentra el Tipo de Habitación
        when(tipoHabitacionRepository.existsById(idTipoSimulado)).thenReturn(true);

        boolean resultado = habitacionValidaciones.existeTipoHabitacion(idTipoSimulado);

        assertTrue(resultado);
        verify(tipoHabitacionRepository, times(1)).existsById(idTipoSimulado);
    }

    @Test
    void testExisteTipoHabitacion_NoEncontrado() {
        Integer idInexistente = faker.number().positive();
        
        // Simula que la base de datos NO encuentra el Tipo de Habitación
        when(tipoHabitacionRepository.existsById(idInexistente)).thenReturn(false);

        boolean resultado = habitacionValidaciones.existeTipoHabitacion(idInexistente);

        assertFalse(resultado);
        verify(tipoHabitacionRepository, times(1)).existsById(idInexistente);
    }


    @Test
    void testElNumeroYaExiste_Verdadero() {
        Integer numeroSimulado = faker.number().numberBetween(100, 999);
        
        // Simula que el número ya está registrado en la base de datos
        when(habitacionRepository.existsByNumero(numeroSimulado)).thenReturn(true);

        boolean resultado = habitacionValidaciones.elNumeroYaExiste(numeroSimulado);

        assertTrue(resultado);
        verify(habitacionRepository, times(1)).existsByNumero(numeroSimulado);
    }

    @Test
    void testElNumeroYaExiste_Falso() {
        Integer numeroLibre = faker.number().numberBetween(100, 999);
        
        // Simula que el número está libre (no existe en la base de datos)
        when(habitacionRepository.existsByNumero(numeroLibre)).thenReturn(false);

        boolean resultado = habitacionValidaciones.elNumeroYaExiste(numeroLibre);

        assertFalse(resultado);
        verify(habitacionRepository, times(1)).existsByNumero(numeroLibre);
    }

    @Test
    void testElPrecioEsValido_Exitoso() {
        Integer precioValido = faker.number().numberBetween(50, 500);

        boolean resultado = habitacionValidaciones.elPrecioEsValido(precioValido);

        assertTrue(resultado);
    }

    @Test
    void testElPrecioEsValido_NegativoOCero() {
        Integer precioInvalido = 0; // Precios menores o iguales a cero son inválidos

        boolean resultado = habitacionValidaciones.elPrecioEsValido(precioInvalido);

        assertFalse(resultado);
    }
}