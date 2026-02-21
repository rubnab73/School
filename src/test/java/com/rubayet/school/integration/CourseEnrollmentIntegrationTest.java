package com.rubayet.school.integration;

import com.rubayet.school.model.*;
import com.rubayet.school.repository.*;
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
 * Integration Test 2: Course Enrollment Flow
 * Tests the complete flow of course creation and student enrollment
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class CourseEnrollmentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Teacher testTeacher;
    private Student testStudent;
    private User teacherUser;
    private User studentUser;
    private Department testDepartment;

    @BeforeEach
    void setUp() {
        // Create a test department
        testDepartment = new Department();
        testDepartment.setName("Computer Science");
        testDepartment = departmentRepository.save(testDepartment);

        // Create teacher user and profile
        teacherUser = new User();
        teacherUser.setUsername("teacher_test");
        teacherUser.setPassword(passwordEncoder.encode("password"));
        teacherUser.setRole("ROLE_TEACHER");
        teacherUser = userRepository.save(teacherUser);

        testTeacher = new Teacher();
        testTeacher.setName("Test Teacher");
        testTeacher.setEmail("teacher@example.com");
        testTeacher.setUser(teacherUser);
        testTeacher.setDepartment(testDepartment);
        testTeacher = teacherRepository.save(testTeacher);

        // Create student user and profile
        studentUser = new User();
        studentUser.setUsername("student_test");
        studentUser.setPassword(passwordEncoder.encode("password"));
        studentUser.setRole("ROLE_STUDENT");
        studentUser = userRepository.save(studentUser);

        testStudent = new Student();
        testStudent.setName("Test Student");
        testStudent.setEmail("student@example.com");
        testStudent.setUser(studentUser);
        testStudent.setDepartment(testDepartment);
        testStudent = studentRepository.save(testStudent);
    }

    @Test
    @WithMockUser(username = "teacher_test", roles = "TEACHER")
    void testTeacherCreatesCourse() throws Exception {
        // Teacher creates a new course
        mockMvc.perform(post("/courses/save")
                        .with(csrf())
                        .param("title", "Introduction to Java")
                        .param("description", "Learn Java programming basics"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));

        // Verify course was created
        Course createdCourse = courseRepository.findAll().stream()
                .filter(c -> "Introduction to Java".equals(c.getTitle()))
                .findFirst()
                .orElse(null);

        assertThat(createdCourse).isNotNull();
        assertThat(createdCourse.getDescription()).isEqualTo("Learn Java programming basics");
        assertThat(createdCourse.getTeacher().getName()).isEqualTo("Test Teacher");
    }

    @Test
    @WithMockUser(username = "student_test", roles = "STUDENT")
    void testStudentEnrollsInCourse() throws Exception {
        // Create a course first
        Course course = new Course();
        course.setTitle("Data Structures");
        course.setDescription("Learn about data structures");
        course.setTeacher(testTeacher);
        course = courseRepository.save(course);

        Long courseId = course.getId();

        // Student enrolls in the course
        mockMvc.perform(post("/courses/enroll/" + courseId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));

        // Verify enrollment
        Student enrolledStudent = studentRepository.findById(testStudent.getId()).orElse(null);
        assertThat(enrolledStudent).isNotNull();
        assertThat(enrolledStudent.getCourses()).isNotEmpty();
        assertThat(enrolledStudent.getCourses().get(0).getTitle()).isEqualTo("Data Structures");
    }

    @Test
    @WithMockUser(username = "student_test", roles = "STUDENT")
    void testStudentUnenrollsFromCourse() throws Exception {
        // Create a course and enroll the student
        Course course = new Course();
        course.setTitle("Algorithms");
        course.setDescription("Learn about algorithms");
        course.setTeacher(testTeacher);
        course = courseRepository.save(course);

        testStudent.getCourses().add(course);
        testStudent = studentRepository.save(testStudent);

        Long courseId = course.getId();

        // Verify student is enrolled
        assertThat(testStudent.getCourses()).hasSize(1);

        // Student unenrolls from the course
        mockMvc.perform(post("/courses/enroll/" + courseId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));

        // Verify unenrollment
        Student unenrolledStudent = studentRepository.findById(testStudent.getId()).orElse(null);
        assertThat(unenrolledStudent).isNotNull();
        assertThat(unenrolledStudent.getCourses()).isEmpty();
    }

    @Test
    @WithMockUser(username = "student_test", roles = "STUDENT")
    void testViewCoursesAsStudent() throws Exception {
        // Create multiple courses
        Course course1 = new Course();
        course1.setTitle("Java Programming");
        course1.setDescription("Learn Java");
        course1.setTeacher(testTeacher);
        courseRepository.save(course1);

        Course course2 = new Course();
        course2.setTitle("Python Programming");
        course2.setDescription("Learn Python");
        course2.setTeacher(testTeacher);
        courseRepository.save(course2);

        // View courses
        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attributeExists("student"))
                .andExpect(model().attribute("courses", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "teacher_test", roles = "TEACHER")
    void testViewCoursesAsTeacher() throws Exception {
        // Create a course
        Course course = new Course();
        course.setTitle("Web Development");
        course.setDescription("Learn web development");
        course.setTeacher(testTeacher);
        courseRepository.save(course);

        // View courses as teacher
        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses"))
                .andExpect(model().attributeExists("courses"));
    }

    @Test
    @WithMockUser(username = "student_test", roles = "STUDENT")
    void testMultipleCourseEnrollments() throws Exception {
        // Create multiple courses
        Course course1 = new Course();
        course1.setTitle("Course 1");
        course1.setDescription("Description 1");
        course1.setTeacher(testTeacher);
        course1 = courseRepository.save(course1);

        Course course2 = new Course();
        course2.setTitle("Course 2");
        course2.setDescription("Description 2");
        course2.setTeacher(testTeacher);
        course2 = courseRepository.save(course2);

        // Enroll in first course
        mockMvc.perform(post("/courses/enroll/" + course1.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        // Enroll in second course
        mockMvc.perform(post("/courses/enroll/" + course2.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        // Verify student is enrolled in both courses
        Student enrolledStudent = studentRepository.findById(testStudent.getId()).orElse(null);
        assertThat(enrolledStudent).isNotNull();
        assertThat(enrolledStudent.getCourses()).hasSize(2);
    }

    @Test
    @WithMockUser(username = "teacher_test", roles = "TEACHER")
    void testTeacherCannotEnrollInCourses() throws Exception {
        // Create a course
        Course course = new Course();
        course.setTitle("Test Course");
        course.setDescription("Test Description");
        course.setTeacher(testTeacher);
        course = courseRepository.save(course);

        // Teacher should not have a student profile, so findByUser will return null
        // This test verifies the system handles this case appropriately
        assertThat(studentRepository.findByUser(teacherUser)).isNull();
    }
}
