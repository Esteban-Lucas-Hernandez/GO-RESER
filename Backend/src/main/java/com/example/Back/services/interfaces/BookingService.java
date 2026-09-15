package com.example.back.services.interfaces;

import com.example.back.dto.booking.BookingDTO;
import com.example.back.dto.booking.CreateBookingDTO;
import com.example.back.models.booking.Booking;
import java.util.List;

public interface BookingService {
    BookingDTO crearReserva(Integer idHabitacion, CreateBookingDTO reservaDTO);
    List<BookingDTO> getReservasPorUsuario();
    List<BookingDTO> getReservasPorHabitacion(Integer idHabitacion);
    List<BookingDTO> getReservasDeMisHoteles();
    List<BookingDTO> getReservasPorHotelDeUsuario(Integer idHotel);
    BookingDTO cancelarReserva(Integer idReserva);
    Booking getReservaPorId(Integer idReserva);
    List<Object[]> getFechasReservadas(Integer idHabitacion);
    List<Object[]> getFechasReservadasConfirmadas(Integer idHabitacion);
    BookingDTO confirmarReserva(Integer idReserva);
    int eliminarReservasAntiguasYCanceladas();
}
