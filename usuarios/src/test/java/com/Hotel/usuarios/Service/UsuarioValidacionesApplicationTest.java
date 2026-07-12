package com.Hotel.usuarios.Service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import com.Hotel.usuarios.model.Usuario;
import com.Hotel.usuarios.model.Roles;
import com.Hotel.usuarios.repository.RolesRepository;
import com.Hotel.usuarios.repository.UsuarioRepository;
import com.Hotel.usuarios.service.UsuarioValidaciones;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
public class UsuarioValidacionesApplicationTest {

    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioValidaciones usuarioValidaciones;

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testValidarRegrasNegocio_CorreoValido() {
        Usuario usuarioValido = new Usuario();
        usuarioValido.setNombre(faker.residentEvil().character());
        usuarioValido.setCorreo(faker.internet().emailAddress()); 

        // WHEN: Ejecutamos la regla de negocio
        boolean resultado = usuarioValidaciones.validarRegrasNegocio(usuarioValido);

        // THEN: Debería dar verdadero
        assertTrue(resultado, "El formato de correo válido debería ser aceptado");
    }

    @Test
    void testValidarRegrasNegocio_CorreoInvalido() {
        // GIVEN: Un usuario con un correo sin arroba
        Usuario usuarioInvalido = new Usuario();
        usuarioInvalido.setNombre(faker.residentEvil().character());
        usuarioInvalido.setCorreo("umbrella_corporation_email.com"); 

        // WHEN: Ejecutamos la regla de negocio
        boolean resultado = usuarioValidaciones.validarRegrasNegocio(usuarioInvalido);

        // THEN: Debería dar falso
        assertFalse(resultado, "El formato de correo sin '@' debería ser rechazado");
    }


    @Test
    void testElCorreoYaExiste_Verdadero() {
        // GIVEN: Un correo que ya está registrado en el sistema
        String correoRegistrado = faker.internet().emailAddress();
        when(usuarioRepository.existsByCorreo(correoRegistrado)).thenReturn(true);

        // WHEN: Consultamos la validación
        boolean resultado = usuarioValidaciones.elCorreoYaExiste(correoRegistrado);

        // THEN: Validamos que retorne true y consulte al repositorio
        assertTrue(resultado);
        verify(usuarioRepository, times(1)).existsByCorreo(correoRegistrado);
    }

    @Test
    void testElCorreoYaExiste_Falso() {
        String correoNuevo = faker.internet().emailAddress();
        when(usuarioRepository.existsByCorreo(correoNuevo)).thenReturn(false);

        // WHEN: Consultamos la validación
        boolean resultado = usuarioValidaciones.elCorreoYaExiste(correoNuevo);

        // THEN: Validamos que retorne false y consulte al repositorio
        assertFalse(resultado);
        verify(usuarioRepository, times(1)).existsByCorreo(correoNuevo);
    }

    @Test
    void testExisteRolAsignado_Exitoso() {
        Integer idRolSimulado = 1;
        
        Roles rolFalso = new Roles();
        rolFalso.setIdroles(idRolSimulado);
        rolFalso.setNombre("Administracion");

        Usuario usuarioConRol = new Usuario();
        usuarioConRol.setRoles(rolFalso);

        when(rolesRepository.existsById(idRolSimulado)).thenReturn(true);

        // WHEN: Validamos si el rol existe
        boolean resultado = usuarioValidaciones.existeRolAsignado(usuarioConRol);

        // THEN: Verificaciones
        assertTrue(resultado, "El rol existe en el repositorio, debería ser verídico");
        verify(rolesRepository, times(1)).existsById(idRolSimulado);
    }

    @Test
    void testExisteRolAsignado_NoEncontrado() {
        // GIVEN: Un usuario con un ID de rol que no existe en el sistema
        Integer idRolInexistente = 99;
        
        Roles rolInvalido = new Roles();
        rolInvalido.setIdroles(idRolInexistente);

        Usuario usuarioConRolInvalido = new Usuario();
        usuarioConRolInvalido.setRoles(rolInvalido);

        when(rolesRepository.existsById(idRolInexistente)).thenReturn(false);

        // WHEN: Validamos
        boolean resultado = usuarioValidaciones.existeRolAsignado(usuarioConRolInvalido);

        // THEN: Verificaciones
        assertFalse(resultado, "El rol no existe en el sistema, debería ser rechazado");
        verify(rolesRepository, times(1)).existsById(idRolInexistente);
    }

    @Test
    void testExisteRolAsignado_RolesNull() {
        // GIVEN: Un usuario que fue enviado sin el objeto de Roles asignado
        Usuario usuarioSinRol = new Usuario();
        usuarioSinRol.setRoles(null);

        // WHEN: Validamos
        boolean resultado = usuarioValidaciones.existeRolAsignado(usuarioSinRol);

        // THEN: Debería denegar el paso con un false controlado sin caer en NullPointerException
        assertFalse(resultado, "Un usuario con objeto de rol nulo debe devolver falso");
    }
}