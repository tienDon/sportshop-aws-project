package com.ojtteamaws.sportshopecommerceplatformbe.config;

import com.ojtteamaws.sportshopecommerceplatformbe.entity.User;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;                 // ⭐ NEW
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DefaultAdminConfig {

    private final IUserRepository userRepository;

    @Value("${app.admin.email}")
    private String defaultAdminEmail;

    @Value("${app.admin.phone}")
    private String defaultAdminPhone;


    @Bean
    public CommandLineRunner createDefaultAdmin() {
        return args -> {

            if (!userRepository.existsByRole("admin")) {
                User admin = User.builder()
                        .fullName("System Administrator")
                        .role("ADMIN")
                        .email(defaultAdminEmail)
                        .phone(defaultAdminPhone)
                        .emailVerified(false)
                        .phoneVerified(false)
                        .build();

                userRepository.save(admin);
                System.out.println("""
                        ==============================
                        ✔ DEFAULT ADMIN CREATED (OTP LOGIN)                  
                        role: ADMIN
                        email: %s
                        phone: %s
                        ==============================
                        """.formatted(defaultAdminEmail, defaultAdminPhone));
            }
        };
    }
}
