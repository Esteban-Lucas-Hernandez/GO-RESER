package com.example.back.controllers.publicapi;

import com.example.back.dto.review.ReviewDTO;
import com.example.back.services.interfaces.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/resenas")
public class ReviewPublicController {

    @Autowired
    private ReviewService resenaService;

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<ReviewDTO>> obtenerResenasPorHotel(@PathVariable Integer hotelId) {
        List<ReviewDTO> resenas = resenaService.obtenerResenasPorHotel(hotelId);
        return ResponseEntity.ok(resenas);
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> obtenerTodasLasResenas() {
        List<ReviewDTO> resenas = resenaService.obtenerTodasLasResenas();
        return ResponseEntity.ok(resenas);
    }
}
