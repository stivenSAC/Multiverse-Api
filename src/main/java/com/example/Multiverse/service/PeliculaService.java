package com.example.Multiverse.service;

import com.example.Multiverse.dto.PeliculaDto;
import com.example.Multiverse.entity.Pelicula;
import com.example.Multiverse.repository.PeliculaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PeliculaService {
    
    private final PeliculaRepository repository;
    
    @Transactional(readOnly = true)
    public List<PeliculaDto> findAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public PeliculaDto findById(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));
    }
    
    public PeliculaDto create(PeliculaDto createDto) {
        Pelicula pelicula = toEntity(createDto);
        return toDto(repository.save(pelicula));
    }
    
    public PeliculaDto update(Long id, PeliculaDto updateDto) {
        Pelicula pelicula = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));
        
        updateEntity(pelicula, updateDto);
        return toDto(repository.save(pelicula));
    }
    
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Película no encontrada");
        }
        repository.deleteById(id);
    }
    
    private PeliculaDto toDto(Pelicula entity) {
        PeliculaDto dto = new PeliculaDto();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setCategoria(entity.getCategoria());
        dto.setAño(entity.getAño());
        dto.setDirector(entity.getDirector());
        dto.setDuracion(entity.getDuracion());
        dto.setCalificacion(entity.getCalificacion());
        return dto;
    }
    
    private Pelicula toEntity(PeliculaDto dto) {
        Pelicula entity = new Pelicula();
        updateEntity(entity, dto);
        return entity;
    }
    
    private void updateEntity(Pelicula entity, PeliculaDto dto) {
        entity.setNombre(dto.getNombre());
        entity.setCategoria(dto.getCategoria());
        entity.setAño(dto.getAño());
        entity.setDirector(dto.getDirector());
        entity.setDuracion(dto.getDuracion());
        entity.setCalificacion(dto.getCalificacion());
    }
}