package com.example.Multiverse.controller;

import com.example.Multiverse.dto.PeliculaDto;
import com.example.Multiverse.service.PeliculaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PeliculaController.class)
class PeliculaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PeliculaService service;

    private static final String PELICULA_JSON = """
        {
            "nombre": "Matrix",
            "categoria": "Sci-Fi",
            "año": 1999,
            "director": "Wachowski",
            "duracion": 136,
            "calificacion": 8.7
        }
        """;

    private PeliculaDto testDto;

    @BeforeEach
    void setUp() {
        testDto = new PeliculaDto();
        testDto.setId(1L);
        testDto.setNombre("Matrix");
        testDto.setCategoria("Sci-Fi");
        testDto.setAño(1999);
        testDto.setDirector("Wachowski");
        testDto.setDuracion(136);
        testDto.setCalificacion(8.7);
    }

    @Test
    void testGetAllPeliculas() throws Exception {
        Mockito.when(service.findAll()).thenReturn(List.of(testDto));
        
        mockMvc.perform(get("/peliculas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Matrix"))
                .andExpect(jsonPath("$[0].categoria").value("Sci-Fi"))
                .andExpect(jsonPath("$[0].año").value(1999));
    }

    @Test
    void testGetAllPeliculasEmpty() throws Exception {
        Mockito.when(service.findAll()).thenReturn(List.of());
        
        mockMvc.perform(get("/peliculas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGetPeliculaById() throws Exception {
        Mockito.when(service.findById(1L)).thenReturn(testDto);
        
        mockMvc.perform(get("/peliculas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Matrix"))
                .andExpect(jsonPath("$.categoria").value("Sci-Fi"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetPeliculaByIdNotFound() throws Exception {
        Mockito.when(service.findById(999L)).thenThrow(new RuntimeException("Película no encontrada"));
        
        mockMvc.perform(get("/peliculas/999"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testCreatePelicula() throws Exception {
        Mockito.when(service.create(any(PeliculaDto.class))).thenReturn(testDto);
        
        mockMvc.perform(post("/peliculas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(PELICULA_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Matrix"))
                .andExpect(jsonPath("$.categoria").value("Sci-Fi"));
    }

    @Test
    void testCreatePeliculaInvalidData() throws Exception {
        String invalidJson = "{\"nombre\": \"\"}";
        
        mockMvc.perform(post("/peliculas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdatePelicula() throws Exception {
        Mockito.when(service.update(eq(1L), any(PeliculaDto.class))).thenReturn(testDto);
        
        mockMvc.perform(put("/peliculas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(PELICULA_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Matrix"))
                .andExpect(jsonPath("$.categoria").value("Sci-Fi"));
    }

    @Test
    void testUpdatePeliculaNotFound() throws Exception {
        Mockito.when(service.update(eq(999L), any(PeliculaDto.class)))
                .thenThrow(new RuntimeException("Película no encontrada"));
        
        mockMvc.perform(put("/peliculas/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(PELICULA_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testDeletePelicula() throws Exception {
        Mockito.doNothing().when(service).deleteById(1L);
        
        mockMvc.perform(delete("/peliculas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeletePeliculaNotFound() throws Exception {
        Mockito.doThrow(new RuntimeException("Película no encontrada"))
                .when(service).deleteById(999L);
        
        mockMvc.perform(delete("/peliculas/999"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testCreatePeliculaMalformedJson() throws Exception {
        String malformedJson = "{\"nombre\": \"Matrix\", \"categoria\"";
        
        mockMvc.perform(post("/peliculas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson))
                .andExpect(status().isBadRequest());
    }
}