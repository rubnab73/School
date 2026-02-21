package com.rubayet.school.repository;

import com.rubayet.school.model.Department;
import com.rubayet.school.model.Student;
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
class StudentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    private User testUser;
    private Student testStudent;
    private Department testDepartment;

    @BeforeEach
    void setUp() {
        // Create a test department
        testDepartment = new Department();
        testDepartment.setName("Computer Science");
        entityManager.persist(testDepartment);

        // Create a test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setRole("ROLE_STUDENT");
        entityManager.persist(testUser);

        // Create a test student
        testStudent = new Student();
        testStudent.setName("Test Student");
        testStudent.setEmail("test@example.com");
        testStudent.setUser(testUser);
        testStudent.setDepartment(testDepartment);
        entityManager.persist(testStudent);

        entityManager.flush();
    }

    @Test
    void testFindByUser() {
        // When
        Student found = studentRepository.findByUser(testUser);

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Test Student");
        assertThat(found.getEmail()).isEqualTo("test@example.com");
        assertThat(found.getUser().getUsername()).isEqualTo("testuser");
    }

    @Test
    void testFindAll() {
        // When
        List<Student> students = studentRepository.findAll();

        // Then
        assertThat(students).isNotEmpty();
        assertThat(students).hasSize(1);
        assertThat(students.get(0).getName()).isEqualTo("Test Student");
    }

    @Test
    void testSaveStudent() {
        // Given
        User newUser = new User();
        newUser.setUsername("newstudent");
        newUser.setPassword("password456");
        newUser.setRole("ROLE_STUDENT");
        entityManager.persist(newUser);

        Student newStudent = new Student();
        newStudent.setName("New Student");
        newStudent.setEmail("new@example.com");
        newStudent.setUser(newUser);
        newStudent.setDepartment(testDepartment);

        // When
        Student saved = studentRepository.save(newStudent);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("New Student");
    }

    @Test
    void testDeleteStudent() {
        // Given
        Long studentId = testStudent.getId();

        // When
        studentRepository.deleteById(studentId);
        Optional<Student> deleted = studentRepository.findById(studentId);

        // Then
        assertThat(deleted).isEmpty();
    }

    @Test
    void testFindById() {
        // When
        Optional<Student> found = studentRepository.findById(testStudent.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Student");
    }

    @Test
    void testUpdateStudent() {
        // Given
        testStudent.setName("Updated Student");
        testStudent.setEmail("updated@example.com");

        // When
        Student updated = studentRepository.save(testStudent);

        // Then
        assertThat(updated.getName()).isEqualTo("Updated Student");
        assertThat(updated.getEmail()).isEqualTo("updated@example.com");
    }
}
