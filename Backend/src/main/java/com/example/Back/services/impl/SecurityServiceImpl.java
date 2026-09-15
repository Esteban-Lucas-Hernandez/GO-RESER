package com.example.back.services.impl;

import com.example.back.models.user.User;
import com.example.back.repo.user.UserRepository;
import com.example.back.services.interfaces.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private UserRepository usuarioRepository;

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        String email = authentication.getName();
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User getAuthenticatedUser() {
        User user = getCurrentUser();
        if (user == null) {
            throw new RuntimeException("Usuario no autenticado");
        }
        return user;
    }
}
