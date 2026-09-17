package com.example.back.services.interfaces;

import com.example.back.dto.room.RoomImageDTO;
import java.util.List;

public interface RoomImageService {
    List<RoomImageDTO> getImagenesByHabitacionId(Integer hotelId, Integer habitacionId);
    RoomImageDTO createImagen(Integer hotelId, Integer habitacionId, RoomImageDTO imagenDTO);
    boolean deleteImagen(Integer hotelId, Integer habitacionId, Integer imagenId);
}
