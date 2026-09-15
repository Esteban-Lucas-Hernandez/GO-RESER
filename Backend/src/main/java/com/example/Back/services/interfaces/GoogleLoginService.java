package com.example.back.services.interfaces;

import com.example.back.dto.auth.GoogleLoginResponseDTO;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public interface GoogleLoginService {
    GoogleLoginResponseDTO processGoogleLogin(OAuth2AuthenticationToken authentication);
}
