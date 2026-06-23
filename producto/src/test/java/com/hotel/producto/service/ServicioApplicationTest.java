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

import com.hotel.producto.DTO.ServicioDTO;
import com.hotel.producto.Repository.ServicioRepository;
import com.hotel.producto.model.Servicio;
import com.hotel.producto.Services.ServicioService;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class ServicioApplicationTest {

    @Mock
    private ServicioRepository servicioRepository; // Simulamos la tabla de servicios adicionales

    @InjectMocks
    private ServicioService servicioService; // Inyectamos el mock en el servicio real

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorId_Exitoso() {
        // GIVEN: Un servicio simulado (Ej. Servicio de masajes o buffet)
        Integer idSimulado = 1;
        String nombreServicioAleatorio = faker.food().dish() + " Premium"; // Ej: "Sushi Premium"
        Double precioAleatorio = faker.number().randomDouble(2, 5000, 25000);

        Servicio servicioFalso = new Servicio();
        servicioFalso.setIdServicio(idSimulado);
        servicioFalso.setNombre(nombreServicioAleatorio);
        servicioFalso.setPrecio(precioAleatorio);

        when(servicioRepository.findById(idSimulado)).thenReturn(Optional.of(servicioFalso));

        // WHEN: Ejecutamos la consulta en el servicio
        ServicioDTO resultado = servicioService.buscarPorId(idSimulado);

        // THEN: Validamos la respuesta mapeada a DTO
        assertNotNull(resultado, "El DTO del servicio no debe ser nulo");
        assertEquals(nombreServicioAleatorio, resultado.getNombre(), "El nombre mapeado debe coincidir");
        assertEquals(precioAleatorio, resultado.getPrecio(), "El precio mapeado debe coincidir");
        
        verify(servicioRepository, times(1)).findById(idSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Integer idInexistente = 999;
        when(servicioRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Valida que el servicio lance una excepción si no encuentra el ID de servicio adicional
        assertThrows(RuntimeException.class, () -> {
            servicioService.buscarPorId(idInexistente);
        });

        verify(servicioRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testObtenerTodo_Exitoso() {
        // GIVEN: Una lista con 2 servicios cargados en la base de datos simulada
        List<Servicio> serviciosFalsos = new ArrayList<>();
        
        Servicio s1 = new Servicio(1, "Desayuno Americano", 8500.0);
        Servicio s2 = new Servicio(2, "Acceso a Piscina Temperada", 15000.0);
        
        serviciosFalsos.add(s1);
        serviciosFalsos.add(s2);

        when(servicioRepository.findAll()).thenReturn(serviciosFalsos);

        // WHEN: Ejecutamos el método para listar todo
        List<ServicioDTO> resultado = servicioService.obtenerTodo();

        // THEN: Comprobamos el tamaño y elementos de la lista transformada
        assertNotNull(resultado);
        assertEquals(2, resultado.size(), "Debe listar exactamente 2 servicios");
        assertEquals("Desayuno Americano", resultado.get(0).getNombre());
        
        verify(servicioRepository, times(1)).findAll();
    }

    @Test
    void testGuardarServicio_Exitoso() {
        // GIVEN: El objeto servicio que se enviará desde el controlador
        Servicio nuevoServicio = new Servicio();
        nuevoServicio.setNombre("Estacionamiento Privado");
        nuevoServicio.setPrecio(12000.0);

        // El objeto que simula retornar la base de datos con una ID autogenerada (ID 4)
        Servicio servicioPersistido = new Servicio();
        servicioPersistido.setIdServicio(4);
        servicioPersistido.setNombre("Estacionamiento Privado");
        servicioPersistido.setPrecio(12000.0);

        when(servicioRepository.save(any(Servicio.class))).thenReturn(servicioPersistido);

        // WHEN: Guardamos a través de la capa Service
        Servicio resultado = servicioService.guardarServicio(nuevoServicio);

        // THEN: Evaluamos que se asigne correctamente la ID
        assertNotNull(resultado);
        assertEquals(4, resultado.getIdServicio());
        assertEquals("Estacionamiento Privado", resultado.getNombre());
        
        verify(servicioRepository, times(1)).save(any(Servicio.class));
    }
}