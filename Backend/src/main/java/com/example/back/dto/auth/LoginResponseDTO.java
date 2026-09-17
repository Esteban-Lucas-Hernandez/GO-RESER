package com.example.back.dto.auth;

import java.util.List;

public class LoginResponseDTO {
    private boolean success;
    private String message;
    private String token;
    private Integer userId;
    private String username;
    private String email;
    private List<String> roles;

    public LoginResponseDTO() {}

    public LoginResponseDTO(boolean success, String message, String token, Integer userId, String username, String email, List<String> roles) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}
