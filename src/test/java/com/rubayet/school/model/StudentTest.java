package com.rubayet.school.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StudentTest {

    private Student student;
    private User user;
    private Department department;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("studentuser");
        user.setPassword("password");
        user.setRole("ROLE_STUDENT");

        department = new Department();
        department.setId(1L);
        department.setName("Computer Science");

        student = new Student();
    }

    @Test
    void testStudentCreation() {
        // When
        student.setId(1L);
        student.setName("John Doe");
        student.setEmail("john@example.com");
        student.setUser(user);
        student.setDepartment(department);

        // Then
        assertThat(student.getId()).isEqualTo(1L);
        assertThat(student.getName()).isEqualTo("John Doe");
        assertThat(student.getEmail()).isEqualTo("john@example.com");
        assertThat(student.getUser()).isEqualTo(user);
        assertThat(student.getDepartment()).isEqualTo(department);
    }

    @Test
    void testStudentCourses() {
        // Given
        Course course1 = new Course();
        course1.setId(1L);
        course1.setTitle("Java Programming");

        Course course2 = new Course();
        course2.setId(2L);
        course2.setTitle("Data Structures");

        List<Course> courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);

        // When
        student.setCourses(courses);

        // Then
        assertThat(student.getCourses()).hasSize(2);
        assertThat(student.getCourses()).contains(course1, course2);
    }

    @Test
    void testStudentDepartmentRelationship() {
        // When
        student.setDepartment(department);

        // Then
        assertThat(student.getDepartment()).isNotNull();
        assertThat(student.getDepartment().getName()).isEqualTo("Computer Science");
    }

    @Test
    void testStudentUserRelationship() {
        // When
        student.setUser(user);

        // Then
        assertThat(student.getUser()).isNotNull();
        assertThat(student.getUser().getUsername()).isEqualTo("studentuser");
        assertThat(student.getUser().getRole()).isEqualTo("ROLE_STUDENT");
    }

    @Test
    void testStudentEquality() {
        // Given
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Student 1");

        Student student2 = new Student();
        student2.setId(1L);
        student2.setName("Student 1");

        // Then
        assertThat(student1).isEqualTo(student2);
    }
}
