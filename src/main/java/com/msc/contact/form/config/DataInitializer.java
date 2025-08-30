package com.msc.contact.form.config;

import com.msc.contact.form.model.AdminUser;
import com.msc.contact.form.repository.AdminUserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Getter
    @Setter
    @Value("${admin.default.email}")
    private String defaultEmail;

    @Value("${admin.default.password}")
    private String defaultPassword;
    @Bean
    public CommandLineRunner initAdminUser(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (adminUserRepository.findByEmail(defaultEmail).isEmpty()) {
                AdminUser admin = new AdminUser();
                admin.setEmail(defaultEmail);
                admin.setPassword(passwordEncoder.encode(defaultPassword));
                adminUserRepository.save(admin);
                System.out.println("✅ Default admin user created: " + defaultEmail);
            }
        };
    }

}
