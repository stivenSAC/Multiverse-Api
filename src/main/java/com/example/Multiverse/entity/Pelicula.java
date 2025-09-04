package com.example.Multiverse.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "peliculas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pelicula {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String categoria;
    
    @Column(nullable = false)
    private Integer año;
    
    @Column(nullable = false)
    private String director;
    
    @Column(nullable = false)
    private Integer duracion;
    
    @Column(nullable = false)
    private Double calificacion;
}