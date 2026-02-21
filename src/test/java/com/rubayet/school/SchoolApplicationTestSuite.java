package com.rubayet.school;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test Suite for the entire application
 * This test verifies that the Spring Boot application context loads successfully
 */
@SpringBootTest
@ActiveProfiles("test")
class SchoolApplicationTestSuite {

    @Test
    void contextLoads() {
        // This test verifies that the Spring application context loads successfully
        // If the context fails to load, this test will fail
        assertThat(true).isTrue();
    }
}
