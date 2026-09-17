package com.example.back.repo;

import com.example.back.models.booking.Booking;
import com.example.back.repo.booking.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingRepositoryTest {

    @Mock
    private BookingRepository bookingRepository;

    @Test
    void testFindByUsuarioIdUsuario() {
        when(bookingRepository.findByUsuarioIdUsuario(1)).thenReturn(List.of(new Booking()));
        assertEquals(1, bookingRepository.findByUsuarioIdUsuario(1).size());
    }
}
