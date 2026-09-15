package com.example.back.services.impl;

import com.example.back.dto.auth.GoogleLoginResponseDTO;
import com.example.back.models.user.Role;
import com.example.back.models.user.User;
import com.example.back.repo.user.RoleRepository;
import com.example.back.repo.user.UserRepository;
import com.example.back.security.JwtUtil;
import com.example.back.services.interfaces.GoogleLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GoogleLoginServiceImpl implements GoogleLoginService {

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public GoogleLoginResponseDTO processGoogleLogin(OAuth2AuthenticationToken authentication) {
        OAuth2User oAuth2User = authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        Optional<User> userOptional = usuarioRepository.findByEmail(email);
        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (user.getFotoUrl() == null && picture != null) {
                user.setFotoUrl(picture);
                usuarioRepository.save(user);
            }
        } else {
            user = new User();
            user.setEmail(email);
            user.setNombreCompleto(name);
            user.setFotoUrl(picture);
            user.setEstado(true);

            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Rol ROLE_USER no encontrado"));
            user.setRoles(Collections.singleton(userRole));
            user = usuarioRepository.save(user);
        }

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        String token = jwtUtil.generateToken(user.getEmail(), roles);

        GoogleLoginResponseDTO response = new GoogleLoginResponseDTO();
        response.setSuccess(true);
        response.setMessage("Login con Google exitoso");
        response.setToken(token);
        response.setUserId(user.getIdUsuario());
        response.setFullName(user.getNombreCompleto());
        response.setEmail(user.getEmail());
        response.setFotoUrl(user.getFotoUrl());
        response.setRoles(roles);

        return response;
    }
}
