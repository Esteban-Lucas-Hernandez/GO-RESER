package com.example.back.mapper.hotel;

import com.example.back.dto.hotel.CityDTO;
import com.example.back.dto.hotel.StateDTO;
import com.example.back.models.hotel.City;
import com.example.back.models.hotel.State;
import org.springframework.stereotype.Component;

@Component
public class StateMapper {

    public StateDTO toStateDTO(State departamento) {
        if (departamento == null) return null;
        StateDTO dto = new StateDTO();
        dto.setId(departamento.getId());
        dto.setNombre(departamento.getNombre());
        return dto;
    }

    public CityDTO toCityDTO(City ciudad) {
        if (ciudad == null) return null;
        CityDTO dto = new CityDTO();
        dto.setId(ciudad.getId());
        dto.setNombre(ciudad.getNombre());
        dto.setLatitud(ciudad.getLatitud());
        dto.setLongitud(ciudad.getLongitud());
        if (ciudad.getDepartamento() != null) {
            dto.setDepartamentoId(ciudad.getDepartamento().getId());
            dto.setDepartamentoNombre(ciudad.getDepartamento().getNombre());
        }
        return dto;
    }
}
