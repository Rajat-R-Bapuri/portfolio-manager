package com.stocksScreener.controller;

import com.stocksScreener.model.GoogleIdToken;
import com.stocksScreener.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(value = "/google/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> login(GoogleIdToken idToken) throws GeneralSecurityException, IOException {
        System.out.println(idToken);
        Map<String, String> response = new HashMap<>();
        String jwt = loginService.verifyIdToken(idToken.getIdToken());
        if(jwt == null){
            response.put("Error", "Invalid Google ID Token");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response.put("token", jwt);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
