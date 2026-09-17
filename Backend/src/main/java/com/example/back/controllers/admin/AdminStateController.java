package com.example.back.controllers.admin;

import com.example.back.dto.hotel.CityDTO;
import com.example.back.dto.hotel.StateDTO;
import com.example.back.mapper.hotel.StateMapper;
import com.example.back.models.hotel.State;
import com.example.back.repo.hotel.CityRepository;
import com.example.back.repo.hotel.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/departamentos")
public class AdminStateController {

    @Autowired
    private StateRepository departamentoRepository;

    @Autowired
    private CityRepository ciudadRepository;

    @Autowired
    private StateMapper departamentoMapper;

    @GetMapping
    public ResponseEntity<List<StateDTO>> getAllDepartamentos() {
        List<StateDTO> dtos = departamentoRepository.findAll().stream()
                .map(departamentoMapper::toStateDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/ciudades")
    public ResponseEntity<List<CityDTO>> getCiudadesPorDepartamento(@PathVariable Integer id) {
        State depto = departamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));
        List<CityDTO> dtos = ciudadRepository.findByDepartamento(depto).stream()
                .map(departamentoMapper::toCityDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
