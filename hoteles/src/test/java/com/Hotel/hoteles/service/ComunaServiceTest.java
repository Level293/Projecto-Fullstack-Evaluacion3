package com.Hotel.hoteles.service; 

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Hotel.hoteles.DTO.ComunaDTO;
import com.Hotel.hoteles.model.Comuna;
import com.Hotel.hoteles.model.Region;
import com.Hotel.hoteles.Repository.ComunaRepository;
import com.Hotel.hoteles.Service.ComunaService;

@ExtendWith(MockitoExtension.class)
class ComunaServiceTest {

    @Mock
    private ComunaRepository comunaRepository; 

    @InjectMocks
    private ComunaService comunaService; 

    @BeforeEach
    void setUp() {
        
        MockitoAnnotations.openMocks(this);
    }

    private Comuna createComuna() {
        Region region = new Region(1, "Metropolitana");
        return new Comuna(1, region, "Santiago Centro");
    }

    @Test
    void testObtenerTodo() {
        when(comunaRepository.findAll()).thenReturn(List.of(createComuna()));

        List<ComunaDTO> comunas = comunaService.obtenerTodo();

        assertNotNull(comunas);
        assertEquals(1, comunas.size());
        assertEquals("Santiago Centro", comunas.get(0).getNombre());
        assertEquals("Metropolitana", comunas.get(0).getNombreRegion());

        verify(comunaRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        when(comunaRepository.findById(1)).thenReturn(Optional.of(createComuna()));

        ComunaDTO comuna = comunaService.buscarPorId(1);

        assertNotNull(comuna);
        assertEquals("Santiago Centro", comuna.getNombre());
        
        verify(comunaRepository, times(1)).findById(1);
    }

    @Test
    void testGuardarComuna() {
        Comuna comuna = createComuna();
        when(comunaRepository.save(comuna)).thenReturn(comuna);

        Comuna savedComuna = comunaService.guardarComuna(comuna);

        assertNotNull(savedComuna);
        assertEquals("Santiago Centro", savedComuna.getNombre());
        
        verify(comunaRepository, times(1)).save(comuna);
    }
}