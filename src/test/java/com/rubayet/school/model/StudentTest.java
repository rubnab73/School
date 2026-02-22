package com.rubayet.school.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void getAndSetId() {
        Student student = new Student();
        student.setId(1L);
        assertEquals(1L, student.getId());
    }

    @Test
    void getAndSetName() {
        Student student = new Student();
        student.setName("nabil");
        assertEquals("nabil", student.getName());
    }

    @Test
    void getAndSetEmail() {
        Student student = new Student();
        student.setEmail("nabil@mail.com");
        assertEquals("nabil@mail.com", student.getEmail());
    }

    @Test
    void getAndSetUser() {
        Student student = new Student();
        User user = new User();
        student.setUser(user);
        assertEquals(user, student.getUser());
    }

    @Test
    void getAndSetDepartment() {
        Student student = new Student();
        Department dept = new Department();
        student.setDepartment(dept);
        assertEquals(dept, student.getDepartment());
    }

    @Test
    void getAndSetCourses() {
        Student student = new Student();
        List<Course> courses = new ArrayList<>();
        student.setCourses(courses);
        assertEquals(courses, student.getCourses());
    }

    @Test
    void testEquals() {
        Student s1 = new Student();
        s1.setId(1L);

        Student s2 = new Student();
        s2.setId(1L);

        assertEquals(s1, s2);
    }

    @Test
    void testHashCode() {
        Student s1 = new Student();
        s1.setId(1L);

        Student s2 = new Student();
        s2.setId(1L);

        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testToString() {
        Student student = new Student();
        student.setName("nabil");
        assertNotNull(student.toString());
    }
}