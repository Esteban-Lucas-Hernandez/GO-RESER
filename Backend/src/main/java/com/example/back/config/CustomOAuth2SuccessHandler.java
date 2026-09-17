package com.example.back.config;

import com.example.back.models.user.Role;
import com.example.back.models.user.User;
import com.example.back.repo.user.RoleRepository;
import com.example.back.repo.user.UserRepository;
import com.example.back.security.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User oauth2User = ((OAuth2AuthenticationToken) authentication).getPrincipal();

            String email = oauth2User.getAttribute("email");
            String fullName = oauth2User.getAttribute("name");
            String pictureUrl = oauth2User.getAttribute("picture");

            Optional<User> existingUser = usuarioRepository.findByEmail(email);

            User user;
            if (existingUser.isPresent()) {
                user = existingUser.get();
                if (!Boolean.TRUE.equals(user.getEstado())) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario inactivo");
                    return;
                }

                if ((user.getFotoUrl() == null || user.getFotoUrl().isEmpty()) && pictureUrl != null) {
                    user.setFotoUrl(pictureUrl);
                    user = usuarioRepository.save(user);
                }
            } else {
                user = new User();
                user.setEmail(email);
                user.setNombreCompleto(fullName != null ? fullName : email);
                user.setContrasena("");
                user.setEstado(true);
                user.setFotoUrl(pictureUrl);

                Role userRole = roleRepository.findByName("ROLE_USER")
                        .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

                Set<Role> roles = new HashSet<>();
                roles.add(userRole);
                user.setRoles(roles);

                user = usuarioRepository.save(user);
            }

            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                Role userRole = roleRepository.findByName("ROLE_USER")
                        .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

                Set<Role> roles = user.getRoles() != null ? user.getRoles() : new HashSet<>();
                roles.add(userRole);
                user.setRoles(roles);
                user = usuarioRepository.save(user);
            }

            String token = jwtUtil.generateToken(user);

            String redirectUrl = UriComponentsBuilder.fromUriString("https://go-reser-api-rest-full-stack-spring-r46x.onrender.com/auth/google/callback")
                    .queryParam("token", token)
                    .queryParam("userId", user.getIdUsuario())
                    .queryParam("email", user.getEmail())
                    .queryParam("fullName", user.getNombreCompleto())
                    .queryParam("fotoUrl", user.getFotoUrl())
                    .queryParam("success", true)
                    .toUriString();

            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
