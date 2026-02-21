package com.rubayet.school.repository;

import com.rubayet.school.model.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DepartmentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department testDepartment;

    @BeforeEach
    void setUp() {
        testDepartment = new Department();
        testDepartment.setName("Computer Science");
        entityManager.persist(testDepartment);
        entityManager.flush();
    }

    @Test
    void testFindByName() {
        // When
        Department found = departmentRepository.findByName("Computer Science");

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Computer Science");
    }

    @Test
    void testFindByNameNotFound() {
        // When
        Department found = departmentRepository.findByName("Non-existent Department");

        // Then
        assertThat(found).isNull();
    }

    @Test
    void testFindAll() {
        // When
        List<Department> departments = departmentRepository.findAll();

        // Then
        assertThat(departments).isNotEmpty();
        assertThat(departments).hasSize(1);
        assertThat(departments.get(0).getName()).isEqualTo("Computer Science");
    }

    @Test
    void testSaveDepartment() {
        // Given
        Department newDepartment = new Department();
        newDepartment.setName("Mathematics");

        // When
        Department saved = departmentRepository.save(newDepartment);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Mathematics");
    }

    @Test
    void testDeleteDepartment() {
        // Given
        Long departmentId = testDepartment.getId();

        // When
        departmentRepository.deleteById(departmentId);
        Optional<Department> deleted = departmentRepository.findById(departmentId);

        // Then
        assertThat(deleted).isEmpty();
    }

    @Test
    void testFindById() {
        // When
        Optional<Department> found = departmentRepository.findById(testDepartment.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Computer Science");
    }

    @Test
    void testUpdateDepartment() {
        // Given
        testDepartment.setName("Software Engineering");

        // When
        Department updated = departmentRepository.save(testDepartment);

        // Then
        assertThat(updated.getName()).isEqualTo("Software Engineering");
    }
}
