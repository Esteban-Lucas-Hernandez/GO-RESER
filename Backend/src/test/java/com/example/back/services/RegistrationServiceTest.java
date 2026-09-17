package com.example.back.services;

import com.example.back.dto.auth.RegistrationRequestDTO;
import com.example.back.models.user.Role;
import com.example.back.models.user.User;
import com.example.back.repo.user.RoleRepository;
import com.example.back.repo.user.UserRepository;
import com.example.back.services.impl.RegistrationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Test
    void testRegistrarSuccess() {
        RegistrationRequestDTO request = new RegistrationRequestDTO();
        request.setEmail("test@email.com");
        request.setNombreCompleto("Test User");
        request.setContrasena("pass123");

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass123")).thenReturn("encoded_pass");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(new Role("ROLE_USER")));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        String result = registrationService.registrar(request);
        assertEquals("Usuario registrado exitosamente", result);
    }
}
