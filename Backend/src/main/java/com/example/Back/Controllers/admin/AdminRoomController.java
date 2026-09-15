package com.example.back.controllers.admin;

import com.example.back.dto.room.CreateRoomDTO;
import com.example.back.dto.room.RoomDTO;
import com.example.back.services.interfaces.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hoteles/{hotelId}/habitaciones")
public class AdminRoomController {

    @Autowired
    private RoomService habitacionService;

    @GetMapping
    public ResponseEntity<List<RoomDTO>> getHabitaciones(@PathVariable Integer hotelId) {
        return ResponseEntity.ok(habitacionService.getHabitacionesByHotelId(hotelId));
    }

    @GetMapping("/{habitacionId}")
    public ResponseEntity<RoomDTO> getHabitacion(
            @PathVariable Integer hotelId,
            @PathVariable Integer habitacionId) {
        return habitacionService.getHabitacionByIdAndHotelId(habitacionId, hotelId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RoomDTO> createHabitacion(
            @PathVariable Integer hotelId,
            @RequestBody CreateRoomDTO crearHabitacionDTO) {
        return habitacionService.createHabitacionDesdeDTO(hotelId, crearHabitacionDTO)
                .map(h -> ResponseEntity.status(HttpStatus.CREATED).body(h))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{habitacionId}")
    public ResponseEntity<RoomDTO> updateHabitacion(
            @PathVariable Integer hotelId,
            @PathVariable Integer habitacionId,
            @RequestBody RoomDTO habitacionDTO) {
        return habitacionService.updateHabitacion(habitacionId, hotelId, habitacionDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{habitacionId}")
    public ResponseEntity<Void> deleteHabitacion(
            @PathVariable Integer hotelId,
            @PathVariable Integer habitacionId) {
        if (habitacionService.deleteHabitacion(habitacionId, hotelId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
