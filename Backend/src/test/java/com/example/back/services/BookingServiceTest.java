package com.example.back.services;

import com.example.back.dto.booking.BookingDTO;
import com.example.back.dto.booking.CreateBookingDTO;
import com.example.back.models.booking.Booking;
import com.example.back.models.room.Room;
import com.example.back.models.user.User;
import com.example.back.repo.booking.BookingRepository;
import com.example.back.repo.room.RoomRepository;
import com.example.back.services.impl.BookingServiceImpl;
import com.example.back.services.interfaces.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void testCrearReservaValida() {
        User user = new User();
        user.setIdUsuario(1);
        when(securityService.getAuthenticatedUser()).thenReturn(user);

        Room room = new Room();
        room.setIdHabitacion(10);
        room.setPrecio(150000.0);
        when(roomRepository.findByIdHabitacion(10)).thenReturn(Optional.of(room));

        CreateBookingDTO dto = new CreateBookingDTO();
        dto.setFechaInicio(LocalDate.now().plusDays(1));
        dto.setFechaFin(LocalDate.now().plusDays(3));
        dto.setMetodoPago("tarjeta");

        when(bookingRepository.existsSolapadas(eq(room), any(), any())).thenReturn(false);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            b.setIdReserva(99);
            return b;
        });

        BookingDTO result = bookingService.crearReserva(10, dto);
        assertNotNull(result);
    }
}
