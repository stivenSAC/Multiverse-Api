package com.example.Multiverse.controller;

import com.example.Multiverse.dto.PeliculaDto;
import com.example.Multiverse.service.PeliculaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PeliculaController.class)
class PeliculaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PeliculaService service;

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
        when(service.findAll()).thenReturn(List.of(testDto));
        
        mockMvc.perform(get("/peliculas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Matrix"))
                .andExpect(jsonPath("$[0].categoria").value("Sci-Fi"))
                .andExpect(jsonPath("$[0].año").value(1999));
    }

    @Test
    void testGetAllPeliculasEmpty() throws Exception {
        when(service.findAll()).thenReturn(List.of());
        
        mockMvc.perform(get("/peliculas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGetPeliculaById() throws Exception {
        when(service.findById(1L)).thenReturn(testDto);
        
        mockMvc.perform(get("/peliculas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Matrix"))
                .andExpect(jsonPath("$.categoria").value("Sci-Fi"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetPeliculaByIdNotFound() throws Exception {
        when(service.findById(999L)).thenThrow(new RuntimeException("Película no encontrada"));
        
        mockMvc.perform(get("/peliculas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreatePelicula() throws Exception {
        when(service.create(any(PeliculaDto.class))).thenReturn(testDto);
        
        mockMvc.perform(post("/peliculas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Matrix"))
                .andExpect(jsonPath("$.categoria").value("Sci-Fi"));
    }

    @Test
    void testCreatePeliculaInvalidData() throws Exception {
        PeliculaDto invalidDto = new PeliculaDto();
        invalidDto.setNombre(""); // Nombre vacío
        
        mockMvc.perform(post("/peliculas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdatePelicula() throws Exception {
        when(service.update(eq(1L), any(PeliculaDto.class))).thenReturn(testDto);
        
        mockMvc.perform(put("/peliculas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Matrix"))
                .andExpect(jsonPath("$.categoria").value("Sci-Fi"));
    }

    @Test
    void testUpdatePeliculaNotFound() throws Exception {
        when(service.update(eq(999L), any(PeliculaDto.class)))
                .thenThrow(new RuntimeException("Película no encontrada"));
        
        mockMvc.perform(put("/peliculas/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletePelicula() throws Exception {
        doNothing().when(service).deleteById(1L);
        
        mockMvc.perform(delete("/peliculas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeletePeliculaNotFound() throws Exception {
        doThrow(new RuntimeException("Película no encontrada"))
                .when(service).deleteById(999L);
        
        mockMvc.perform(delete("/peliculas/999"))
                .andExpect(status().isNotFound());
        
        verify(service).deleteById(999L);
    }
}