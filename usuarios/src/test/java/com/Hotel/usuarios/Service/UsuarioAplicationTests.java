package com.Hotel.usuarios.Service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Hotel.usuarios.DTO.UsuarioDTO;
import com.Hotel.usuarios.model.Roles;
import com.Hotel.usuarios.model.Usuario;
import com.Hotel.usuarios.repository.UsuarioRepository;
import com.Hotel.usuarios.service.UsuarioService;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
public class UsuarioAplicationTests {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;
    private Faker faker = new Faker();
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorId_Exitoso() {
        Integer idSimulado = 10;
        String nombreAleatorio = faker.residentEvil().character();
        String correoAleatorio = faker.internet().emailAddress();

        Roles rolFalso = new Roles();
        rolFalso.setIdroles(1);
        rolFalso.setNombre("Administracion");

        Usuario usuarioFalso = new Usuario();
        usuarioFalso.setIdUsuario(idSimulado);
        usuarioFalso.setNombre(nombreAleatorio);
        usuarioFalso.setCorreo(correoAleatorio);
        usuarioFalso.setRoles(null);

        when(usuarioRepository.findById(idSimulado)).thenReturn(Optional.of(usuarioFalso));

        UsuarioDTO resultado = usuarioService.buscarPorId(idSimulado);

        assertNotNull(resultado, "El usuario resultante no debería ser nulo");
        assertEquals(nombreAleatorio, resultado.getNombre());
        assertEquals(correoAleatorio, resultado.getCorreo());
        assertEquals("Administracion", resultado.getNombreRol());
        
        verify(usuarioRepository, times(1)).findById(idSimulado);
    }
}
