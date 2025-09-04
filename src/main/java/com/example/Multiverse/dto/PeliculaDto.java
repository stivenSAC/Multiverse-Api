package com.example.Multiverse.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class PeliculaDto {
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;
    
    @NotNull(message = "El año es obligatorio")
    @Min(value = 1900, message = "El año debe ser mayor a 1900")
    private Integer año;
    
    @NotBlank(message = "El director es obligatorio")
    private String director;
    
    @NotNull(message = "La duración es obligatoria")
    @Min(value = 1, message = "La duración debe ser mayor a 0")
    private Integer duracion;
    
    @NotNull(message = "La calificación es obligatoria")
    @DecimalMin(value = "0.0", message = "La calificación debe ser mayor o igual a 0")
    @DecimalMax(value = "10.0", message = "La calificación debe ser menor o igual a 10")
    private Double calificacion;
}