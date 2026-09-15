package com.example.back.services;

import com.example.back.dto.hotel.HotelDTO;
import com.example.back.mapper.hotel.HotelMapper;
import com.example.back.models.hotel.Hotel;
import com.example.back.models.user.User;
import com.example.back.repo.hotel.CityRepository;
import com.example.back.repo.hotel.HotelRepository;
import com.example.back.services.impl.HotelServiceImpl;
import com.example.back.services.interfaces.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelMapper hotelMapper;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @Test
    void testGetAllHoteles() {
        when(hotelRepository.findAll()).thenReturn(List.of(new Hotel()));
        List<Hotel> list = hotelService.getAllHoteles();
        assertEquals(1, list.size());
    }
}
