package com.example.back.controllers.admin;

import com.example.back.dto.booking.BookingDTO;
import com.example.back.services.interfaces.BookingService;
import com.example.back.services.interfaces.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminBookingsControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private AdminBookingsController adminBookingsController;

    @Test
    void testGetReservasDeMisHoteles() {
        when(bookingService.getReservasDeMisHoteles()).thenReturn(List.of(new BookingDTO()));
        ResponseEntity<List<BookingDTO>> response = adminBookingsController.getReservasDeMisHoteles();
        assertEquals(200, response.getStatusCode().value());
    }
}
