package com.Hotel.usuarios.Service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Hotel.usuarios.DTO.ReservaDTO;
import com.Hotel.usuarios.model.Reservas;
import com.Hotel.usuarios.repository.ReservasRepository;
import com.Hotel.usuarios.service.ReservasService;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
public class ReservasAplicationTests {

    @Mock
    private ReservasRepository reservasRepository;

    @InjectMocks
    private ReservasService reservasService;
    private Faker faker = new Faker();
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarReserva_Exitoso(){
        Integer idSimulado = 12;
        Integer totalAleatorio = faker.number().numberBetween(50000, 200000);
        Reservas reservasFalsa = new Reservas();
        reservasFalsa.setIdReserva(idSimulado);
        reservasFalsa.setFechaInicio(LocalDate.now());
        reservasFalsa.setFechaFin(LocalDate.now().plusDays(7));
        reservasFalsa.setEstado("Confirmada");
        reservasFalsa.setTotal(totalAleatorio);

        when(reservasRepository.findById(idSimulado)).thenReturn(Optional.of(reservasFalsa));

        ReservaDTO resultado = reservasService.buscarPorId(idSimulado);

        assertNotNull(resultado, "El DTO resultante no deberia ser nulo");
        assertEquals(idSimulado, resultado.getIdReserva());
        assertEquals(totalAleatorio, resultado.getTotal());

        verify(reservasRepository, times(1)).findById(idSimulado);
    }
}
