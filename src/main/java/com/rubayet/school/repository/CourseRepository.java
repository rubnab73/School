package com.rubayet.school.repository;

import com.rubayet.school.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    // We can add custom queries later if needed
}