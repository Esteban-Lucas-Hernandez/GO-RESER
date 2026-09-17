package com.example.back.services.interfaces;

import com.example.back.dto.auth.LoginResponseDTO;

public interface LoginService {
    LoginResponseDTO login(String email, String password);
}
