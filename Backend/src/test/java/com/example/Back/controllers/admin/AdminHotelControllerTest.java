package com.example.back.controllers.admin;

import com.example.back.dto.hotel.HotelDTO;
import com.example.back.models.hotel.Hotel;
import com.example.back.services.interfaces.HotelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminHotelControllerTest {

    @Mock
    private HotelService hotelService;

    @InjectMocks
    private AdminHotelController adminHotelController;

    @Test
    void testGetMisHoteles() {
        when(hotelService.getHotelesByCurrentUser()).thenReturn(List.of(new HotelDTO()));
        ResponseEntity<List<HotelDTO>> response = adminHotelController.getMisHoteles();
        assertEquals(200, response.getStatusCode().value());
        assertFalse(response.getBody().isEmpty());
    }
}
