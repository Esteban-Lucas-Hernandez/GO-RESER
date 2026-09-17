package com.example.back.controllers.admin;

import com.example.back.dto.review.ReviewDTO;
import com.example.back.services.interfaces.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/resenas")
public class AdminReviewsController {

    @Autowired
    private ReviewService resenaService;

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getResenasDeMisHoteles() {
        return ResponseEntity.ok(resenaService.getResenasDeMisHoteles());
    }

    @GetMapping("/hotel/{idHotel}")
    public ResponseEntity<List<ReviewDTO>> getResenasPorHotel(@PathVariable Integer idHotel) {
        return ResponseEntity.ok(resenaService.getResenasPorHotelDeUsuario(idHotel));
    }
}
