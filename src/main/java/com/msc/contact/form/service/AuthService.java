package com.msc.contact.form.service;

import com.msc.contact.form.dto.LoginRequest;
import com.msc.contact.form.dto.LoginResponse;
public interface AuthService {
    public LoginResponse login(LoginRequest loginRequest);
}
