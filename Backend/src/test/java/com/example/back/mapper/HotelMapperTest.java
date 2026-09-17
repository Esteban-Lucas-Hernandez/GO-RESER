package com.example.back.mapper;

import com.example.back.dto.hotel.HotelDTO;
import com.example.back.mapper.hotel.HotelMapper;
import com.example.back.models.hotel.Hotel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HotelMapperTest {

    private final HotelMapper mapper = new HotelMapper();

    @Test
    void testHotelToDTO() {
        Hotel hotel = new Hotel();
        hotel.setId(1);
        hotel.setNombre("Grand Hotel");

        HotelDTO dto = mapper.hotelToHotelDTO(hotel);
        assertNotNull(dto);
        assertEquals("Grand Hotel", dto.getNombre());
    }
}
