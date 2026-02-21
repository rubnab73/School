package com.rubayet.school.repository;

import com.rubayet.school.model.Course;
import com.rubayet.school.model.Teacher;
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
class CourseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    private Teacher testTeacher;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        // Create a test user for the teacher
        User testUser = new User();
        testUser.setUsername("teacheruser");
        testUser.setPassword("password123");
        testUser.setRole("ROLE_TEACHER");
        entityManager.persist(testUser);

        // Create a test teacher
        testTeacher = new Teacher();
        testTeacher.setName("Test Teacher");
        testTeacher.setEmail("teacher@example.com");
        testTeacher.setUser(testUser);
        entityManager.persist(testTeacher);

        // Create a test course
        testCourse = new Course();
        testCourse.setTitle("Introduction to Programming");
        testCourse.setDescription("Learn the basics of programming");
        testCourse.setTeacher(testTeacher);
        entityManager.persist(testCourse);

        entityManager.flush();
    }

    @Test
    void testFindAll() {
        // When
        List<Course> courses = courseRepository.findAll();

        // Then
        assertThat(courses).isNotEmpty();
        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getTitle()).isEqualTo("Introduction to Programming");
    }

    @Test
    void testSaveCourse() {
        // Given
        Course newCourse = new Course();
        newCourse.setTitle("Advanced Java");
        newCourse.setDescription("Learn advanced Java concepts");
        newCourse.setTeacher(testTeacher);

        // When
        Course saved = courseRepository.save(newCourse);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Advanced Java");
        assertThat(saved.getTeacher()).isEqualTo(testTeacher);
    }

    @Test
    void testFindById() {
        // When
        Optional<Course> found = courseRepository.findById(testCourse.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Introduction to Programming");
        assertThat(found.get().getDescription()).isEqualTo("Learn the basics of programming");
    }

    @Test
    void testDeleteCourse() {
        // Given
        Long courseId = testCourse.getId();

        // When
        courseRepository.deleteById(courseId);
        Optional<Course> deleted = courseRepository.findById(courseId);

        // Then
        assertThat(deleted).isEmpty();
    }

    @Test
    void testUpdateCourse() {
        // Given
        testCourse.setTitle("Updated Course Title");
        testCourse.setDescription("Updated description");

        // When
        Course updated = courseRepository.save(testCourse);

        // Then
        assertThat(updated.getTitle()).isEqualTo("Updated Course Title");
        assertThat(updated.getDescription()).isEqualTo("Updated description");
    }
}
