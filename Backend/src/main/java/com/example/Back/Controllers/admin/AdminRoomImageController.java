package com.example.back.controllers.admin;

import com.example.back.dto.room.RoomImageDTO;
import com.example.back.services.interfaces.RoomImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hoteles/{hotelId}/habitaciones/{habitacionId}/imagenes")
public class AdminRoomImageController {

    @Autowired
    private RoomImageService imagenService;

    @GetMapping
    public ResponseEntity<List<RoomImageDTO>> getImagenes(
            @PathVariable Integer hotelId,
            @PathVariable Integer habitacionId) {
        return ResponseEntity.ok(imagenService.getImagenesByHabitacionId(hotelId, habitacionId));
    }

    @PostMapping
    public ResponseEntity<RoomImageDTO> createImagen(
            @PathVariable Integer hotelId,
            @PathVariable Integer habitacionId,
            @RequestBody RoomImageDTO imagenDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(imagenService.createImagen(hotelId, habitacionId, imagenDTO));
    }

    @DeleteMapping("/{imagenId}")
    public ResponseEntity<Void> deleteImagen(
            @PathVariable Integer hotelId,
            @PathVariable Integer habitacionId,
            @PathVariable Integer imagenId) {
        if (imagenService.deleteImagen(hotelId, habitacionId, imagenId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
