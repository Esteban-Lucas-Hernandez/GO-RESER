package com.example.back.controllers.user;

import com.example.back.dto.payment.PaymentDetailDTO;
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

@RestController
@RequestMapping("/user/pagos")
public class PaymentController {

    @Autowired
    private PaymentService pagoService;

    @Autowired
    private BookingService reservaService;

    @PostMapping("/confirmar/{idReserva}")
    public ResponseEntity<PaymentDetailDTO> confirmarPago(@PathVariable Integer idReserva) {
        return ResponseEntity.ok(pagoService.confirmarPago(idReserva));
    }

    @PostMapping("/confirmar/{idReserva}/pdf")
    public ResponseEntity<byte[]> confirmarPagoYGenerarPdf(@PathVariable Integer idReserva) {
        try {
            Booking reserva = reservaService.getReservaPorId(idReserva);
            Payment pago;
            if (reserva.getEstado() == Booking.EstadoReserva.confirmada && reserva.getPagos() != null && !reserva.getPagos().isEmpty()) {
                pago = reserva.getPagos().get(reserva.getPagos().size() - 1);
            } else {
                pagoService.confirmarPago(idReserva);
                reserva = reservaService.getReservaPorId(idReserva);
                pago = reserva.getPagos().get(reserva.getPagos().size() - 1);
            }
            byte[] pdfBytes = pagoService.generarComprobantePdf(pago);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "comprobante_pago_" + idReserva + ".pdf");
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
