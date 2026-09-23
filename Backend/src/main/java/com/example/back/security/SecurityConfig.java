package com.example.back.security;

import com.example.back.config.CustomOAuth2SuccessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("Configurando cadena de filtros de seguridad");

        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/auth/**").permitAll();
                auth.requestMatchers("/public/**").permitAll();
                auth.requestMatchers("/oauth2/**").permitAll();
                auth.requestMatchers("/login/oauth2/**").permitAll();
                auth.requestMatchers("/h2-console/**").permitAll();
                auth.requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**"
                ).permitAll();

                // Permitir consulta pública de fechas reservadas para el calendario
                auth.requestMatchers("/user/reservas/habitacion/*/fechas-reservadas").permitAll();

                auth.requestMatchers("/user/**").hasAnyRole("USER", "ADMIN", "SUPERADMIN");
                auth.requestMatchers("/admin/**").hasAnyRole("ADMIN");
                auth.requestMatchers("/superadmin/**").hasRole("SUPERADMIN");

                auth.anyRequest().authenticated();
            })
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, authException) -> {
                    logger.error("⛔ [SECURITY 401 UNAUTHORIZED] Intento no autorizado en URI: {} - Error: {}", 
                            request.getRequestURI(), authException.getMessage());
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"status\": 401, \"error\": \"Unauthorized\", \"message\": \"No autorizado. Inicie sesión para continuar.\"}");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    logger.error("⛔ [SECURITY 403 FORBIDDEN] Acceso denegado (falta de rol/permiso) en URI: {} - Error: {}", 
                            request.getRequestURI(), accessDeniedException.getMessage());
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("{\"status\": 403, \"error\": \"Forbidden\", \"message\": \"No tiene permisos suficientes para acceder a este recurso.\"}");
                })
            )
            .oauth2Login(oauth2 -> oauth2
                .successHandler(customOAuth2SuccessHandler)
                .failureUrl("/auth/google/failure")
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
