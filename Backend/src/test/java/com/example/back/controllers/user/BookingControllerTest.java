package com.example.back.controllers.user;

import com.example.back.dto.booking.BookingDTO;
import com.example.back.dto.booking.CreateBookingDTO;
import com.example.back.services.interfaces.BookingService;
import com.example.back.services.interfaces.PaymentService;
import com.example.back.services.interfaces.SecurityService;
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

    @Mock
    private SecurityService securityService;

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

    @Test
    void testDescargarComprobante_Exitoso() throws Exception {
        com.example.back.models.user.User user = new com.example.back.models.user.User();
        user.setIdUsuario(1);
        user.setRoles(java.util.Collections.emptySet());

        com.example.back.models.booking.Booking booking = new com.example.back.models.booking.Booking();
        booking.setIdReserva(1);
        booking.setUsuario(user);

        when(securityService.getAuthenticatedUser()).thenReturn(user);
        when(bookingService.getReservaPorId(1)).thenReturn(booking);
        byte[] dummyPdf = new byte[]{1, 2, 3};
        when(paymentService.generarComprobantePorReserva(1)).thenReturn(dummyPdf);

        ResponseEntity<byte[]> response = bookingController.descargarComprobante(1);
        assertEquals(200, response.getStatusCode().value());
        assertArrayEquals(dummyPdf, response.getBody());
        assertTrue(response.getHeaders().getContentType().includes(org.springframework.http.MediaType.APPLICATION_PDF));
    }

    @Test
    void testDescargarComprobante_ReservaNoExiste() {
        when(bookingService.getReservaPorId(999)).thenThrow(new RuntimeException("Reserva no encontrada"));

        ResponseEntity<byte[]> response = bookingController.descargarComprobante(999);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testDescargarComprobante_UsuarioDiferente_Forbidden() {
        com.example.back.models.user.User userLogueado = new com.example.back.models.user.User();
        userLogueado.setIdUsuario(1);
        userLogueado.setRoles(java.util.Collections.emptySet());

        com.example.back.models.user.User userDueno = new com.example.back.models.user.User();
        userDueno.setIdUsuario(2);

        com.example.back.models.booking.Booking booking = new com.example.back.models.booking.Booking();
        booking.setIdReserva(5);
        booking.setUsuario(userDueno);

        when(securityService.getAuthenticatedUser()).thenReturn(userLogueado);
        when(bookingService.getReservaPorId(5)).thenReturn(booking);

        ResponseEntity<byte[]> response = bookingController.descargarComprobante(5);
        assertEquals(403, response.getStatusCode().value());
    }
}
