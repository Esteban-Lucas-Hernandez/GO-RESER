package com.example.back.mapper;

import com.example.back.dto.booking.BookingDTO;
import com.example.back.mapper.booking.BookingMapper;
import com.example.back.models.booking.Booking;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookingMapperTest {

    @Test
    void testBookingToDTO() {
        Booking booking = new Booking();
        booking.setIdReserva(5);

        BookingDTO dto = BookingMapper.INSTANCE.reservaToReservaDTO(booking);
        assertNotNull(dto);
        assertEquals(5, dto.getIdReserva());
    }
}
