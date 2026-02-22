package com.rubayet.school.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    @Test
    void getAndSetId() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        assertEquals(1L, teacher.getId());
    }

    @Test
    void getAndSetName() {
        Teacher teacher = new Teacher();
        teacher.setName("nabil");
        assertEquals("nabil", teacher.getName());
    }

    @Test
    void getAndSetEmail() {
        Teacher teacher = new Teacher();
        teacher.setEmail("nabil@mail.com");
        assertEquals("nabil@mail.com", teacher.getEmail());
    }

    @Test
    void getAndSetUser() {
        Teacher teacher = new Teacher();
        User user = new User();
        teacher.setUser(user);
        assertEquals(user, teacher.getUser());
    }

    @Test
    void getAndSetDepartment() {
        Teacher teacher = new Teacher();
        Department dept = new Department();
        teacher.setDepartment(dept);
        assertEquals(dept, teacher.getDepartment());
    }

    @Test
    void getAndSetCourses() {
        Teacher teacher = new Teacher();
        List<Course> courses = new ArrayList<>();
        teacher.setCourses(courses);
        assertEquals(courses, teacher.getCourses());
    }

    @Test
    void testEquals() {
        Teacher t1 = new Teacher();
        t1.setId(1L);

        Teacher t2 = new Teacher();
        t2.setId(1L);

        assertEquals(t1, t2);
    }

    @Test
    void testHashCode() {
        Teacher t1 = new Teacher();
        t1.setId(1L);

        Teacher t2 = new Teacher();
        t2.setId(1L);

        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void testToString() {
        Teacher teacher = new Teacher();
        teacher.setName("nabil");
        assertNotNull(teacher.toString());
    }
}