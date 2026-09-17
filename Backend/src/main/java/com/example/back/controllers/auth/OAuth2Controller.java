package com.example.back.controllers.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuth2Controller {

    @GetMapping("/oauth2/authorization/google")
    public String redirectToGoogle() {
        return "forward:/oauth2/authorization/google";
    }
}
