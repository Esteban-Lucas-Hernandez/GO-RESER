package com.example.back.controllers.user;

import com.example.back.dto.booking.BookingDTO;
import com.example.back.dto.booking.CreateBookingDTO;
import com.example.back.models.booking.Booking;
import com.example.back.models.payment.Payment;
import com.example.back.models.user.User;
import com.example.back.services.interfaces.BookingService;
import com.example.back.services.interfaces.PaymentService;
import com.example.back.services.interfaces.SecurityService;
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
@RequestMapping("/user/reservas")
public class BookingController {

    @Autowired
    private BookingService reservaService;

    @Autowired
    private PaymentService pagoService;

    @Autowired
    private SecurityService securityService;

    @GetMapping("/habitacion/{idHabitacion}/fechas-reservadas")
    public ResponseEntity<List<Object[]>> getFechasReservadas(@PathVariable Integer idHabitacion) {
        return ResponseEntity.ok(reservaService.getFechasReservadasConfirmadas(idHabitacion));
    }

    @PostMapping("/habitacion/{idHabitacion}")
    public ResponseEntity<?> crearReserva(
            @PathVariable Integer idHabitacion,
            @RequestBody CreateBookingDTO reservaDTO) {
        try {
            BookingDTO reserva = reservaService.crearReserva(idHabitacion, reservaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(reserva);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getMisReservas() {
        return ResponseEntity.ok(reservaService.getReservasPorUsuario());
    }

    @GetMapping("/habitacion/{idHabitacion}")
    public ResponseEntity<List<BookingDTO>> getReservasPorHabitacion(@PathVariable Integer idHabitacion) {
        return ResponseEntity.ok(reservaService.getReservasPorHabitacion(idHabitacion));
    }

    @PutMapping("/{idReserva}/cancelar")
    public ResponseEntity<BookingDTO> cancelarReserva(@PathVariable Integer idReserva) {
        return ResponseEntity.ok(reservaService.cancelarReserva(idReserva));
    }

    @PutMapping("/{idReserva}/confirmar")
    public ResponseEntity<BookingDTO> confirmarReserva(@PathVariable Integer idReserva) {
        return ResponseEntity.ok(reservaService.confirmarReserva(idReserva));
    }

    @GetMapping("/{idReserva}/comprobante")
    public ResponseEntity<byte[]> descargarComprobante(@PathVariable Integer idReserva) {
        try {
            User usuarioActual = securityService.getAuthenticatedUser();
            Booking reserva = reservaService.getReservaPorId(idReserva);

            boolean esAdmin = usuarioActual != null && usuarioActual.getRoles() != null && usuarioActual.getRoles().stream()
                    .anyMatch(r -> "ROLE_ADMIN".equals(r.getName()) || "ROLE_SUPERADMIN".equals(r.getName()));

            if (usuarioActual != null && !esAdmin && reserva.getUsuario() != null &&
                    !reserva.getUsuario().getIdUsuario().equals(usuarioActual.getIdUsuario())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            byte[] pdfBytes = pagoService.generarComprobantePorReserva(idReserva);
            if (pdfBytes == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "comprobante_reserva_" + idReserva + ".pdf");
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
