package com.msc.contact.form.service;

import com.msc.contact.form.dto.LoginRequest;
import com.msc.contact.form.dto.LoginResponse;
import com.msc.contact.form.dto.ResponseDto;
import com.msc.contact.form.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;

public interface AuthService {
    public LoginResponse login(LoginRequest loginRequest);
}
