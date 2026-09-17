package com.example.back.mapper.hotel;

import com.example.back.dto.hotel.CityDTO;
import com.example.back.dto.hotel.HotelPublicDTO;
import com.example.back.models.hotel.Hotel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HotelPublicMapper {

    public HotelPublicDTO hotelToHotelPublicDTO(Hotel hotel) {
        if (hotel == null) return null;
        HotelPublicDTO dto = new HotelPublicDTO();
        dto.setId(hotel.getId());
        dto.setNombre(hotel.getNombre());
        dto.setDireccion(hotel.getDireccion());
        dto.setTelefono(hotel.getTelefono());
        dto.setEmail(hotel.getEmail());
        dto.setDescripcion(hotel.getDescripcion());
        dto.setEstrellas(hotel.getEstrellas());
        dto.setPoliticaCancelacion(hotel.getPoliticaCancelacion());
        dto.setCheckIn(hotel.getCheckIn());
        dto.setCheckOut(hotel.getCheckOut());
        dto.setImagenUrl(hotel.getImagenUrl());
        dto.setCreatedAt(hotel.getCreatedAt());
        dto.setUpdatedAt(hotel.getUpdatedAt());

        if (hotel.getCiudad() != null) {
            CityDTO ciudadDto = new CityDTO();
            ciudadDto.setId(hotel.getCiudad().getId());
            ciudadDto.setNombre(hotel.getCiudad().getNombre());
            if (hotel.getCiudad().getLatitud() != null) {
                ciudadDto.setLatitud(hotel.getCiudad().getLatitud());
                dto.setLatitud(hotel.getCiudad().getLatitud().doubleValue());
            }
            if (hotel.getCiudad().getLongitud() != null) {
                ciudadDto.setLongitud(hotel.getCiudad().getLongitud());
                dto.setLongitud(hotel.getCiudad().getLongitud().doubleValue());
            }
            if (hotel.getCiudad().getDepartamento() != null) {
                ciudadDto.setDepartamentoId(hotel.getCiudad().getDepartamento().getId());
                ciudadDto.setDepartamentoNombre(hotel.getCiudad().getDepartamento().getNombre());
            }
            dto.setCiudad(ciudadDto);
        }

        return dto;
    }

    public List<HotelPublicDTO> hotelsToHotelPublicDTOs(List<Hotel> hotels) {
        return hotels.stream()
                .map(this::hotelToHotelPublicDTO)
                .collect(Collectors.toList());
    }
}
