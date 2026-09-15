package com.example.back.controllers.auth;

import com.example.back.dto.auth.RegistrationRequestDTO;
import com.example.back.services.interfaces.RegistrationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationControllerTest {

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private RegistrationController registrationController;

    @Test
    void testRegistrationSuccess() {
        RegistrationRequestDTO request = new RegistrationRequestDTO();
        request.setEmail("new@example.com");
        request.setNombreCompleto("New User");
        request.setContrasena("secret123");

        when(registrationService.registrar(request)).thenReturn("Usuario registrado exitosamente");

        ResponseEntity<?> response = registrationController.registrar(request);
        assertEquals(200, response.getStatusCode().value());
    }
}
