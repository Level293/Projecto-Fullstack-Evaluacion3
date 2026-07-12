package com.Hotel.usuarios.Service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Hotel.usuarios.DTO.PagoDTO;
import com.Hotel.usuarios.model.Pago;
import com.Hotel.usuarios.repository.PagoRepository;
import com.Hotel.usuarios.service.PagoService;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
public class PagoAplicationTests {

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoService pagoService;
    private Faker faker = new Faker();
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    

    @Test
    void testBuscarPorId_Exitoso(){
        Integer idSimulado = 42;
        
        java.util.Date fakerDate = faker.date().past(30, java.util.concurrent.TimeUnit.DAYS);
        LocalDate fechaAleatoria = fakerDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Pago pagoFalso = new Pago();
        pagoFalso.setIdPago(idSimulado);
        pagoFalso.setMonto(faker.number().numberBetween(100000, 250000));
        pagoFalso.setFechaPago(fechaAleatoria);
        pagoFalso.setMetodo("Tarjeta");
        pagoFalso.setEstado(true);

        when(pagoRepository.findById(idSimulado)).thenReturn(Optional.of(pagoFalso));
        
        PagoDTO resultado = pagoService.buscarPorId(idSimulado);

        assertNotNull(resultado, "El DTO resultante no deberia ser nulo");
        assertEquals(idSimulado, resultado.getIdPago());

        verify(pagoRepository, times(1)).findById(idSimulado);
    }

}
