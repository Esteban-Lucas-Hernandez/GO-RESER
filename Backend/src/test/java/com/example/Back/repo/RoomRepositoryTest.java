package com.example.back.repo;

import com.example.back.models.room.Room;
import com.example.back.repo.room.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomRepositoryTest {

    @Mock
    private RoomRepository roomRepository;

    @Test
    void testFindByHotelId() {
        when(roomRepository.findByHotelId(1)).thenReturn(List.of(new Room()));
        assertEquals(1, roomRepository.findByHotelId(1).size());
    }
}
