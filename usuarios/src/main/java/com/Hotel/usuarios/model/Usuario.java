package com.Hotel.usuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @NotBlank(message = "el nombre no puede estar en blanco")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 a 100 caracteres")
    @Column(name = "nombre")
    private String nombre;

    @NotBlank(message = "el correo no puede estar en blanco")
    @Column(name = "correo")
    private String correo;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Roles roles;
}