package com.example.back.controllers.admin;

import com.example.back.dto.room.RoomDTO;
import com.example.back.services.interfaces.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/hoteles/habitaciones")
public class AdminRoomInventoryController {

    @Autowired
    private RoomService habitacionService;

    @GetMapping
    public ResponseEntity<List<RoomDTO>> getHabitacionesDeMisHoteles() {
        return ResponseEntity.ok(habitacionService.getHabitacionesDeMisHoteles());
    }
}
