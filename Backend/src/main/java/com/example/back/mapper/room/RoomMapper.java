package com.example.back.mapper.room;

import com.example.back.dto.room.RoomDTO;
import com.example.back.models.room.Room;
import com.example.back.models.room.RoomImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {RoomCategoryMapper.class})
public interface RoomMapper {

    @Mapping(source = "hotel.id", target = "idHotel")
    @Mapping(source = "hotel.nombre", target = "hotelNombre")
    @Mapping(source = "hotel.estrellas", target = "estrellas")
    @Mapping(source = "hotel.email", target = "email")
    @Mapping(source = "hotel.descripcion", target = "descripcionHotel")
    @Mapping(source = "hotel.checkIn", target = "checkIn")
    @Mapping(source = "hotel.checkOut", target = "checkOut")
    @Mapping(source = "hotel.imagenUrl", target = "hotelImagenUrl")
    @Mapping(source = "hotel.politicaCancelacion", target = "politicaCancelacion")
    @Mapping(source = "hotel.ciudad.nombre", target = "ciudadNombre")
    @Mapping(source = "hotel.ciudad.latitud", target = "latitud")
    @Mapping(source = "hotel.ciudad.longitud", target = "longitud")
    @Mapping(source = "hotel.ciudad.departamento.nombre", target = "departamentoNombre")
    @Mapping(source = "imagenes", target = "imagenUrl", qualifiedByName = "mapFirstImageUrl")
    @Mapping(source = "imagenes", target = "imagenesUrls", qualifiedByName = "mapAllImageUrls")
    RoomDTO habitacionToHabitacionDTO(Room habitacion);

    List<RoomDTO> habitacionesToHabitacionDTOs(List<Room> habitaciones);

    @Named("mapFirstImageUrl")
    default String mapFirstImageUrl(List<RoomImage> imagenes) {
        if (imagenes != null && !imagenes.isEmpty()) {
            return imagenes.get(0).getUrlImagen();
        }
        return null;
    }

    @Named("mapAllImageUrls")
    default List<String> mapAllImageUrls(List<RoomImage> imagenes) {
        if (imagenes != null) {
            return imagenes.stream()
                    .map(RoomImage::getUrlImagen)
                    .collect(Collectors.toList());
        }
        return null;
    }
}
