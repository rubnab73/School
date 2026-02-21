package com.rubayet.school.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DepartmentTest {

    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
    }

    @Test
    void testDepartmentCreation() {
        // When
        department.setId(1L);
        department.setName("Computer Science");

        // Then
        assertThat(department.getId()).isEqualTo(1L);
        assertThat(department.getName()).isEqualTo("Computer Science");
    }

    @Test
    void testDepartmentStudents() {
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
        department.setStudents(students);

        // Then
        assertThat(department.getStudents()).hasSize(2);
        assertThat(department.getStudents()).contains(student1, student2);
    }

    @Test
    void testDepartmentTeachers() {
        // Given
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setName("Teacher 1");

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setName("Teacher 2");

        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher1);
        teachers.add(teacher2);

        // When
        department.setTeachers(teachers);

        // Then
        assertThat(department.getTeachers()).hasSize(2);
        assertThat(department.getTeachers()).contains(teacher1, teacher2);
    }

    @Test
    void testDepartmentEquality() {
        // Given
        Department dept1 = new Department();
        dept1.setId(1L);
        dept1.setName("CS");

        Department dept2 = new Department();
        dept2.setId(1L);
        dept2.setName("CS");

        // Then
        assertThat(dept1).isEqualTo(dept2);
    }

    @Test
    void testEmptyDepartment() {
        // Given
        department.setStudents(new ArrayList<>());
        department.setTeachers(new ArrayList<>());

        // Then
        assertThat(department.getStudents()).isEmpty();
        assertThat(department.getTeachers()).isEmpty();
    }
}
