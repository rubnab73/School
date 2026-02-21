package com.rubayet.school.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CourseTest {

    private Course course;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Test Teacher");

        course = new Course();
    }

    @Test
    void testCourseCreation() {
        // When
        course.setId(1L);
        course.setTitle("Introduction to Programming");
        course.setDescription("Learn programming basics");
        course.setTeacher(teacher);

        // Then
        assertThat(course.getId()).isEqualTo(1L);
        assertThat(course.getTitle()).isEqualTo("Introduction to Programming");
        assertThat(course.getDescription()).isEqualTo("Learn programming basics");
        assertThat(course.getTeacher()).isEqualTo(teacher);
    }

    @Test
    void testCourseStudents() {
        // Given
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Student 1");

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Student 2");

        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);

        // When
        course.setStudents(students);

        // Then
        assertThat(course.getStudents()).hasSize(2);
        assertThat(course.getStudents()).contains(student1, student2);
    }

    @Test
    void testCourseTeacherRelationship() {
        // When
        course.setTeacher(teacher);

        // Then
        assertThat(course.getTeacher()).isNotNull();
        assertThat(course.getTeacher().getName()).isEqualTo("Test Teacher");
    }

    @Test
    void testCourseEquality() {
        // Given
        Course course1 = new Course();
        course1.setId(1L);
        course1.setTitle("Course 1");

        Course course2 = new Course();
        course2.setId(1L);
        course2.setTitle("Course 1");

        // Then
        assertThat(course1).isEqualTo(course2);
    }

    @Test
    void testEmptyStudentsList() {
        // Given
        course.setStudents(new ArrayList<>());

        // Then
        assertThat(course.getStudents()).isEmpty();
    }
}
