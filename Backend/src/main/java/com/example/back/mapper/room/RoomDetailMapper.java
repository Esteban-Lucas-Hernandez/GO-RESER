package com.example.back.mapper.room;

import com.example.back.dto.room.RoomDetailDTO;
import com.example.back.models.room.Room;
import com.example.back.models.room.RoomImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {RoomCategoryMapper.class})
public interface RoomDetailMapper {

    @Mapping(source = "hotel.id", target = "idHotel")
    @Mapping(source = "hotel.nombre", target = "hotelNombre")
    @Mapping(source = "hotel.descripcion", target = "descripcionHotel")
    @Mapping(source = "hotel.ciudad.nombre", target = "ciudadNombre")
    @Mapping(source = "hotel.ciudad.departamento.nombre", target = "departamentoNombre")
    @Mapping(source = "hotel.checkIn", target = "checkIn")
    @Mapping(source = "hotel.checkOut", target = "checkOut")
    @Mapping(source = "imagenes", target = "imagenesUrls", qualifiedByName = "mapImagesToUrls")
    RoomDetailDTO habitacionToHabitacionDetalleDTO(Room habitacion);

    @Named("mapImagesToUrls")
    default List<String> mapImagesToUrls(List<RoomImage> imagenes) {
        if (imagenes == null) return null;
        return imagenes.stream()
                .map(RoomImage::getUrlImagen)
                .collect(Collectors.toList());
    }
}
