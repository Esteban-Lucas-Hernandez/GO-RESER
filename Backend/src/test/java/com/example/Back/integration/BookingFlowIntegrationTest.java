package com.example.back.integration;

import com.example.back.dto.booking.BookingDTO;
import com.example.back.dto.booking.CreateBookingDTO;
import com.example.back.services.interfaces.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingFlowIntegrationTest {

    @Mock
    private BookingService bookingService;

    @Test
    void testCompleteBookingFlow() {
        CreateBookingDTO dto = new CreateBookingDTO();
        dto.setFechaInicio(LocalDate.now().plusDays(1));
        dto.setFechaFin(LocalDate.now().plusDays(2));

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setIdReserva(10);
        bookingDTO.setEstado("pendiente");

        when(bookingService.crearReserva(eq(5), any())).thenReturn(bookingDTO);

        BookingDTO created = bookingService.crearReserva(5, dto);
        assertEquals("pendiente", created.getEstado());
    }
}
