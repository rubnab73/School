package com.rubayet.school.controller;

import com.rubayet.school.model.Course;
import com.rubayet.school.model.Student;
import com.rubayet.school.model.Teacher;
import com.rubayet.school.model.User;
import com.rubayet.school.repository.CourseRepository;
import com.rubayet.school.repository.StudentRepository;
import com.rubayet.school.repository.TeacherRepository;
import com.rubayet.school.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TeacherRepository teacherRepository;

    @MockBean
    private StudentRepository studentRepository;

    private Course testCourse;
    private Teacher testTeacher;
    private Student testStudent;
    private User teacherUser;
    private User studentUser;

    @BeforeEach
    void setUp() {
        teacherUser = new User();
        teacherUser.setId(1L);
        teacherUser.setUsername("teacher");
        teacherUser.setPassword("password");
        teacherUser.setRole("ROLE_TEACHER");

        studentUser = new User();
        studentUser.setId(2L);
        studentUser.setUsername("student");
        studentUser.setPassword("password");
        studentUser.setRole("ROLE_STUDENT");

        testTeacher = new Teacher();
        testTeacher.setId(1L);
        testTeacher.setName("Test Teacher");
        testTeacher.setUser(teacherUser);

        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setName("Test Student");
        testStudent.setUser(studentUser);
        testStudent.setCourses(new ArrayList<>());

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Test Course");
        testCourse.setDescription("Test Description");
        testCourse.setTeacher(testTeacher);
    }

    @Test
    @WithMockUser(username = "teacher", roles = "TEACHER")
    void testListCourses_AsTeacher() throws Exception {
        // Given
        List<Course> courses = Arrays.asList(testCourse);
        when(courseRepository.findAll()).thenReturn(courses);
        when(userRepository.findByUsername("teacher")).thenReturn(teacherUser);

        // When/Then
        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attribute("courses", hasSize(1)));

        verify(courseRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "student", roles = "STUDENT")
    void testListCourses_AsStudent() throws Exception {
        // Given
        List<Course> courses = Arrays.asList(testCourse);
        when(courseRepository.findAll()).thenReturn(courses);
        when(userRepository.findByUsername("student")).thenReturn(studentUser);
        when(studentRepository.findByUser(studentUser)).thenReturn(testStudent);

        // When/Then
        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attributeExists("student"));

        verify(courseRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testShowCreateForm() throws Exception {
        // When/Then
        mockMvc.perform(get("/courses/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_course"))
                .andExpect(model().attributeExists("course"));
    }

    @Test
    @WithMockUser(username = "teacher", roles = "TEACHER")
    void testSaveCourse() throws Exception {
        // Given
        when(userRepository.findByUsername("teacher")).thenReturn(teacherUser);
        when(teacherRepository.findByUser(teacherUser)).thenReturn(testTeacher);
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        // When/Then
        mockMvc.perform(post("/courses/save")
                        .with(csrf())
                        .param("title", "New Course")
                        .param("description", "New Description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @WithMockUser(username = "student", roles = "STUDENT")
    void testToggleEnrollment_Enroll() throws Exception {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(userRepository.findByUsername("student")).thenReturn(studentUser);
        when(studentRepository.findByUser(studentUser)).thenReturn(testStudent);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // When/Then
        mockMvc.perform(post("/courses/enroll/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));

        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @WithMockUser(username = "student", roles = "STUDENT")
    void testToggleEnrollment_Unenroll() throws Exception {
        // Given - Student already enrolled
        testStudent.getCourses().add(testCourse);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(userRepository.findByUsername("student")).thenReturn(studentUser);
        when(studentRepository.findByUser(studentUser)).thenReturn(testStudent);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // When/Then
        mockMvc.perform(post("/courses/enroll/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));

        verify(studentRepository, times(1)).save(any(Student.class));
    }
}
