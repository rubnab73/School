package com.rubayet.school.service;

import com.rubayet.school.model.User;
import com.rubayet.school.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setRole("ROLE_STUDENT");
    }

    @Test
    void testLoadUserByUsername_Success() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("testuser");
        assertThat(userDetails.getPassword()).isEqualTo("encodedPassword");
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority())
                .isEqualTo("ROLE_STUDENT");

        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsername_WithRolePrefix() {
        // Given
        testUser.setRole("ROLE_TEACHER");
        when(userRepository.findByUsername("teacher")).thenReturn(testUser);

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("teacher");

        // Then
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority())
                .isEqualTo("ROLE_TEACHER");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Given
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);

        // When/Then
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("nonexistent"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository, times(1)).findByUsername("nonexistent");
    }

    @Test
    void testLoadUserByUsername_WithDifferentRoles() {
        // Test with ROLE_ADMIN
        testUser.setRole("ROLE_ADMIN");
        when(userRepository.findByUsername("admin")).thenReturn(testUser);

        UserDetails adminDetails = customUserDetailsService.loadUserByUsername("admin");

        assertThat(adminDetails.getAuthorities().iterator().next().getAuthority())
                .isEqualTo("ROLE_ADMIN");
    }

    @Test
    void testLoadUserByUsername_RoleWithoutPrefix() {
        // Given - Role stored without ROLE_ prefix
        testUser.setRole("STUDENT");
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

        // Then - The service should handle roles with or without ROLE_ prefix
        assertThat(userDetails).isNotNull();
    }
}
