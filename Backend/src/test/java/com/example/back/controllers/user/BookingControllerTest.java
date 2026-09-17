package com.example.back.controllers.user;

import com.example.back.dto.booking.BookingDTO;
import com.example.back.dto.booking.CreateBookingDTO;
import com.example.back.services.interfaces.BookingService;
import com.example.back.services.interfaces.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private BookingController bookingController;

    @Test
    void testCrearReserva() {
        CreateBookingDTO dto = new CreateBookingDTO();
        dto.setFechaInicio(LocalDate.now().plusDays(1));
        dto.setFechaFin(LocalDate.now().plusDays(3));

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setIdReserva(1);

        when(bookingService.crearReserva(eq(10), any(CreateBookingDTO.class))).thenReturn(bookingDTO);

        ResponseEntity<?> response = bookingController.crearReserva(10, dto);
        assertEquals(201, response.getStatusCode().value());
    }
}
