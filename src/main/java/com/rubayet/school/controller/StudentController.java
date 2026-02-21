package com.rubayet.school.controller;

import com.rubayet.school.model.Department;
import com.rubayet.school.model.Student;
import com.rubayet.school.repository.DepartmentRepository;
import com.rubayet.school.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository; // <--- NEW REPOSITORY INJECTED

    // 1. List all students
    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "students";
    }

    // 2. Show the "Add Student" Form
    @GetMapping("/new")
    public String createStudentForm(Model model) {
        Student student = new Student();
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("student", student);
        model.addAttribute("departments", departments);
        return "create_student";
    }

    // 3. Save the Student (UPDATED METHOD)
    @PostMapping
    public String saveStudent(@ModelAttribute("student") Student student,
                              @RequestParam("departmentId") Long departmentId) {

        // Fetch the department by ID
        Department department = departmentRepository.findById(departmentId).orElse(null);

        if (department != null) {
            student.setDepartment(department);
            studentRepository.save(student);
        }

        return "redirect:/students";
    }

    // 4. Delete a Student
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
        return "redirect:/students";
    }

    // 5. Show Edit Form (GET)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            List<Department> departments = departmentRepository.findAll();
            model.addAttribute("student", student);
            model.addAttribute("departments", departments);
            return "edit_student"; // We will create this file next!
        }
        return "redirect:/students";
    }

    // 6. Update Student (POST)
    @PostMapping("/{id}")
    public String updateStudent(@PathVariable Long id,
                                @ModelAttribute("student") Student student,
                                @RequestParam("departmentId") Long departmentId) {

        // Find existing student
        Student existingStudent = studentRepository.findById(id).orElse(null);

        if (existingStudent != null) {
            // Update basic fields
            existingStudent.setName(student.getName());
            existingStudent.setEmail(student.getEmail());

            // Fetch and set department
            Department department = departmentRepository.findById(departmentId).orElse(null);
            if (department != null) {
                existingStudent.setDepartment(department);
            }

            // Save updated student
            studentRepository.save(existingStudent);
        }

        return "redirect:/students";
    }
}