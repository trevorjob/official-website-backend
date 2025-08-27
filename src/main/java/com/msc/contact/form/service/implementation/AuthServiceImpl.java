package com.msc.contact.form.service.implementation;

import com.msc.contact.form.dto.LoginRequest;
import com.msc.contact.form.dto.LoginResponse;
import com.msc.contact.form.dto.ResponseDto;
import com.msc.contact.form.security.JwtUtil;
import com.msc.contact.form.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            String token = jwtUtil.generateToken(authentication.getName());

            LoginResponse response = new LoginResponse();
            response.setToken(token);
            response.setMessage("Login successful");

            return response;
        } catch (AuthenticationException e) {
            LoginResponse response = new LoginResponse();
            response.setMessage("Invalid email or password");
            return response;
        }
    }



}
