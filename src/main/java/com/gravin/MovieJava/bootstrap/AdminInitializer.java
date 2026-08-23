package com.gravin.MovieJava.bootstrap;

import com.gravin.MovieJava.common.enums.UserType;
import com.gravin.MovieJava.users.domain.User;
import com.gravin.MovieJava.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBooleanProperty(name = "app.bootstrap.admin-initialization-enabled")
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements CommandLineRunner {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap.admin-username}")
    private String adminUsername;

    @Value("${app.bootstrap.admin-password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.existsByUsername(adminUsername)) {
            return;
        }

        User admin = new User();
        admin.setUserType(UserType.ADMIN);
        admin.setUsername(adminUsername);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setFullName("Administrator");

        userRepository.save(admin);

        log.warn("Bootstrap admin '{}' created. CHANGE ITS PASSWORD and set "
                + "app.bootstrap.admin.enabled=false in production.", adminUsername);
    }
}
