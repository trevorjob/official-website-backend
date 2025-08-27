package com.msc.contact.form.config;

import com.msc.contact.form.model.AdminUser;
import com.msc.contact.form.repository.AdminUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initAdminUser(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!adminUserRepository.findByEmail("admin@example.com").isPresent()) {
                AdminUser admin = new AdminUser();
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                adminUserRepository.save(admin);
            }
        };
    }

}
