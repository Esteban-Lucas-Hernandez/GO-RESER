package com.example.back.repo;

import com.example.back.models.hotel.Hotel;
import com.example.back.repo.hotel.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HotelRepositoryTest {

    @Mock
    private HotelRepository hotelRepository;

    @Test
    void testFindAll() {
        when(hotelRepository.findAll()).thenReturn(List.of(new Hotel()));
        assertFalse(hotelRepository.findAll().isEmpty());
    }
}
