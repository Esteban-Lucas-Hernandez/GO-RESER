package com.example.back.controllers.user;

import com.example.back.dto.review.ReviewDTO;
import com.example.back.services.interfaces.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/resenas")
public class ReviewController {

    @Autowired
    private ReviewService resenaService;

    @PostMapping("/hotel/{hotelId}")
    public ResponseEntity<?> crearResena(
            @PathVariable Integer hotelId,
            @RequestBody ReviewDTO resenaDTO) {
        try {
            ReviewDTO nuevaResena = resenaService.crearResena(resenaDTO, hotelId);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaResena);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarResena(
            @PathVariable Integer id,
            @RequestBody ReviewDTO resenaDTO) {
        try {
            ReviewDTO resenaActualizada = resenaService.actualizarResena(id, resenaDTO);
            return ResponseEntity.ok(resenaActualizada);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarResena(@PathVariable Integer id) {
        try {
            resenaService.eliminarResena(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Reseña eliminada correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
