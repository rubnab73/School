package com.rubayet.school.repository;

import com.rubayet.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // This gives you ready-made methods:
    // .save(), .findAll(), .deleteById(), etc.
    Student findByUser(com.rubayet.school.model.User user);
}