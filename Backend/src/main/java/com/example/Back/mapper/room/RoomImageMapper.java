package com.example.back.mapper.room;

import com.example.back.dto.room.RoomImageDTO;
import com.example.back.models.room.RoomImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomImageMapper {
    @Mapping(source = "habitacion.idHabitacion", target = "idHabitacion")
    RoomImageDTO imagenHabitacionToImagenHabitacionDTO(RoomImage imagenHabitacion);

    List<RoomImageDTO> imagenesHabitacionToImagenesHabitacionDTOs(List<RoomImage> imagenesHabitacion);
}
