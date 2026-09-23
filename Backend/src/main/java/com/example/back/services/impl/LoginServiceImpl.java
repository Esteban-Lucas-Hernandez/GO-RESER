package com.example.back.services.impl;

import com.example.back.dto.auth.LoginResponseDTO;
import com.example.back.models.user.User;
import com.example.back.repo.user.UserRepository;
import com.example.back.security.JwtUtil;
import com.example.back.services.interfaces.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public LoginResponseDTO login(String email, String password) {
        logger.info("🔐 [LOGIN SERVICE] Procesando login para email: {}", email);

        User usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("❌ [LOGIN SERVICE] Usuario no encontrado con email: {}", email);
                    return new RuntimeException("Usuario no encontrado con email: " + email);
                });

        logger.info("👤 [LOGIN SERVICE] Usuario encontrado: id={}, nombre={}, estado={}", 
                usuario.getIdUsuario(), usuario.getNombreCompleto(), usuario.getEstado());

        if (!Boolean.TRUE.equals(usuario.getEstado())) {
            logger.warn("⛔ [LOGIN SERVICE] Cuenta inactiva o deshabilitada para email: {}", email);
            throw new RuntimeException("Tu cuenta se encuentra inactiva o deshabilitada. Contacta al administrador.");
        }

        if (usuario.getContrasena() == null || !passwordEncoder.matches(password, usuario.getContrasena())) {
            logger.warn("❌ [LOGIN SERVICE] Contraseña incorrecta para email: {}", email);
            throw new RuntimeException("Credenciales incorrectas");
        }

        List<String> roles = usuario.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());

        logger.info("👑 [LOGIN SERVICE] Roles asignados al usuario {}: {}", email, roles);

        String token = jwtUtil.generateToken(usuario.getEmail(), roles);
        logger.info("🎟️ [LOGIN SERVICE] Token JWT generado exitosamente para {}", email);

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
