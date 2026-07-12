package com.Hotel.hoteles.service; 

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Hotel.hoteles.DTO.HotelDTO;
import com.Hotel.hoteles.model.Hotel;
import com.Hotel.hoteles.Repository.HotelRepository;
import com.Hotel.hoteles.Service.HotelService;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository; 

    @InjectMocks
    private HotelService hotelService; 

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Hotel createHotel() {
        return new Hotel(1, "Hotel Costanera", "Av. Vitacura 123");
    }

    @Test
    void testObtenerTodo() {

        when(hotelRepository.findAll()).thenReturn(List.of(createHotel()));

        List<HotelDTO> hoteles = hotelService.obtenerTodo();

        assertNotNull(hoteles);
        assertEquals(1, hoteles.size());
        assertEquals("Hotel Costanera", hoteles.get(0).getNombre());

        verify(hotelRepository, times(1)).findAll();

    }
}