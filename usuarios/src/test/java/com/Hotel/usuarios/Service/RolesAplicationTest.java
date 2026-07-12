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

import com.Hotel.usuarios.DTO.RolesDTO;
import com.Hotel.usuarios.model.Roles;
import com.Hotel.usuarios.model.Usuario;
import com.Hotel.usuarios.repository.RolesRepository;
import com.Hotel.usuarios.service.RolesService;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
public class RolesAplicationTest {

    @Mock
    private RolesRepository rolesRepository;

    @InjectMocks
    private RolesService rolesService;

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorId_Exitoso() {
        Integer idSimulado = 1;
        String rolAleatorio = faker.options().option("Administracion", "Limpieza", "Cliente");
        
        Usuario usuarioFalso = new Usuario();
        usuarioFalso.setIdUsuario(1);
        usuarioFalso.setNombre(faker.residentEvil().character());
        usuarioFalso.setCorreo(faker.internet().emailAddress());

        Roles rolFalso = new Roles();
        rolFalso.setIdroles(idSimulado);
        rolFalso.setNombre(rolAleatorio);
        rolFalso.setUsuario(usuarioFalso);

        when(rolesRepository.findById(idSimulado)).thenReturn(Optional.of(rolFalso));

        RolesDTO resultado = rolesService.buscarPorId(idSimulado);

        assertNotNull(resultado, "El DTO resultante no debería ser nulo");
        assertEquals(rolAleatorio, resultado.getNombre());

        assertNotNull(resultado.getNombreUsuarios(), "El usuario dentro del DTO no debería ser nulo");
        assertEquals(usuarioFalso, resultado.getNombreUsuarios());
    
        verify(rolesRepository, times(1)).findById(idSimulado);

    }
}
