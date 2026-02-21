package com.rubayet.school.repository;

import com.rubayet.school.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    // This custom method allows us to find a department just by typing its name (e.g. "CSE")
    Department findByName(String name);
}