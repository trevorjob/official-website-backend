package com.msc.contact.form.security;

import com.msc.contact.form.model.AdminUser;
import com.msc.contact.form.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Primary
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AdminUserRepository adminUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AdminUser admin = adminUserRepository.findByEmailActive(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new User(
                admin.getEmail(),
                admin.getPassword(),
                Collections.emptyList()
        );
    }
}