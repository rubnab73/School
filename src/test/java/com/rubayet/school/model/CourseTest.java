package com.rubayet.school.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void getAndSetId() {
        Course course = new Course();
        course.setId(1L);
        assertEquals(1L, course.getId());
    }

    @Test
    void getAndSetTitle() {
        Course course = new Course();
        course.setTitle("Math");
        assertEquals("Math", course.getTitle());
    }

    @Test
    void getAndSetDescription() {
        Course course = new Course();
        course.setDescription("Basic course");
        assertEquals("Basic course", course.getDescription());
    }

    @Test
    void getAndSetTeacher() {
        Course course = new Course();
        Teacher teacher = new Teacher();
        course.setTeacher(teacher);
        assertEquals(teacher, course.getTeacher());
    }

    @Test
    void getAndSetStudents() {
        Course course = new Course();
        List<Student> students = new ArrayList<>();
        course.setStudents(students);
        assertEquals(students, course.getStudents());
    }

    @Test
    void testEquals() {
        Course c1 = new Course();
        c1.setId(1L);

        Course c2 = new Course();
        c2.setId(1L);

        assertEquals(c1, c2);
    }

    @Test
    void testHashCode() {
        Course c1 = new Course();
        c1.setId(1L);

        Course c2 = new Course();
        c2.setId(1L);

        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testToString() {
        Course course = new Course();
        course.setTitle("Math");
        assertNotNull(course.toString());
    }
}