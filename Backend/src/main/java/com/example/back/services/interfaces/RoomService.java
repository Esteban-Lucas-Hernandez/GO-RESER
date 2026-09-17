package com.example.back.services.interfaces;

import com.example.back.dto.room.CreateRoomDTO;
import com.example.back.dto.room.RoomDTO;
import java.util.List;
import java.util.Optional;

public interface RoomService {
    List<RoomDTO> getHabitacionesByHotelId(Integer hotelId);
    Optional<RoomDTO> getHabitacionByIdAndHotelId(Integer habitacionId, Integer hotelId);
    List<RoomDTO> getHabitacionesDeMisHoteles();
    Optional<RoomDTO> createHabitacion(Integer hotelId, RoomDTO habitacionDTO);
    Optional<RoomDTO> createHabitacionDesdeDTO(Integer hotelId, CreateRoomDTO crearHabitacionDTO);
    Optional<RoomDTO> updateHabitacion(Integer habitacionId, Integer hotelId, RoomDTO habitacionDTO);
    boolean deleteHabitacion(Integer habitacionId, Integer hotelId);
}
