package com.example.back.services.impl;

import com.example.back.dto.auth.LoginResponseDTO;
import com.example.back.models.user.User;
import com.example.back.repo.user.UserRepository;
import com.example.back.security.JwtUtil;
import com.example.back.services.interfaces.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public LoginResponseDTO login(String email, String password) {
        User usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!Boolean.TRUE.equals(usuario.getEstado())) {
            throw new RuntimeException("Tu cuenta se encuentra inactiva o deshabilitada. Contacta al administrador.");
        }

        if (usuario.getContrasena() == null || !passwordEncoder.matches(password, usuario.getContrasena())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        List<String> roles = usuario.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());

        String token = jwtUtil.generateToken(usuario.getEmail(), roles);

        return new LoginResponseDTO(
                true,
                "Login exitoso",
                token,
                usuario.getIdUsuario(),
                usuario.getNombreCompleto(),
                usuario.getEmail(),
                roles
        );
    }
}
