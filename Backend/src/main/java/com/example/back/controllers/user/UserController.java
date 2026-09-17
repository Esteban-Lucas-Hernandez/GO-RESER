package com.example.back.controllers.user;

import com.example.back.dto.user.UpdateProfileDTO;
import com.example.back.dto.user.UserDTO;
import com.example.back.mapper.user.UserMapper;
import com.example.back.models.user.User;
import com.example.back.repo.user.UserRepository;
import com.example.back.services.interfaces.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/profile")
public class UserController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private UserMapper usuarioMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<UserDTO> getProfile() {
        User user = securityService.getAuthenticatedUser();
        return ResponseEntity.ok(usuarioMapper.toDTO(user));
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateProfile(@RequestBody UpdateProfileDTO dto) {
        User user = securityService.getAuthenticatedUser();

        if (dto.getNombreCompleto() != null) user.setNombreCompleto(dto.getNombreCompleto());
        if (dto.getTelefono() != null) user.setTelefono(dto.getTelefono());
        if (dto.getDocumento() != null) user.setDocumento(dto.getDocumento());
        if (dto.getFotoUrl() != null) user.setFotoUrl(dto.getFotoUrl());
        if (dto.getContrasena() != null && !dto.getContrasena().isBlank()) {
            user.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        }

        User updated = usuarioRepository.save(user);
        return ResponseEntity.ok(usuarioMapper.toDTO(updated));
    }
}
