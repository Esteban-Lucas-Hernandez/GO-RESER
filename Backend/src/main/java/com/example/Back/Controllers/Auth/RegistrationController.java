package com.example.back.controllers.auth;

import com.example.back.dto.auth.RegistrationRequestDTO;
import com.example.back.services.interfaces.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class RegistrationController {

    private final RegistrationService registroService;

    public RegistrationController(RegistrationService registroService) {
        this.registroService = registroService;
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistrationRequestDTO request) {
        String mensaje = registroService.registrar(request);
        return ResponseEntity.ok().body("{\"message\": \"" + mensaje + "\"}");
    }
}
