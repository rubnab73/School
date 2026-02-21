package com.rubayet.school.controller;

import com.rubayet.school.model.Department;
import com.rubayet.school.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    // 1. List all departments
    @GetMapping
    public String listDepartments(Model model) {
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments", departments);
        return "departments";
    }

    // Check if department has students
    private boolean hasDependentStudents(Long deptId) {
        Department dept = departmentRepository.findById(deptId).orElse(null);
        return dept != null && dept.getStudents() != null && !dept.getStudents().isEmpty();
    }

    // 2. Show the "Add Department" Form
    @GetMapping("/new")
    public String createDepartmentForm(Model model) {
        model.addAttribute("department", new Department());
        return "create_department";
    }

    // 3. Save the Department
    @PostMapping
    public String saveDepartment(@ModelAttribute("department") Department department) {
        departmentRepository.save(department);
        return "redirect:/departments";
    }

    // 4. Delete a Department
    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id, Model model) {
        // Check if department has students
        if (hasDependentStudents(id)) {
            List<Department> departments = departmentRepository.findAll();
            model.addAttribute("departments", departments);
            model.addAttribute("error", "Cannot delete department! It has students assigned. Please reassign students first.");
            return "departments";
        }
        
        departmentRepository.deleteById(id);
        return "redirect:/departments";
    }
}
