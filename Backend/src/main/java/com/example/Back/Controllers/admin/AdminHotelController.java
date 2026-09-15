package com.example.back.controllers.admin;

import com.example.back.dto.hotel.HotelDTO;
import com.example.back.models.hotel.Hotel;
import com.example.back.services.interfaces.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/hoteles")
public class AdminHotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<HotelDTO>> getMisHoteles() {
        return ResponseEntity.ok(hotelService.getHotelesByCurrentUser());
    }

    @PostMapping
    public ResponseEntity<Hotel> createHotel(@RequestBody HotelDTO hotelDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.createHotel(hotelDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Integer id, @RequestBody HotelDTO hotelDTO) {
        return ResponseEntity.ok(hotelService.updateHotel(id, hotelDTO));
    }

    @GetMapping("/{id}/has-habitaciones")
    public ResponseEntity<Map<String, Boolean>> hasHabitaciones(@PathVariable Integer id) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("hasHabitaciones", hotelService.hasHabitaciones(id));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/cascade")
    public ResponseEntity<Void> deleteHotelCascade(@PathVariable Integer id) {
        if (hotelService.deleteHotelWithCascade(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
