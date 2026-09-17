package com.example.back.services.interfaces;

import com.example.back.dto.auth.RegistrationRequestDTO;

public interface RegistrationService {
    String registrar(RegistrationRequestDTO request);
}
