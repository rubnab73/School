package com.rubayet.school.config;

import com.rubayet.school.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void testPasswordEncoderBean() {
        // Given
        String rawPassword = "password123";

        // When
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Then
        assertThat(encodedPassword).isNotNull();
        assertThat(encodedPassword).isNotEqualTo(rawPassword);
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
    }

    @Test
    void testPasswordEncoderMatches() {
        // Given
        String password = "mySecretPassword";
        String encoded = passwordEncoder.encode(password);

        // When/Then
        assertThat(passwordEncoder.matches(password, encoded)).isTrue();
        assertThat(passwordEncoder.matches("wrongPassword", encoded)).isFalse();
    }

    @Test
    void testPasswordEncoderGeneratesDifferentHashesForSamePassword() {
        // Given
        String password = "samePassword";

        // When
        String encoded1 = passwordEncoder.encode(password);
        String encoded2 = passwordEncoder.encode(password);

        // Then - BCrypt generates different salts
        assertThat(encoded1).isNotEqualTo(encoded2);
        assertThat(passwordEncoder.matches(password, encoded1)).isTrue();
        assertThat(passwordEncoder.matches(password, encoded2)).isTrue();
    }

    @Test
    void testCustomUserDetailsServiceBean() {
        // Then
        assertThat(customUserDetailsService).isNotNull();
    }
}
