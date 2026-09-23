package com.example.back.controllers.superadmin;

import com.example.back.dto.booking.BookingDTO;
import com.example.back.models.booking.Booking;
import com.example.back.models.payment.Payment;
import com.example.back.services.interfaces.BookingService;
import com.example.back.services.interfaces.PaymentService;
import com.example.back.services.interfaces.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/superadmin/reservas")
public class SuperAdminBookingsController {

    @Autowired
    private SuperAdminService superAdminService;

    @Autowired
    private BookingService reservaService;

    @Autowired
    private PaymentService pagoService;

    @GetMapping
    public ResponseEntity<List<BookingDTO>> listarReservas() {
        return ResponseEntity.ok(superAdminService.listarReservas());
    }

    @GetMapping("/hotel/{idHotel}")
    public ResponseEntity<List<BookingDTO>> listarReservasPorHotel(@PathVariable Integer idHotel) {
        return ResponseEntity.ok(superAdminService.listarReservasPorHotel(idHotel));
    }

    @GetMapping("/{idReserva}")
    public ResponseEntity<BookingDTO> obtenerReserva(@PathVariable Integer idReserva) {
        BookingDTO dto = superAdminService.obtenerReservaPorId(idReserva);
        if (dto != null) return ResponseEntity.ok(dto);
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{idReserva}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> descargarPdf(@PathVariable Integer idReserva) {
        try {
            byte[] pdfBytes = pagoService.generarComprobantePorReserva(idReserva);
            if (pdfBytes == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "superadmin_reserva_" + idReserva + ".pdf");
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
