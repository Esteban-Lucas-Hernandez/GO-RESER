package com.example.back.services.impl;

import com.example.back.dto.auth.RegistrationRequestDTO;
import com.example.back.models.user.Role;
import com.example.back.models.user.User;
import com.example.back.repo.user.RoleRepository;
import com.example.back.repo.user.UserRepository;
import com.example.back.services.interfaces.RegistrationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationServiceImpl(UserRepository usuarioRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String registrar(RegistrationRequestDTO request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        User usuario = new User();
        usuario.setNombreCompleto(request.getNombreCompleto());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setDocumento(request.getDocumento());
        usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        usuario.setFotoUrl(request.getFotoUrl());
        usuario.setEstado(true);

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Rol ROLE_USER no encontrado"));
        usuario.setRoles(Collections.singleton(userRole));

        usuarioRepository.save(usuario);
        return "Usuario registrado exitosamente";
    }
}
