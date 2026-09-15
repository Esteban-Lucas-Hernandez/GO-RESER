package com.example.back.services.impl;

import com.example.back.dto.payment.PaymentDetailDTO;
import com.example.back.mapper.payment.PaymentMapper;
import com.example.back.models.booking.Booking;
import com.example.back.models.payment.Payment;
import com.example.back.repo.booking.BookingRepository;
import com.example.back.repo.payment.PaymentRepository;
import com.example.back.services.interfaces.PaymentService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository pagoRepository;

    @Autowired
    private BookingRepository reservaRepository;

    private final PaymentMapper pagoMapper = PaymentMapper.INSTANCE;

    @Override
    public PaymentDetailDTO confirmarPago(Integer idReserva) {
        Booking reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        Payment pago = new Payment();
        pago.setReserva(reserva);
        pago.setMonto(reserva.getTotal());
        pago.setFechaPago(LocalDateTime.now());
        pago.setReferenciaPago("PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        try {
            pago.setMetodo(Payment.MetodoPago.valueOf(reserva.getMetodoPago().name()));
        } catch (Exception e) {
            pago.setMetodo(Payment.MetodoPago.tarjeta);
        }

        Payment guardado = pagoRepository.save(pago);
        reserva.setEstado(Booking.EstadoReserva.confirmada);
        reservaRepository.save(reserva);

        return pagoMapper.pagoToPagoDetalladoDTO(guardado);
    }

    @Override
    public byte[] generarComprobantePdf(Payment pago) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);
        document.open();

        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12);

        document.add(new Paragraph("Comprobante de Pago - GoReser", fontTitulo));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Referencia: " + pago.getReferenciaPago(), fontNormal));
        document.add(new Paragraph("Fecha: " + pago.getFechaPago(), fontNormal));
        document.add(new Paragraph("Monto: $" + pago.getMonto(), fontNormal));
        document.add(new Paragraph("Método: " + pago.getMetodo(), fontNormal));
        if (pago.getReserva() != null) {
            document.add(new Paragraph("Reserva ID: " + pago.getReserva().getIdReserva(), fontNormal));
            if (pago.getReserva().getHabitacion() != null && pago.getReserva().getHabitacion().getHotel() != null) {
                document.add(new Paragraph("Hotel: " + pago.getReserva().getHabitacion().getHotel().getNombre(), fontNormal));
            }
        }

        document.close();
        return out.toByteArray();
    }
}
