package com.example.back.mapper.room;

import com.example.back.dto.room.RoomCategoryDTO;
import com.example.back.models.room.RoomCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomCategoryMapper {
    @Mapping(source = "usuario.idUsuario", target = "usuarioId")
    RoomCategoryDTO categoriaHabitacionToCategoriaHabitacionDTO(RoomCategory categoriaHabitacion);

    @Mapping(target = "usuario", ignore = true)
    RoomCategory categoriaHabitacionDTOToCategoriaHabitacion(RoomCategoryDTO categoriaHabitacionDTO);
}
