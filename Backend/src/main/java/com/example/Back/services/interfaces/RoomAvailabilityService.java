package com.example.back.services.interfaces;

import com.example.back.models.room.Room;
import java.util.List;

public interface RoomAvailabilityService {
    Room getCheapestRoom();
    List<Room> getAvailableRooms();
}
