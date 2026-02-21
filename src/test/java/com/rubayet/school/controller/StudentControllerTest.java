package com.rubayet.school.controller;

import com.rubayet.school.model.Department;
import com.rubayet.school.model.Student;
import com.rubayet.school.repository.DepartmentRepository;
import com.rubayet.school.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private DepartmentRepository departmentRepository;

    private Student testStudent;
    private Department testDepartment;

    @BeforeEach
    void setUp() {
        testDepartment = new Department();
        testDepartment.setId(1L);
        testDepartment.setName("Computer Science");

        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setName("Test Student");
        testStudent.setEmail("test@example.com");
        testStudent.setDepartment(testDepartment);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testListStudents() throws Exception {
        // Given
        List<Student> students = Arrays.asList(testStudent);
        when(studentRepository.findAll()).thenReturn(students);

        // When/Then
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attribute("students", hasSize(1)));

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateStudentForm() throws Exception {
        // Given
        List<Department> departments = Arrays.asList(testDepartment);
        when(departmentRepository.findAll()).thenReturn(departments);

        // When/Then
        mockMvc.perform(get("/students/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_student"))
                .andExpect(model().attributeExists("student"))
                .andExpect(model().attributeExists("departments"));

        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSaveStudent() throws Exception {
        // Given
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(testDepartment));
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // When/Then
        mockMvc.perform(post("/students")
                        .with(csrf())
                        .param("name", "New Student")
                        .param("email", "new@example.com")
                        .param("departmentId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testDeleteStudent() throws Exception {
        // Given
        doNothing().when(studentRepository).deleteById(1L);

        // When/Then
        mockMvc.perform(get("/students/delete/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testShowEditForm() throws Exception {
        // Given
        List<Department> departments = Arrays.asList(testDepartment);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(departmentRepository.findAll()).thenReturn(departments);

        // When/Then
        mockMvc.perform(get("/students/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit_student"))
                .andExpect(model().attributeExists("student"))
                .andExpect(model().attributeExists("departments"));

        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testShowEditForm_StudentNotFound() throws Exception {
        // Given
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/students/edit/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testUpdateStudent() throws Exception {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(testDepartment));
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // When/Then
        mockMvc.perform(post("/students/1")
                        .with(csrf())
                        .param("name", "Updated Student")
                        .param("email", "updated@example.com")
                        .param("departmentId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        verify(studentRepository, times(1)).save(any(Student.class));
    }
}
