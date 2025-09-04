package com.example.Multiverse.controller;

import com.example.Multiverse.dto.PeliculaDto;
import com.example.Multiverse.service.PeliculaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/peliculas")
@RequiredArgsConstructor
public class PeliculaController {
    
    private final PeliculaService peliculaService;
    
    @GetMapping
    public ResponseEntity<List<PeliculaDto>> getAllPeliculas() {
        return ResponseEntity.ok(peliculaService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PeliculaDto> getPeliculaById(@PathVariable Long id) {
        return ResponseEntity.ok(peliculaService.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<PeliculaDto> createPelicula(@Valid @RequestBody PeliculaDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(peliculaService.create(createDto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PeliculaDto> updatePelicula(
            @PathVariable Long id, 
            @Valid @RequestBody PeliculaDto updateDto) {
        return ResponseEntity.ok(peliculaService.update(id, updateDto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePelicula(@PathVariable Long id) {
        peliculaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}