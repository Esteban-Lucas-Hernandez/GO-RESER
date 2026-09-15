package com.example.back.services;

import com.example.back.dto.payment.PaymentDetailDTO;
import com.example.back.models.booking.Booking;
import com.example.back.models.payment.Payment;
import com.example.back.repo.booking.BookingRepository;
import com.example.back.repo.payment.PaymentRepository;
import com.example.back.services.impl.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void testConfirmarPago() {
        Booking booking = new Booking();
        booking.setIdReserva(1);
        booking.setTotal(200000.0);
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> inv.getArgument(0));

        PaymentDetailDTO result = paymentService.confirmarPago(1);
        assertNotNull(result);
    }
}
