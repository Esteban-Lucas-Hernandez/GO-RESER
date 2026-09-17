package com.example.back.mapper.user;

import com.example.back.dto.user.UserDTO;
import com.example.back.models.user.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDTO toDTO(User usuario) {
        if (usuario == null) return null;
        UserDTO dto = new UserDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombreCompleto(usuario.getNombreCompleto());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setDocumento(usuario.getDocumento());
        dto.setEstado(usuario.getEstado());
        dto.setFotoUrl(usuario.getFotoUrl());

        if (usuario.getRoles() != null) {
            dto.setRoles(usuario.getRoles().stream()
                    .map(role -> role.getName())
                    .collect(Collectors.toList()));
        }

        if (usuario.getFechaRegistro() != null) {
            dto.setFechaRegistro(usuario.getFechaRegistro().toString());
        }

        return dto;
    }
}
