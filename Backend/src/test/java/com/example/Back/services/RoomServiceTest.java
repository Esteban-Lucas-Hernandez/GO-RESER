package com.example.back.services;

import com.example.back.dto.room.RoomDTO;
import com.example.back.mapper.room.RoomMapper;
import com.example.back.models.room.Room;
import com.example.back.repo.room.RoomRepository;
import com.example.back.services.impl.RoomServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomServiceImpl roomService;

    @Test
    void testGetHabitacionesByHotelId() {
        when(roomRepository.findByHotelId(1)).thenReturn(List.of(new Room()));
        when(roomMapper.habitacionToHabitacionDTO(any())).thenReturn(new RoomDTO());

        List<RoomDTO> list = roomService.getHabitacionesByHotelId(1);
        assertEquals(1, list.size());
    }
}
