package com.rubayet.school.controller;

import com.rubayet.school.model.Department;
import com.rubayet.school.repository.DepartmentRepository;
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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentRepository departmentRepository;

    private Department testDepartment;

    @BeforeEach
    void setUp() {
        testDepartment = new Department();
        testDepartment.setId(1L);
        testDepartment.setName("Computer Science");
        testDepartment.setStudents(new ArrayList<>());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testListDepartments() throws Exception {
        // Given
        List<Department> departments = Arrays.asList(testDepartment);
        when(departmentRepository.findAll()).thenReturn(departments);

        // When/Then
        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(view().name("departments"))
                .andExpect(model().attributeExists("departments"))
                .andExpect(model().attribute("departments", hasSize(1)));

        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testCreateDepartmentForm() throws Exception {
        // When/Then
        mockMvc.perform(get("/departments/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_department"))
                .andExpect(model().attributeExists("department"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testSaveDepartment() throws Exception {
        // Given
        when(departmentRepository.save(any(Department.class))).thenReturn(testDepartment);

        // When/Then
        mockMvc.perform(post("/departments")
                        .with(csrf())
                        .param("name", "Computer Science"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/departments"));

        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testDeleteDepartment_WithoutStudents() throws Exception {
        // Given
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(testDepartment));
        doNothing().when(departmentRepository).deleteById(1L);

        // When/Then
        mockMvc.perform(get("/departments/delete/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/departments"));

        verify(departmentRepository, times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testDeleteDepartment_WithStudents() throws Exception {
        // Given
        testDepartment.setStudents(Arrays.asList(new com.rubayet.school.model.Student()));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(testDepartment));
        when(departmentRepository.findAll()).thenReturn(Arrays.asList(testDepartment));

        // When/Then
        mockMvc.perform(get("/departments/delete/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("departments"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", containsString("Cannot delete department")));

        verify(departmentRepository, never()).deleteById(1L);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testAccessDenied_ForStudent() throws Exception {
        // When/Then - Students should not have access to departments page
        mockMvc.perform(get("/departments"))
                .andExpect(status().isForbidden());
    }
}
