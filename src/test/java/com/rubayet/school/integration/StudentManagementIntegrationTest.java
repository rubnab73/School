package com.rubayet.school.integration;

import com.rubayet.school.model.Department;
import com.rubayet.school.model.Student;
import com.rubayet.school.model.User;
import com.rubayet.school.repository.DepartmentRepository;
import com.rubayet.school.repository.StudentRepository;
import com.rubayet.school.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Test 1: Student Management Flow
 * Tests the complete flow of creating, updating, and managing students with departments
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class StudentManagementIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Department testDepartment;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Create a test department
        testDepartment = new Department();
        testDepartment.setName("Computer Science");
        testDepartment = departmentRepository.save(testDepartment);

        // Create a test user
        testUser = new User();
        testUser.setUsername("testadmin");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole("ROLE_ADMIN");
        testUser = userRepository.save(testUser);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCompleteStudentLifecycle() throws Exception {
        // Step 1: Create a new student
        mockMvc.perform(post("/students")
                        .with(csrf())
                        .param("name", "John Doe")
                        .param("email", "john@example.com")
                        .param("departmentId", testDepartment.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        // Verify student was created in the database
        Student createdStudent = studentRepository.findAll().stream()
                .filter(s -> "John Doe".equals(s.getName()))
                .findFirst()
                .orElse(null);

        assertThat(createdStudent).isNotNull();
        assertThat(createdStudent.getEmail()).isEqualTo("john@example.com");
        assertThat(createdStudent.getDepartment().getName()).isEqualTo("Computer Science");

        // Step 2: View all students
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students"))
                .andExpect(model().attributeExists("students"));

        // Step 3: Show edit form for the student
        mockMvc.perform(get("/students/edit/" + createdStudent.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit_student"))
                .andExpect(model().attributeExists("student"))
                .andExpect(model().attribute("student", hasProperty("name", is("John Doe"))));

        // Step 4: Update the student
        mockMvc.perform(post("/students/" + createdStudent.getId())
                        .with(csrf())
                        .param("name", "John Smith")
                        .param("email", "johnsmith@example.com")
                        .param("departmentId", testDepartment.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        // Verify student was updated
        Student updatedStudent = studentRepository.findById(createdStudent.getId()).orElse(null);
        assertThat(updatedStudent).isNotNull();
        assertThat(updatedStudent.getName()).isEqualTo("John Smith");
        assertThat(updatedStudent.getEmail()).isEqualTo("johnsmith@example.com");
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testDeleteStudent() throws Exception {
        // Create a student to delete
        Student studentToDelete = new Student();
        studentToDelete.setName("To Delete");
        studentToDelete.setEmail("delete@example.com");
        studentToDelete.setDepartment(testDepartment);
        studentToDelete = studentRepository.save(studentToDelete);

        Long studentId = studentToDelete.getId();

        // Delete the student
        mockMvc.perform(get("/students/delete/" + studentId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        // Verify student was deleted
        assertThat(studentRepository.findById(studentId)).isEmpty();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testStudentDepartmentRelationship() throws Exception {
        // Create another department
        Department newDepartment = new Department();
        newDepartment.setName("Mathematics");
        newDepartment = departmentRepository.save(newDepartment);

        // Create a student with the first department
        mockMvc.perform(post("/students")
                        .with(csrf())
                        .param("name", "Jane Doe")
                        .param("email", "jane@example.com")
                        .param("departmentId", testDepartment.getId().toString()))
                .andExpect(status().is3xxRedirection());

        Student student = studentRepository.findAll().stream()
                .filter(s -> "Jane Doe".equals(s.getName()))
                .findFirst()
                .orElse(null);

        assertThat(student).isNotNull();
        assertThat(student.getDepartment().getName()).isEqualTo("Computer Science");

        // Update student to new department
        mockMvc.perform(post("/students/" + student.getId())
                        .with(csrf())
                        .param("name", "Jane Doe")
                        .param("email", "jane@example.com")
                        .param("departmentId", newDepartment.getId().toString()))
                .andExpect(status().is3xxRedirection());

        // Verify department was updated
        Student updatedStudent = studentRepository.findById(student.getId()).orElse(null);
        assertThat(updatedStudent).isNotNull();
        assertThat(updatedStudent.getDepartment().getName()).isEqualTo("Mathematics");
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testStudentAccessControl() throws Exception {
        // Students should be able to view students
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk());

        // Students should NOT be able to delete students
        mockMvc.perform(get("/students/delete/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }
}
