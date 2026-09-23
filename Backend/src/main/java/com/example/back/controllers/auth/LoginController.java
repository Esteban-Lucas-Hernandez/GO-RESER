package com.example.back.controllers.auth;

import com.example.back.dto.auth.LoginRequestDTO;
import com.example.back.dto.auth.LoginResponseDTO;
import com.example.back.services.interfaces.LoginService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        logger.info("📩 [CONTROLLER /auth/login] Petición de login recibida para email: {}", request.getEmail());
        LoginResponseDTO response = loginService.login(request.getEmail(), request.getPassword());
        logger.info("✅ [CONTROLLER /auth/login] Login exitoso para {}. Roles devueltos: {}", request.getEmail(), response.getRoles());
        return ResponseEntity.ok(response);
    }
}
