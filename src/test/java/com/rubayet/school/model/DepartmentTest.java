package com.rubayet.school.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentTest {

    @Test
    void getAndSetId() {
        Department department = new Department();
        department.setId(1L);
        assertEquals(1L, department.getId());
    }

    @Test
    void getAndSetName() {
        Department department = new Department();
        department.setName("CSE");
        assertEquals("CSE", department.getName());
    }

    @Test
    void getAndSetStudents() {
        Department department = new Department();
        List<Student> students = new ArrayList<>();
        department.setStudents(students);
        assertEquals(students, department.getStudents());
    }

    @Test
    void getAndSetTeachers() {
        Department department = new Department();
        List<Teacher> teachers = new ArrayList<>();
        department.setTeachers(teachers);
        assertEquals(teachers, department.getTeachers());
    }

    @Test
    void testEquals() {
        Department d1 = new Department();
        d1.setId(1L);

        Department d2 = new Department();
        d2.setId(1L);

        assertEquals(d1, d2);
    }

    @Test
    void testHashCode() {
        Department d1 = new Department();
        d1.setId(1L);

        Department d2 = new Department();
        d2.setId(1L);

        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void testToString() {
        Department department = new Department();
        department.setName("CSE");
        assertNotNull(department.toString());
    }
}