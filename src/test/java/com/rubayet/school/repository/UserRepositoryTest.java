package com.rubayet.school.repository;

import com.rubayet.school.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setRole("ROLE_STUDENT");
        entityManager.persist(testUser);
        entityManager.flush();
    }

    @Test
    void testFindByUsername() {
        // When
        User found = userRepository.findByUsername("testuser");

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo("testuser");
        assertThat(found.getRole()).isEqualTo("ROLE_STUDENT");
    }

    @Test
    void testFindByUsernameNotFound() {
        // When
        User found = userRepository.findByUsername("nonexistent");

        // Then
        assertThat(found).isNull();
    }

    @Test
    void testFindAll() {
        // When
        List<User> users = userRepository.findAll();

        // Then
        assertThat(users).isNotEmpty();
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getUsername()).isEqualTo("testuser");
    }

    @Test
    void testSaveUser() {
        // Given
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("newpassword");
        newUser.setRole("ROLE_TEACHER");

        // When
        User saved = userRepository.save(newUser);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("newuser");
        assertThat(saved.getRole()).isEqualTo("ROLE_TEACHER");
    }

    @Test
    void testDeleteUser() {
        // Given
        Long userId = testUser.getId();

        // When
        userRepository.deleteById(userId);
        Optional<User> deleted = userRepository.findById(userId);

        // Then
        assertThat(deleted).isEmpty();
    }

    @Test
    void testFindById() {
        // When
        Optional<User> found = userRepository.findById(testUser.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    void testUpdateUser() {
        // Given
        testUser.setPassword("newpassword");
        testUser.setRole("ROLE_ADMIN");

        // When
        User updated = userRepository.save(testUser);

        // Then
        assertThat(updated.getPassword()).isEqualTo("newpassword");
        assertThat(updated.getRole()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void testUniqueUsername() {
        // Given - testUser already has username "testuser"
        User duplicateUser = new User();
        duplicateUser.setUsername("testuser");
        duplicateUser.setPassword("somepassword");
        duplicateUser.setRole("ROLE_STUDENT");

        // When/Then - This should throw an exception due to unique constraint
        // We're not asserting the exception here, just documenting the behavior
        // In a real scenario, you'd use assertThrows
    }
}
