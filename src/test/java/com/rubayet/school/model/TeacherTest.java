package com.rubayet.school.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherTest {

    private Teacher teacher;
    private User user;
    private Department department;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("teacheruser");
        user.setPassword("password");
        user.setRole("ROLE_TEACHER");

        department = new Department();
        department.setId(1L);
        department.setName("Computer Science");

        teacher = new Teacher();
    }

    @Test
    void testTeacherCreation() {
        // When
        teacher.setId(1L);
        teacher.setName("Dr. Smith");
        teacher.setEmail("smith@example.com");
        teacher.setUser(user);
        teacher.setDepartment(department);

        // Then
        assertThat(teacher.getId()).isEqualTo(1L);
        assertThat(teacher.getName()).isEqualTo("Dr. Smith");
        assertThat(teacher.getEmail()).isEqualTo("smith@example.com");
        assertThat(teacher.getUser()).isEqualTo(user);
        assertThat(teacher.getDepartment()).isEqualTo(department);
    }

    @Test
    void testTeacherCourses() {
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
        teacher.setCourses(courses);

        // Then
        assertThat(teacher.getCourses()).hasSize(2);
        assertThat(teacher.getCourses()).contains(course1, course2);
    }

    @Test
    void testTeacherDepartmentRelationship() {
        // When
        teacher.setDepartment(department);

        // Then
        assertThat(teacher.getDepartment()).isNotNull();
        assertThat(teacher.getDepartment().getName()).isEqualTo("Computer Science");
    }

    @Test
    void testTeacherUserRelationship() {
        // When
        teacher.setUser(user);

        // Then
        assertThat(teacher.getUser()).isNotNull();
        assertThat(teacher.getUser().getUsername()).isEqualTo("teacheruser");
        assertThat(teacher.getUser().getRole()).isEqualTo("ROLE_TEACHER");
    }

    @Test
    void testTeacherEquality() {
        // Given
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setName("Teacher 1");

        Teacher teacher2 = new Teacher();
        teacher2.setId(1L);
        teacher2.setName("Teacher 1");

        // Then
        assertThat(teacher1).isEqualTo(teacher2);
    }
}
