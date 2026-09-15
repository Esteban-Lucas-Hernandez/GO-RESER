package com.example.back.services.interfaces;

import com.example.back.dto.hotel.HotelDTO;
import com.example.back.models.hotel.Hotel;
import java.util.List;
import java.util.Optional;

public interface HotelService {
    List<Hotel> getAllHoteles();
    List<HotelDTO> getAllHotelesDTO();
    List<HotelDTO> getHotelesByCurrentUser();
    Optional<Hotel> getHotelById(Integer id);
    Hotel createHotel(HotelDTO hotelDTO);
    Hotel updateHotel(Integer id, HotelDTO hotelDTO);
    boolean deleteHotelWithCascade(Integer id);
    boolean hasHabitaciones(Integer hotelId);
}
