package com.rubayet.school.repository;

import com.rubayet.school.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    // This gives us standard database methods like save(), findAll(), etc.
    Teacher findByUser(com.rubayet.school.model.User user);
}