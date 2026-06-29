package com.Hotel.hoteles.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.Hotel.hoteles.DTO.RegionDTO;
import com.Hotel.hoteles.model.Region;
import com.Hotel.hoteles.Repository.RegionRepository;
import com.Hotel.hoteles.Service.RegionService;

@ExtendWith(MockitoExtension.class)
class RegionServiceTest {

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private RegionService regionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Region createRegion() {
        return new Region(1, "Metropolitana");
    }

    @Test
    void testObtenerTodo() {
        when(regionRepository.findAll()).thenReturn(List.of(createRegion()));

        List<RegionDTO> regiones = regionService.obtenerTodo();

        assertNotNull(regiones);
        assertEquals(1, regiones.size());
        assertEquals("Metropolitana", regiones.get(0).getNombre());

        verify(regionRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        when(regionRepository.findById(1)).thenReturn(Optional.of(createRegion()));

        RegionDTO region = regionService.buscarPorId(1);

        assertNotNull(region);
        assertEquals("Metropolitana", region.getNombre());
        
        verify(regionRepository, times(1)).findById(1);
    }
}