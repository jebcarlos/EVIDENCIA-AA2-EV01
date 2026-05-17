package com.pqrs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "fundusuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "email")
    private String email;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "fecha_creacion")
    private java.time.LocalDateTime fechaCreacion;

}
