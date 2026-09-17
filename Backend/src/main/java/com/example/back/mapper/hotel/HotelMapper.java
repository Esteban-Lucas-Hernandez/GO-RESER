package com.example.back.mapper.hotel;

import com.example.back.dto.hotel.HotelDTO;
import com.example.back.models.hotel.Hotel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HotelMapper {

    public HotelDTO hotelToHotelDTO(Hotel hotel) {
        if (hotel == null) return null;
        HotelDTO dto = new HotelDTO();
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
            dto.setCiudadNombre(hotel.getCiudad().getNombre());
            dto.setCiudadId(hotel.getCiudad().getId());
            if (hotel.getCiudad().getDepartamento() != null) {
                dto.setDepartamentoNombre(hotel.getCiudad().getDepartamento().getNombre());
            }
        }

        if (hotel.getUsuario() != null) {
            dto.setUsuarioId(hotel.getUsuario().getIdUsuario());
        }

        return dto;
    }

    public Hotel hotelDTOToHotel(HotelDTO hotelDTO) {
        if (hotelDTO == null) return null;
        Hotel hotel = new Hotel();
        hotel.setId(hotelDTO.getId());
        hotel.setNombre(hotelDTO.getNombre());
        hotel.setDireccion(hotelDTO.getDireccion());
        hotel.setTelefono(hotelDTO.getTelefono());
        hotel.setEmail(hotelDTO.getEmail());
        hotel.setDescripcion(hotelDTO.getDescripcion());
        hotel.setEstrellas(hotelDTO.getEstrellas());
        hotel.setPoliticaCancelacion(hotelDTO.getPoliticaCancelacion());
        hotel.setCheckIn(hotelDTO.getCheckIn());
        hotel.setCheckOut(hotelDTO.getCheckOut());
        hotel.setImagenUrl(hotelDTO.getImagenUrl());
        return hotel;
    }

    public void updateHotelFromDTO(HotelDTO hotelDTO, Hotel hotel) {
        if (hotelDTO != null && hotel != null) {
            hotel.setNombre(hotelDTO.getNombre());
            hotel.setDireccion(hotelDTO.getDireccion());
            hotel.setTelefono(hotelDTO.getTelefono());
            hotel.setEmail(hotelDTO.getEmail());
            hotel.setDescripcion(hotelDTO.getDescripcion());
            hotel.setEstrellas(hotelDTO.getEstrellas());
            hotel.setPoliticaCancelacion(hotelDTO.getPoliticaCancelacion());
            hotel.setCheckIn(hotelDTO.getCheckIn());
            hotel.setCheckOut(hotelDTO.getCheckOut());
            hotel.setImagenUrl(hotelDTO.getImagenUrl());
        }
    }

    public List<HotelDTO> hotelsToHotelDTOs(List<Hotel> hotels) {
        return hotels.stream()
                .map(this::hotelToHotelDTO)
                .collect(Collectors.toList());
    }
}
