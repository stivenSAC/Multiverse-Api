package com.example.Multiverse.service;

import com.example.Multiverse.dto.PeliculaDto;
import com.example.Multiverse.entity.Pelicula;
import com.example.Multiverse.repository.PeliculaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PeliculaServiceTest {

    @Mock
    private PeliculaRepository repository;

    @InjectMocks
    private PeliculaService service;

    private Pelicula testPelicula;
    private PeliculaDto testPeliculaDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testPelicula = new Pelicula(1L, "Matrix", "Sci-Fi", 1999, "Wachowski", 136, 8.7);
        testPeliculaDto = createPeliculaDto();
    }

    private PeliculaDto createPeliculaDto() {
        PeliculaDto dto = new PeliculaDto();
        dto.setId(1L);
        dto.setNombre("Matrix");
        dto.setCategoria("Sci-Fi");
        dto.setAño(1999);
        dto.setDirector("Wachowski");
        dto.setDuracion(136);
        dto.setCalificacion(8.7);
        return dto;
    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(List.of(testPelicula));
        
        List<PeliculaDto> result = service.findAll();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Matrix", result.get(0).getNombre());
        assertEquals("Sci-Fi", result.get(0).getCategoria());
        verify(repository).findAll();
    }

    @Test
    void testFindAllEmptyList() {
        when(repository.findAll()).thenReturn(List.of());
        
        List<PeliculaDto> result = service.findAll();
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).findAll();
    }

    @Test
    void testFindByIdFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(testPelicula));
        
        PeliculaDto result = service.findById(1L);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Matrix", result.getNombre());
        verify(repository).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());
        
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> service.findById(2L));
        
        assertEquals("Película no encontrada", exception.getMessage());
        verify(repository).findById(2L);
    }

    @Test
    void testCreate() {
        when(repository.save(any(Pelicula.class))).thenReturn(testPelicula);
        
        PeliculaDto result = service.create(testPeliculaDto);
        
        assertNotNull(result);
        assertEquals("Matrix", result.getNombre());
        assertEquals("Sci-Fi", result.getCategoria());
        verify(repository).save(any(Pelicula.class));
    }

    @Test
    void testUpdateFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(testPelicula));
        when(repository.save(any(Pelicula.class))).thenReturn(testPelicula);
        
        PeliculaDto result = service.update(1L, testPeliculaDto);
        
        assertNotNull(result);
        assertEquals("Matrix", result.getNombre());
        verify(repository).findById(1L);
        verify(repository).save(any(Pelicula.class));
    }

    @Test
    void testUpdateNotFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());
        
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> service.update(2L, testPeliculaDto));
        
        assertEquals("Película no encontrada", exception.getMessage());
        verify(repository).findById(2L);
        verify(repository, never()).save(any(Pelicula.class));
    }

    @Test
    void testDeleteByIdFound() {
        when(repository.existsById(1L)).thenReturn(true);
        
        assertDoesNotThrow(() -> service.deleteById(1L));
        
        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void testDeleteByIdNotFound() {
        when(repository.existsById(2L)).thenReturn(false);
        
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> service.deleteById(2L));
        
        assertEquals("Película no encontrada", exception.getMessage());
        verify(repository).existsById(2L);
        verify(repository, never()).deleteById(2L);
    }
}