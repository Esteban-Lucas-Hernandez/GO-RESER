package com.example.back.controllers.superadmin;

import com.example.back.dto.room.RoomDTO;
import com.example.back.services.interfaces.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/superadmin/habitaciones")
public class SuperAdminRoomsController {

    @Autowired
    private SuperAdminService superAdminService;

    @GetMapping
    public ResponseEntity<List<RoomDTO>> listarHabitaciones() {
        return ResponseEntity.ok(superAdminService.listarHabitaciones());
    }

    @GetMapping("/hotel/{idHotel}")
    public ResponseEntity<List<RoomDTO>> listarHabitacionesPorHotel(@PathVariable Integer idHotel) {
        return ResponseEntity.ok(superAdminService.listarHabitacionesPorHotel(idHotel));
    }
}
