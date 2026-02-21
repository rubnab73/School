package com.rubayet.school.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testUserCreation() {
        // When
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setRole("ROLE_STUDENT");

        // Then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("testuser");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getRole()).isEqualTo("ROLE_STUDENT");
    }

    @Test
    void testUserEquality() {
        // Given
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword("pass1");
        user1.setRole("ROLE_STUDENT");

        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("user1");
        user2.setPassword("pass1");
        user2.setRole("ROLE_STUDENT");

        // Then
        assertThat(user1).isEqualTo(user2);
    }

    @Test
    void testUserToString() {
        // Given
        user.setUsername("testuser");
        user.setRole("ROLE_TEACHER");

        // When
        String userString = user.toString();

        // Then
        assertThat(userString).contains("testuser");
        assertThat(userString).contains("ROLE_TEACHER");
    }

    @Test
    void testRoleValidation() {
        // Test different roles
        user.setRole("ROLE_STUDENT");
        assertThat(user.getRole()).isEqualTo("ROLE_STUDENT");

        user.setRole("ROLE_TEACHER");
        assertThat(user.getRole()).isEqualTo("ROLE_TEACHER");

        user.setRole("ROLE_ADMIN");
        assertThat(user.getRole()).isEqualTo("ROLE_ADMIN");
    }
}
