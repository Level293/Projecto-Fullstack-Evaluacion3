package com.hotel.producto.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
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

import com.hotel.producto.DTO.TipoHabitacionDTO;
import com.hotel.producto.Repository.TipoHabitacionRepository;
import com.hotel.producto.model.TipoHabitacion;
import com.hotel.producto.Services.TipoHabitacionService;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class TipoHabitacionApplicationTest {

    @Mock
    private TipoHabitacionRepository tipoHabitacionRepository; // Simulamos la tabla tipo_habitacion

    @InjectMocks
    private TipoHabitacionService tipoHabitacionService; // Inyectamos el mock en el servicio real

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorId_Exitoso() {
        // GIVEN: Un tipo de habitación aleatorio (Ej: Suite Ejecutiva)
        Integer idSimulado = 1;
        String nombreTipoAleatorio = faker.options().option("Simple", "Doble", "Suite Familiar", "Presidencial");
        Integer capacidadAleatoria = faker.number().numberBetween(1, 6);

        TipoHabitacion tipoFalso = new TipoHabitacion();
        tipoFalso.setIdTipo(idSimulado);
        tipoFalso.setNombre(nombreTipoAleatorio);
        tipoFalso.setCapacidad(capacidadAleatoria);

        when(tipoHabitacionRepository.findById(idSimulado)).thenReturn(Optional.of(tipoFalso));

        // WHEN: Ejecutamos la acción en el servicio
        TipoHabitacionDTO resultado = tipoHabitacionService.buscarPorId(idSimulado);

        // THEN: Validamos que los datos se hayan transformado correctamente al DTO
        assertNotNull(resultado, "El DTO de tipo de habitación no debe ser nulo");
        assertEquals(nombreTipoAleatorio, resultado.getNombre(), "El nombre debe coincidir");
        assertEquals(capacidadAleatoria, resultado.getCapacidad(), "La capacidad debe coincidir");
        
        verify(tipoHabitacionRepository, times(1)).findById(idSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Integer idInexistente = 404;
        when(tipoHabitacionRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Valida que el servicio lance una excepción si no encuentra la categoría
        assertThrows(RuntimeException.class, () -> {
            tipoHabitacionService.buscarPorId(idInexistente);
        });

        verify(tipoHabitacionRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testObtenerTodo_Exitoso() {
        // GIVEN: Creamos una lista simulada con dos categorías existentes
        List<TipoHabitacion> listaFalsa = new ArrayList<>();
        
        TipoHabitacion t1 = new TipoHabitacion();
        t1.setIdTipo(1);
        t1.setNombre("Estándar");
        t1.setCapacidad(2);

        TipoHabitacion t2 = new TipoHabitacion();
        t2.setIdTipo(2);
        t2.setNombre("Penthouse");
        t2.setCapacidad(4);

        listaFalsa.add(t1);
        listaFalsa.add(t2);

        when(tipoHabitacionRepository.findAll()).thenReturn(listaFalsa);

        // WHEN: Consultamos la lista completa en el servicio
        List<TipoHabitacionDTO> resultado = tipoHabitacionService.obtenerTodo();

        // THEN: Comprobamos que devuelva la misma cantidad y mapeos adecuados
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Estándar", resultado.get(0).getNombre());
        
        verify(tipoHabitacionRepository, times(1)).findAll();
    }

    @Test
    void testGuardarTipoHabitacion_Exitoso() {
        // GIVEN: El objeto que se pretende guardar
        TipoHabitacion nuevoTipo = new TipoHabitacion();
        nuevoTipo.setNombre("Deluxe Twin");
        nuevoTipo.setCapacidad(3);

        // El objeto simulado que guardará la base de datos con su ID asignado (ID 7)
        TipoHabitacion tipoPersistido = new TipoHabitacion();
        tipoPersistido.setIdTipo(7);
        tipoPersistido.setNombre("Deluxe Twin");
        tipoPersistido.setCapacidad(3);

        when(tipoHabitacionRepository.save(any(TipoHabitacion.class))).thenReturn(tipoPersistido);

        // WHEN: Ejecutamos el guardado desde el Service
        TipoHabitacionDTO resultado = tipoHabitacionService.guardarTipo(nuevoTipo);

        // THEN: Verificamos que se devuelva el objeto con la ID correcta
        assertNotNull(resultado);
        assertEquals(7, resultado.getIdTipo());
        assertEquals("Deluxe Twin", resultado.getNombre());
        
        verify(tipoHabitacionRepository, times(1)).save(any(TipoHabitacion.class));
    }
}