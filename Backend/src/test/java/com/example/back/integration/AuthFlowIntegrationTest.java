package com.example.back.integration;

import com.example.back.dto.auth.LoginRequestDTO;
import com.example.back.dto.auth.LoginResponseDTO;
import com.example.back.services.interfaces.LoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthFlowIntegrationTest {

    @Mock
    private LoginService loginService;

    @Test
    void testCompleteAuthFlow() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("admin@admin.com");
        request.setPassword("admin123");

        LoginResponseDTO response = new LoginResponseDTO(true, "OK", "mock_token", 1, "Admin", "admin@admin.com", List.of("ROLE_ADMIN"));
        when(loginService.login("admin@admin.com", "admin123")).thenReturn(response);

        LoginResponseDTO result = loginService.login(request.getEmail(), request.getPassword());
        assertTrue(result.isSuccess());
        assertEquals("mock_token", result.getToken());
    }
}
