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
    private ServicioRepository servicioRepository;

    @InjectMocks
    private ServicioService servicioService;

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorId_Exitoso() {
        Integer idSimulado = 1;
        String nombreServicioAleatorio = faker.food().dish() + " Premium";
        Double precioAleatorio = faker.number().randomDouble(2, 5000, 25000);

        Servicio servicioFalso = new Servicio();
        servicioFalso.setIdServicio(idSimulado);
        servicioFalso.setNombre(nombreServicioAleatorio);
        servicioFalso.setPrecio(precioAleatorio);

        when(servicioRepository.findById(idSimulado)).thenReturn(Optional.of(servicioFalso));

        ServicioDTO resultado = servicioService.buscarPorId(idSimulado);

        assertNotNull(resultado, "El DTO del servicio no debe ser nulo");
        assertEquals(nombreServicioAleatorio, resultado.getNombre(), "El nombre mapeado debe coincidir");
        assertEquals(precioAleatorio, resultado.getPrecio(), "El precio mapeado debe coincidir");
        
        verify(servicioRepository, times(1)).findById(idSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Integer idInexistente = 999;
        when(servicioRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            servicioService.buscarPorId(idInexistente);
        });

        verify(servicioRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testObtenerTodo_Exitoso() {
        List<Servicio> serviciosFalsos = new ArrayList<>();
        
        Servicio s1 = new Servicio(1, "Desayuno Americano", 8500.0);
        Servicio s2 = new Servicio(2, "Acceso a Piscina Temperada", 15000.0);
        
        serviciosFalsos.add(s1);
        serviciosFalsos.add(s2);

        when(servicioRepository.findAll()).thenReturn(serviciosFalsos);

        List<ServicioDTO> resultado = servicioService.obtenerTodo();

        assertNotNull(resultado);
        assertEquals(2, resultado.size(), "Debe listar exactamente 2 servicios");
        assertEquals("Desayuno Americano", resultado.get(0).getNombre());
        
        verify(servicioRepository, times(1)).findAll();
    }

    @Test
    void testGuardarServicio_Exitoso() {
        Servicio nuevoServicio = new Servicio();
        nuevoServicio.setNombre("Estacionamiento Privado");
        nuevoServicio.setPrecio(12000.0);

        Servicio servicioPersistido = new Servicio();
        servicioPersistido.setIdServicio(4);
        servicioPersistido.setNombre("Estacionamiento Privado");
        servicioPersistido.setPrecio(12000.0);

        when(servicioRepository.save(any(Servicio.class))).thenReturn(servicioPersistido);

        Servicio resultado = servicioService.guardarServicio(nuevoServicio);

        assertNotNull(resultado);
        assertEquals(4, resultado.getIdServicio());
        assertEquals("Estacionamiento Privado", resultado.getNombre());
        
        verify(servicioRepository, times(1)).save(any(Servicio.class));
    }
}