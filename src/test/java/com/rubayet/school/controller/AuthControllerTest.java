package com.rubayet.school.controller;

import com.rubayet.school.model.Department;
import com.rubayet.school.model.Student;
import com.rubayet.school.model.Teacher;
import com.rubayet.school.model.User;
import com.rubayet.school.repository.DepartmentRepository;
import com.rubayet.school.repository.StudentRepository;
import com.rubayet.school.repository.TeacherRepository;
import com.rubayet.school.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private TeacherRepository teacherRepository;

    @MockBean
    private DepartmentRepository departmentRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private Department testDepartment;
    private User testUser;

    @BeforeEach
    void setUp() {
        testDepartment = new Department();
        testDepartment.setId(1L);
        testDepartment.setName("Computer Science");

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setRole("ROLE_STUDENT");
    }

    @Test
    void testShowSignupForm() throws Exception {
        // Given
        when(departmentRepository.findAll()).thenReturn(Arrays.asList(testDepartment));

        // When/Then
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("departments"));

        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void testRegisterStudent() throws Exception {
        // Given
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(testDepartment));
        when(studentRepository.save(any(Student.class))).thenReturn(new Student());

        // When/Then
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "newstudent")
                        .param("password", "password123")
                        .param("role", "STUDENT")
                        .param("departmentId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userRepository, times(1)).save(any(User.class));
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testRegisterTeacher() throws Exception {
        // Given
        User teacherUser = new User();
        teacherUser.setId(2L);
        teacherUser.setUsername("newteacher");
        teacherUser.setPassword("encodedPassword");
        teacherUser.setRole("ROLE_TEACHER");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(teacherUser);
        when(teacherRepository.save(any(Teacher.class))).thenReturn(new Teacher());

        // When/Then
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "newteacher")
                        .param("password", "password123")
                        .param("role", "TEACHER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userRepository, times(1)).save(any(User.class));
        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }

    @Test
    void testRegisterStudent_WithRolePrefix() throws Exception {
        // Given
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(testDepartment));
        when(studentRepository.save(any(Student.class))).thenReturn(new Student());

        // When/Then
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "newstudent")
                        .param("password", "password123")
                        .param("role", "ROLE_STUDENT")
                        .param("departmentId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterStudent_WithoutDepartment() throws Exception {
        // Given
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(studentRepository.save(any(Student.class))).thenReturn(new Student());

        // When/Then
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "newstudent")
                        .param("password", "password123")
                        .param("role", "STUDENT"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userRepository, times(1)).save(any(User.class));
        verify(studentRepository, times(1)).save(any(Student.class));
    }
}
