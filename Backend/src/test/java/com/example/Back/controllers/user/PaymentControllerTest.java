package com.example.back.controllers.user;

import com.example.back.dto.payment.PaymentDetailDTO;
import com.example.back.services.interfaces.BookingService;
import com.example.back.services.interfaces.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    void testConfirmarPago() {
        PaymentDetailDTO dto = new PaymentDetailDTO();
        dto.setIdPago(1);
        when(paymentService.confirmarPago(1)).thenReturn(dto);

        ResponseEntity<PaymentDetailDTO> response = paymentController.confirmarPago(1);
        assertEquals(200, response.getStatusCode().value());
    }
}
