package com.example.back.services.interfaces;

import com.example.back.dto.booking.BookingDTO;
import com.example.back.dto.hotel.HotelDTO;
import com.example.back.dto.room.RoomDTO;
import com.example.back.dto.user.UserDTO;
import com.example.back.models.user.User;
import java.util.List;

public interface SuperAdminService {
    List<UserDTO> listarUsuarios();
    User obtenerUsuarioPorId(Integer idUsuario);
    UserDTO actualizarRoles(Integer idUsuario, List<String> nombresRoles);
    UserDTO cambiarEstadoUsuario(Integer idUsuario, Boolean estado);
    void eliminarUsuario(Integer idUsuario);
    List<HotelDTO> listarHoteles();
    boolean eliminarHotelConDependencias(Integer idHotel);
    List<RoomDTO> listarHabitaciones();
    List<RoomDTO> listarHabitacionesPorHotel(Integer idHotel);
    List<BookingDTO> listarReservas();
    List<BookingDTO> listarReservasPorHotel(Integer idHotel);
    BookingDTO obtenerReservaPorId(Integer idReserva);
}
