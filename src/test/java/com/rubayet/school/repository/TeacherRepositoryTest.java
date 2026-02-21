package com.rubayet.school.repository;

import com.rubayet.school.model.Teacher;
import com.rubayet.school.model.User;
import com.rubayet.school.model.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TeacherRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TeacherRepository teacherRepository;

    private User testUser;
    private Teacher testTeacher;
    private Department testDepartment;

    @BeforeEach
    void setUp() {
        // Create a test department
        testDepartment = new Department();
        testDepartment.setName("Computer Science");
        entityManager.persist(testDepartment);

        // Create a test user
        testUser = new User();
        testUser.setUsername("teacheruser");
        testUser.setPassword("password123");
        testUser.setRole("ROLE_TEACHER");
        entityManager.persist(testUser);

        // Create a test teacher
        testTeacher = new Teacher();
        testTeacher.setName("Test Teacher");
        testTeacher.setEmail("teacher@example.com");
        testTeacher.setUser(testUser);
        testTeacher.setDepartment(testDepartment);
        entityManager.persist(testTeacher);

        entityManager.flush();
    }

    @Test
    void testFindByUser() {
        // When
        Teacher found = teacherRepository.findByUser(testUser);

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Test Teacher");
        assertThat(found.getEmail()).isEqualTo("teacher@example.com");
        assertThat(found.getUser().getUsername()).isEqualTo("teacheruser");
    }

    @Test
    void testFindAll() {
        // When
        List<Teacher> teachers = teacherRepository.findAll();

        // Then
        assertThat(teachers).isNotEmpty();
        assertThat(teachers).hasSize(1);
        assertThat(teachers.get(0).getName()).isEqualTo("Test Teacher");
    }

    @Test
    void testSaveTeacher() {
        // Given
        User newUser = new User();
        newUser.setUsername("newteacher");
        newUser.setPassword("password456");
        newUser.setRole("ROLE_TEACHER");
        entityManager.persist(newUser);

        Teacher newTeacher = new Teacher();
        newTeacher.setName("New Teacher");
        newTeacher.setEmail("newteacher@example.com");
        newTeacher.setUser(newUser);
        newTeacher.setDepartment(testDepartment);

        // When
        Teacher saved = teacherRepository.save(newTeacher);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("New Teacher");
    }

    @Test
    void testDeleteTeacher() {
        // Given
        Long teacherId = testTeacher.getId();

        // When
        teacherRepository.deleteById(teacherId);
        Optional<Teacher> deleted = teacherRepository.findById(teacherId);

        // Then
        assertThat(deleted).isEmpty();
    }

    @Test
    void testFindById() {
        // When
        Optional<Teacher> found = teacherRepository.findById(testTeacher.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Teacher");
    }

    @Test
    void testUpdateTeacher() {
        // Given
        testTeacher.setName("Updated Teacher");
        testTeacher.setEmail("updated@example.com");

        // When
        Teacher updated = teacherRepository.save(testTeacher);

        // Then
        assertThat(updated.getName()).isEqualTo("Updated Teacher");
        assertThat(updated.getEmail()).isEqualTo("updated@example.com");
    }
}
