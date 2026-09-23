package com.example.back.services.impl;

import com.example.back.dto.payment.PaymentDetailDTO;
import com.example.back.mapper.payment.PaymentMapper;
import com.example.back.models.booking.Booking;
import com.example.back.models.hotel.Hotel;
import com.example.back.models.payment.Payment;
import com.example.back.models.room.Room;
import com.example.back.models.user.User;
import com.example.back.repo.booking.BookingRepository;
import com.example.back.repo.payment.PaymentRepository;
import com.example.back.services.interfaces.PaymentService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository pagoRepository;

    @Autowired
    private BookingRepository reservaRepository;

    private final PaymentMapper pagoMapper = PaymentMapper.INSTANCE;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    @Transactional
    public PaymentDetailDTO confirmarPago(Integer idReserva) {
        Booking reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + idReserva));

        Payment pago = new Payment();
        pago.setReserva(reserva);
        pago.setMonto(reserva.getTotal() != null ? reserva.getTotal() : 0.0);
        pago.setFechaPago(LocalDateTime.now());
        pago.setReferenciaPago("PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        try {
            pago.setMetodo(Payment.MetodoPago.valueOf(reserva.getMetodoPago() != null ? reserva.getMetodoPago().name() : "tarjeta"));
        } catch (Exception e) {
            pago.setMetodo(Payment.MetodoPago.tarjeta);
        }

        Payment guardado = pagoRepository.save(pago);
        reserva.setEstado(Booking.EstadoReserva.confirmada);
        reservaRepository.save(reserva);

        return pagoMapper.pagoToPagoDetalladoDTO(guardado);
    }

    @Override
    @Transactional
    public byte[] generarComprobantePorReserva(Integer idReserva) throws Exception {
        Booking reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + idReserva));

        List<Payment> pagos = pagoRepository.findByReservaIdReserva(idReserva);
        Payment pago;
        if (pagos != null && !pagos.isEmpty()) {
            pago = pagos.get(pagos.size() - 1);
        } else {
            // Si la reserva no tiene pago registrado, crear el registro de pago automáticamente
            pago = new Payment();
            pago.setReserva(reserva);
            pago.setMonto(reserva.getTotal() != null ? reserva.getTotal() : 0.0);
            pago.setFechaPago(reserva.getFechaReserva() != null ? reserva.getFechaReserva() : LocalDateTime.now());
            pago.setReferenciaPago("PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            try {
                pago.setMetodo(Payment.MetodoPago.valueOf(reserva.getMetodoPago() != null ? reserva.getMetodoPago().name() : "tarjeta"));
            } catch (Exception e) {
                pago.setMetodo(Payment.MetodoPago.tarjeta);
            }
            pago = pagoRepository.save(pago);

            if (reserva.getEstado() == Booking.EstadoReserva.pendiente) {
                reserva.setEstado(Booking.EstadoReserva.confirmada);
                reservaRepository.save(reserva);
            }
        }

        return generarComprobantePdf(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generarComprobantePdf(Payment pago) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 40, 40);
        PdfWriter.getInstance(document, out);
        document.open();

        // Colores de diseño
        Color primaryColor = new Color(26, 86, 219);     // Azul elegante #1A56DB
        Color secondaryColor = new Color(31, 41, 55);    // Gris oscuro #1F2937
        Color lightGray = new Color(243, 244, 246);      // Gris claro fondo #F3F4F6
        Color borderColor = new Color(229, 231, 235);    // Borde suave #E5E7EB
        Color successColor = new Color(16, 185, 129);    // Verde éxito #10B981

        // Fuentes
        Font fontBrand = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, primaryColor);
        Font fontSubtitle = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);
        Font fontSectionHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, primaryColor);
        Font fontLabel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, secondaryColor);
        Font fontValue = FontFactory.getFont(FontFactory.HELVETICA, 10, secondaryColor);
        Font fontStatus = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, successColor);
        Font fontTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, primaryColor);

        // Formato moneda
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("es", "CO"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat currencyFormat = new DecimalFormat("$ #,##0.00 COP", symbols);

        // 1. ENCABEZADO
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{60, 40});

        PdfPCell leftHeader = new PdfPCell();
        leftHeader.setBorder(Rectangle.NO_BORDER);
        leftHeader.addElement(new Paragraph("GoReser", fontBrand));
        leftHeader.addElement(new Paragraph("Plataforma Oficial de Reservas Hoteleras", fontSubtitle));
        headerTable.addCell(leftHeader);

        PdfPCell rightHeader = new PdfPCell();
        rightHeader.setBorder(Rectangle.NO_BORDER);
        rightHeader.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Paragraph pComp = new Paragraph("COMPROBANTE DE PAGO", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, secondaryColor));
        pComp.setAlignment(Element.ALIGN_RIGHT);
        rightHeader.addElement(pComp);
        Paragraph pRef = new Paragraph("Ref: " + (pago.getReferenciaPago() != null ? pago.getReferenciaPago() : "N/A"), fontLabel);
        pRef.setAlignment(Element.ALIGN_RIGHT);
        rightHeader.addElement(pRef);
        headerTable.addCell(rightHeader);

        document.add(headerTable);
        document.add(new Paragraph(" "));

        // 2. DETALLES DEL PAGO Y RESERVA
        Booking reserva = pago.getReserva();
        User usuario = reserva != null ? reserva.getUsuario() : null;
        Room habitacion = reserva != null ? reserva.getHabitacion() : null;
        Hotel hotel = habitacion != null ? habitacion.getHotel() : null;

        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setWidths(new float[]{50, 50});
        infoTable.setSpacingAfter(15);

        // Bloque Izquierdo: Información del Huésped
        PdfPCell guestCell = new PdfPCell();
        guestCell.setBackgroundColor(lightGray);
        guestCell.setBorderColor(borderColor);
        guestCell.setPadding(10);

        guestCell.addElement(new Paragraph("INFORMACIÓN DEL HUÉSPED", fontSectionHeader));
        guestCell.addElement(new Paragraph("Nombre: " + (usuario != null && usuario.getNombreCompleto() != null ? usuario.getNombreCompleto() : "Cliente General"), fontValue));
        guestCell.addElement(new Paragraph("Email: " + (usuario != null && usuario.getEmail() != null ? usuario.getEmail() : "N/A"), fontValue));
        guestCell.addElement(new Paragraph("Teléfono: " + (usuario != null && usuario.getTelefono() != null ? usuario.getTelefono() : "No registrado"), fontValue));
        if (usuario != null && usuario.getDocumento() != null && !usuario.getDocumento().isEmpty()) {
            guestCell.addElement(new Paragraph("Documento: " + usuario.getDocumento(), fontValue));
        }
        infoTable.addCell(guestCell);

        // Bloque Derecho: Información de la Transacción
        PdfPCell transCell = new PdfPCell();
        transCell.setBackgroundColor(lightGray);
        transCell.setBorderColor(borderColor);
        transCell.setPadding(10);

        transCell.addElement(new Paragraph("DETALLES DE LA TRANSACCIÓN", fontSectionHeader));
        String fechaPagoStr = pago.getFechaPago() != null ? pago.getFechaPago().format(DATE_TIME_FORMATTER) : LocalDateTime.now().format(DATE_TIME_FORMATTER);
        transCell.addElement(new Paragraph("Fecha de Pago: " + fechaPagoStr, fontValue));
        transCell.addElement(new Paragraph("Método de Pago: " + (pago.getMetodo() != null ? pago.getMetodo().name().toUpperCase() : "TARJETA"), fontValue));
        transCell.addElement(new Paragraph("Estado del Pago: CONFIRMADO", fontStatus));
        if (reserva != null && reserva.getIdReserva() != null) {
            transCell.addElement(new Paragraph("Número de Reserva: #" + reserva.getIdReserva(), fontLabel));
        }
        infoTable.addCell(transCell);

        document.add(infoTable);

        // 3. DETALLES DE LA ESTANCIA Y HABITACIÓN
        PdfPTable stayTable = new PdfPTable(2);
        stayTable.setWidthPercentage(100);
        stayTable.setWidths(new float[]{50, 50});
        stayTable.setSpacingAfter(15);

        // Datos del Hotel
        PdfPCell hotelCell = new PdfPCell();
        hotelCell.setBorderColor(borderColor);
        hotelCell.setPadding(10);
        hotelCell.addElement(new Paragraph("DATOS DEL HOTEL", fontSectionHeader));
        hotelCell.addElement(new Paragraph("Hotel: " + (hotel != null && hotel.getNombre() != null ? hotel.getNombre() : "GoReser Hotel"), fontLabel));
        hotelCell.addElement(new Paragraph("Dirección: " + (hotel != null && hotel.getDireccion() != null ? hotel.getDireccion() : "Dirección central"), fontValue));
        String ciudadStr = (hotel != null && hotel.getCiudad() != null) ? hotel.getCiudad().getNombre() : "Colombia";
        hotelCell.addElement(new Paragraph("Ciudad: " + ciudadStr, fontValue));
        if (hotel != null && hotel.getTelefono() != null) {
            hotelCell.addElement(new Paragraph("Tel. Hotel: " + hotel.getTelefono(), fontValue));
        }
        stayTable.addCell(hotelCell);

        // Datos de la Habitación y Fechas
        PdfPCell roomCell = new PdfPCell();
        roomCell.setBorderColor(borderColor);
        roomCell.setPadding(10);
        roomCell.addElement(new Paragraph("HABITACIÓN Y ESTANCIA", fontSectionHeader));
        String numHab = (habitacion != null && habitacion.getNumero() != null) ? habitacion.getNumero() : "Estándar";
        String catHab = (habitacion != null && habitacion.getCategoria() != null) ? habitacion.getCategoria().getNombre() : "";
        roomCell.addElement(new Paragraph("Habitación: " + numHab + (!catHab.isEmpty() ? " (" + catHab + ")" : ""), fontValue));

        if (reserva != null && reserva.getFechaInicio() != null && reserva.getFechaFin() != null) {
            roomCell.addElement(new Paragraph("Check-in: " + reserva.getFechaInicio().format(DATE_FORMATTER), fontValue));
            roomCell.addElement(new Paragraph("Check-out: " + reserva.getFechaFin().format(DATE_FORMATTER), fontValue));
            long noches = ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin());
            if (noches <= 0) noches = 1;
            roomCell.addElement(new Paragraph("Duración: " + noches + (noches == 1 ? " noche" : " noches"), fontValue));
        }
        stayTable.addCell(roomCell);

        document.add(stayTable);

        // 4. RESUMEN DE TOTALES
        PdfPTable totalTable = new PdfPTable(2);
        totalTable.setWidthPercentage(100);
        totalTable.setWidths(new float[]{60, 40});

        PdfPCell noteCell = new PdfPCell();
        noteCell.setBorder(Rectangle.NO_BORDER);
        Paragraph noteText = new Paragraph("Este documento es un comprobante de pago válido emitido electrónicamente por GoReser. Por favor preséntelo en la recepción del hotel al momento de realizar su registro (Check-in).", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, Color.GRAY));
        noteCell.addElement(noteText);
        totalTable.addCell(noteCell);

        PdfPCell amountCell = new PdfPCell();
        amountCell.setBackgroundColor(lightGray);
        amountCell.setBorderColor(primaryColor);
        amountCell.setBorderWidth(1.5f);
        amountCell.setPadding(12);
        amountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        Paragraph labelTotal = new Paragraph("TOTAL PAGADO", fontLabel);
        labelTotal.setAlignment(Element.ALIGN_RIGHT);
        amountCell.addElement(labelTotal);

        double montoTotal = pago.getMonto() != null ? pago.getMonto() : (reserva != null && reserva.getTotal() != null ? reserva.getTotal() : 0.0);
        Paragraph valueTotal = new Paragraph(currencyFormat.format(montoTotal), fontTotal);
        valueTotal.setAlignment(Element.ALIGN_RIGHT);
        amountCell.addElement(valueTotal);

        totalTable.addCell(amountCell);
        document.add(totalTable);

        // 5. PIE DE PÁGINA
        document.add(new Paragraph(" "));
        Paragraph footer = new Paragraph("© 2026 GoReser - Todos los derechos reservados. Soporte: soporte@goreser.com", FontFactory.getFont(FontFactory.HELVETICA, 8, Color.GRAY));
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
        return out.toByteArray();
    }
}
