package com.example.back.services.interfaces;

import com.example.back.models.user.User;

public interface SecurityService {
    User getCurrentUser();
    User getAuthenticatedUser();
}
