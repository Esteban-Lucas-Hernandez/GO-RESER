package com.example.back.services.interfaces;

import com.example.back.dto.payment.PaymentDetailDTO;
import com.example.back.models.payment.Payment;

public interface PaymentService {
    PaymentDetailDTO confirmarPago(Integer idReserva);
    byte[] generarComprobantePdf(Payment pago) throws Exception;
    byte[] generarComprobantePorReserva(Integer idReserva) throws Exception;
}
