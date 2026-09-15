package com.example.back.mapper.booking;

import com.example.back.dto.booking.BookingDTO;
import com.example.back.models.booking.Booking;
import com.example.back.models.room.RoomImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(source = "usuario.email", target = "emailUsuario")
    @Mapping(source = "habitacion.numero", target = "numeroHabitacion")
    @Mapping(source = "habitacion.hotel.nombre", target = "nombreHotel")
    @Mapping(source = "habitacion.imagenes", target = "urlImagenHabitacion", qualifiedByName = "mapFirstImageUrl")
    BookingDTO reservaToReservaDTO(Booking reserva);

    @Mapping(source = "emailUsuario", target = "usuario.email")
    @Mapping(source = "numeroHabitacion", target = "habitacion.numero")
    @Mapping(source = "nombreHotel", target = "habitacion.hotel.nombre")
    @Mapping(source = "urlImagenHabitacion", target = "habitacion.imagenes", ignore = true)
    Booking reservaDTOToReserva(BookingDTO reservaDTO);

    @Named("mapFirstImageUrl")
    default String mapFirstImageUrl(List<RoomImage> imagenes) {
        if (imagenes != null && !imagenes.isEmpty()) {
            return imagenes.get(0).getUrlImagen();
        }
        return null;
    }
}
