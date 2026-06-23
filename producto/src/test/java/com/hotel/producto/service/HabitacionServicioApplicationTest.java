package com.hotel.producto.service;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

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
class HabitacionServicioApplicationTest {

    @Mock
    private HabitacionRepository habitacionRepository; // Simula la conexión a la base de datos de habitaciones

    @InjectMocks
    private HabitacionService habitacionService; // Inyecta el repositorio simulado en tu servicio real

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarHabitacion_Exitoso() {
        // GIVEN: Un DTO con datos aleatorios generados por Datafaker
        HabitacionDTO dtoIngreso = new HabitacionDTO();
        dtoIngreso.setNumero(faker.number().numberBetween(100, 999));
        dtoIngreso.setEstado("Disponible");

        // El objeto que simulará responder la base de datos con su ID autonumérico asignado
        Habitacion habitacionGuardada = new Habitacion();
        habitacionGuardada.setIdHabitacion(faker.number().numberBetween(1, 100));
        habitacionGuardada.setNumero(dtoIngreso.getNumero());
        habitacionGuardada.setEstado(dtoIngreso.getEstado());

        when(habitacionRepository.save(any(Habitacion.class))).thenReturn(habitacionGuardada);

        // WHEN: Ejecutamos el método del servicio
        HabitacionDTO resultado = habitacionService.guardar(dtoIngreso);

        // THEN: Verificamos que los datos devueltos coincidan con lo guardado
        assertNotNull(resultado);
        assertEquals(dtoIngreso.getNumero(), resultado.getNumero());
        verify(habitacionRepository, times(1)).save(any(Habitacion.class));
    }

    @Test
    void testBuscarPorId_Exitoso() {
        // GIVEN: Configuramos una habitación existente en nuestra BD falsa
        Integer idSimulado = 10;
        Habitacion habitacionFalsa = new Habitacion();
        habitacionFalsa.setIdHabitacion(idSimulado);
        habitacionFalsa.setNumero(205);
        habitacionFalsa.setEstado("Mantenimiento");

        when(habitacionRepository.findById(idSimulado)).thenReturn(Optional.of(habitacionFalsa));

        // WHEN: Buscamos a través del servicio
        HabitacionDTO resultado = habitacionService.buscarPorId(idSimulado);

        // THEN: Comprobamos que el mapeo a DTO sea correcto
        assertNotNull(resultado);
        assertEquals(205, resultado.getNumero());
        assertEquals("Mantenimiento", resultado.getEstado());
        verify(habitacionRepository, times(1)).findById(idSimulado);
    }

    @Test
    void testActualizarEstado_Exitoso() {
        // GIVEN: Una habitación que inicialmente está "Disponible"
        Integer idHabitacion = 5;
        Habitacion habitacionExistente = new Habitacion();
        habitacionExistente.setIdHabitacion(idHabitacion);
        habitacionExistente.setNumero(102);
        habitacionExistente.setEstado("Disponible");

        // Respuestas del Mock
        when(habitacionRepository.findById(idHabitacion)).thenReturn(Optional.of(habitacionExistente));
        when(habitacionRepository.save(any(Habitacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN: El recepcionista del hotel cambia el estado a "Ocupada"
        HabitacionDTO resultado = habitacionService.actualizarEstado(idHabitacion, "Ocupada");

        // THEN: Comprobamos que el estado cambió exitosamente
        assertNotNull(resultado);
        assertEquals("Ocupada", resultado.getEstado());
        verify(habitacionRepository, times(1)).findById(idHabitacion);
        verify(habitacionRepository, times(1)).save(any(Habitacion.class));
    }

    @Test
    void testListarHabitacionesDisponibles() {
        // GIVEN: Una lista simulada con 2 habitaciones que están "Disponible"
        List<Habitacion> habitacionesDisponibles = new ArrayList<>();
        
        Habitacion h1 = new Habitacion();
        h1.setEstado("Disponible");
        h1.setNumero(faker.number().numberBetween(100, 200));
        
        Habitacion h2 = new Habitacion();
        h2.setEstado("Disponible");
        h2.setNumero(faker.number().numberBetween(201, 300));
        
        habitacionesDisponibles.add(h1);
        habitacionesDisponibles.add(h2);

        when(habitacionRepository.findAll()).thenReturn(habitacionesDisponibles);

        // WHEN: Ejecutamos el método de búsqueda del servicio
        List<HabitacionDTO> resultado = habitacionService.obtenerTodo(); // O habitacionService.listarDisponibles() si lo tienes específico

        // THEN: Validamos que devuelva la cantidad correcta de registros
        assertNotNull(resultado);
        assertTrue(resultado.size() >= 2 || resultado.size() == habitacionesDisponibles.size());
        verify(habitacionRepository, times(1)).findAll();
    }
}