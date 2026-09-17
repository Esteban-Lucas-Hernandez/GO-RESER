package com.example.back.controllers.admin;

import com.example.back.dto.booking.BookingDTO;
import com.example.back.models.booking.Booking;
import com.example.back.models.payment.Payment;
import com.example.back.services.interfaces.BookingService;
import com.example.back.services.interfaces.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/reservas")
public class AdminBookingsController {

    @Autowired
    private BookingService reservaService;

    @Autowired
    private PaymentService pagoService;

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getReservasDeMisHoteles() {
        return ResponseEntity.ok(reservaService.getReservasDeMisHoteles());
    }

    @GetMapping("/hotel/{idHotel}")
    public ResponseEntity<List<BookingDTO>> getReservasPorHotel(@PathVariable Integer idHotel) {
        return ResponseEntity.ok(reservaService.getReservasPorHotelDeUsuario(idHotel));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> eliminarReservasAntiguas() {
        int eliminadas = reservaService.eliminarReservasAntiguasYCanceladas();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Reservas antiguas eliminadas");
        response.put("totalEliminadas", eliminadas);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{idReserva}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> descargarPdf(@PathVariable Integer idReserva) {
        try {
            Booking reserva = reservaService.getReservaPorId(idReserva);
            if (reserva.getPagos() == null || reserva.getPagos().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            Payment pago = reserva.getPagos().get(0);
            byte[] pdfBytes = pagoService.generarComprobantePdf(pago);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reserva_" + idReserva + ".pdf");
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
