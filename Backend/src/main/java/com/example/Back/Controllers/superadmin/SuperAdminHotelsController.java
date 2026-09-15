package com.example.back.controllers.superadmin;

import com.example.back.dto.hotel.HotelDTO;
import com.example.back.services.interfaces.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/superadmin/hoteles")
public class SuperAdminHotelsController {

    @Autowired
    private SuperAdminService superAdminService;

    @GetMapping
    public ResponseEntity<List<HotelDTO>> listarHoteles() {
        return ResponseEntity.ok(superAdminService.listarHoteles());
    }
}
