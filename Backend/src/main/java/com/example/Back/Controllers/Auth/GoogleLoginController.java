package com.example.back.controllers.auth;

import com.example.back.dto.auth.GoogleLoginResponseDTO;
import com.example.back.services.interfaces.GoogleLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/google")
public class GoogleLoginController {

    @Autowired
    private GoogleLoginService googleLoginService;

    @GetMapping("/login")
    public RedirectView initiateGoogleLogin() {
        return new RedirectView("/oauth2/authorization/google");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginWithGoogle(OAuth2AuthenticationToken authentication) {
        try {
            if (authentication == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "No autenticado con Google");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            GoogleLoginResponseDTO response = googleLoginService.processGoogleLogin(authentication);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error procesando el login con Google: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/success")
    public RedirectView handleGoogleSuccess(OAuth2AuthenticationToken authentication) {
        try {
            GoogleLoginResponseDTO response = googleLoginService.processGoogleLogin(authentication);
            String redirectUrl = String.format(
                "http://localhost:4200/auth/callback?token=%s&userId=%d&email=%s&name=%s&photoUrl=%s",
                URLEncoder.encode(response.getToken(), StandardCharsets.UTF_8),
                response.getUserId(),
                URLEncoder.encode(response.getEmail(), StandardCharsets.UTF_8),
                URLEncoder.encode(response.getFullName(), StandardCharsets.UTF_8),
                URLEncoder.encode(response.getFotoUrl() != null ? response.getFotoUrl() : "", StandardCharsets.UTF_8)
            );
            return new RedirectView(redirectUrl);
        } catch (Exception e) {
            return new RedirectView("http://localhost:4200/login?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
        }
    }

    @GetMapping("/failure")
    public RedirectView handleGoogleFailure(@RequestParam(required = false) String error) {
        String errorMessage = error != null ? error : "Error desconocido en la autenticación con Google";
        return new RedirectView("http://localhost:4200/login?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
    }
}
