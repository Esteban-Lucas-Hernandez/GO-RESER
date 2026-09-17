package com.example.back.services.impl;

import com.example.back.models.room.Room;
import com.example.back.repo.room.RoomAvailabilityRepository;
import com.example.back.services.interfaces.RoomAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomAvailabilityServiceImpl implements RoomAvailabilityService {

    @Autowired
    private RoomAvailabilityRepository roomAvailabilityRepository;

    @Override
    public Room getCheapestRoom() {
        List<Room> cheapest = roomAvailabilityRepository.findCheapestRooms();
        return cheapest.isEmpty() ? null : cheapest.get(0);
    }

    @Override
    public List<Room> getAvailableRooms() {
        return roomAvailabilityRepository.findCheapestRooms();
    }
}
