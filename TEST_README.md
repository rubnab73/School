# Test Suite Documentation

This document provides an overview of all the test files created for the School Management System.

## Test Structure

The test suite is organized into the following categories:

### 1. **Model Tests** (Unit Tests)
Tests for entity/model classes to verify their basic functionality:
- `UserTest.java` - Tests for User entity
- `StudentTest.java` - Tests for Student entity
- `TeacherTest.java` - Tests for Teacher entity
- `CourseTest.java` - Tests for Course entity
- `DepartmentTest.java` - Tests for Department entity

### 2. **Repository Tests** (Data Layer Tests)
Tests for JPA repositories using `@DataJpaTest`:
- `UserRepositoryTest.java` - Tests for UserRepository (findByUsername, CRUD operations)
- `StudentRepositoryTest.java` - Tests for StudentRepository (findByUser, CRUD operations)
- `TeacherRepositoryTest.java` - Tests for TeacherRepository (findByUser, CRUD operations)
- `CourseRepositoryTest.java` - Tests for CourseRepository (CRUD operations)
- `DepartmentRepositoryTest.java` - Tests for DepartmentRepository (findByName, CRUD operations)

### 3. **Service Tests** (Business Logic Tests)
Tests for service layer using Mockito:
- `CustomUserDetailsServiceTest.java` - Tests for user authentication service
  - Tests user loading by username
  - Tests handling of missing users
  - Tests role handling with and without ROLE_ prefix

### 4. **Controller Tests** (Web Layer Tests)
Tests for controllers using `@WebMvcTest` and MockMvc:
- `StudentControllerTest.java` - Tests for student management endpoints
- `CourseControllerTest.java` - Tests for course management and enrollment
- `DepartmentControllerTest.java` - Tests for department management
- `AuthControllerTest.java` - Tests for user registration
- `HomeControllerTest.java` - Tests for home page
- `LoginControllerTest.java` - Tests for login page

### 5. **Integration Tests** (End-to-End Tests)
Full integration tests using `@SpringBootTest`:

#### **StudentManagementIntegrationTest.java**
Tests the complete student management workflow:
- Creating students with departments
- Viewing all students
- Editing student information
- Updating student department assignments
- Deleting students
- Role-based access control for students

#### **CourseEnrollmentIntegrationTest.java**
Tests the complete course enrollment workflow:
- Teachers creating courses
- Students viewing available courses
- Students enrolling in courses
- Students unenrolling from courses
- Multiple course enrollments
- Role-based access (teachers vs students)

### 6. **Configuration Tests**
- `SecurityConfigTest.java` - Tests for security configuration
  - Password encoder functionality
  - BCrypt password hashing

### 7. **Application Tests**
- `SchoolApplicationTestSuite.java` - Tests that the application context loads successfully

## Test Statistics

### Total Test Files Created: **22 files**

Breakdown by category:
- Model Tests: 5 files
- Repository Tests: 5 files
- Service Tests: 1 file
- Controller Tests: 6 files
- Integration Tests: 2 files (with multiple test methods each)
- Configuration Tests: 1 file
- Application Tests: 2 files (including existing SchoolApplicationTests.java)

### Estimated Total Test Methods: **100+ test methods**

## Running the Tests

### Run All Tests
```bash
./mvnw test
```

### Run Specific Test Class
```bash
./mvnw test -Dtest=StudentRepositoryTest
```

### Run Integration Tests Only
```bash
./mvnw test -Dtest=*IntegrationTest
```

### Run Unit Tests Only
```bash
./mvnw test -Dtest=*Test -Dtest=!*IntegrationTest
```

### Run Tests with Coverage (if coverage plugin is configured)
```bash
./mvnw clean test jacoco:report
```

## Test Configuration

### Test Dependencies Added to pom.xml:
- `spring-boot-starter-test` - Core Spring Boot testing support
- `spring-boot-starter-security-test` - Security testing utilities
- `h2` - In-memory database for testing
- `mockito-core` - Mocking framework
- `junit-jupiter-api` and `junit-jupiter-engine` - JUnit 5 testing framework

### Test Application Properties:
Created `application-test.yaml` in `src/test/resources/` with:
- H2 in-memory database configuration
- Test-specific JPA settings
- Disabled H2 console for tests

## Key Testing Features

### 1. Security Testing
- Tests use `@WithMockUser` to simulate authenticated users
- Tests verify role-based access control (STUDENT, TEACHER, ADMIN)
- CSRF protection is properly tested using `.with(csrf())`

### 2. Database Testing
- Repository tests use `@DataJpaTest` for fast, isolated testing
- Integration tests use `@Transactional` to rollback changes after each test
- H2 in-memory database is used for test isolation

### 3. MockMvc Testing
- Controller tests use MockMvc to simulate HTTP requests
- Tests verify view names, model attributes, and redirects
- Tests check HTTP status codes and response content

### 4. Mocking
- Service tests use Mockito to mock dependencies
- Tests verify correct method calls and interactions
- Tests use `when()...thenReturn()` for stubbing behavior

## Test Coverage Areas

### Functional Coverage:
✅ User registration (Student and Teacher)
✅ Student CRUD operations
✅ Course CRUD operations
✅ Department CRUD operations
✅ Course enrollment/unenrollment
✅ User authentication and authorization
✅ Role-based access control
✅ Department-Student relationships
✅ Department-Teacher relationships
✅ Course-Teacher relationships
✅ Course-Student many-to-many relationships
✅ Password encryption

### Edge Cases Tested:
✅ Non-existent entity handling
✅ Empty collections
✅ Null checks
✅ Role prefix handling (ROLE_ vs no prefix)
✅ Department deletion with dependent students
✅ Access denied scenarios
✅ Multiple enrollments
✅ Toggle enrollment (enroll/unenroll)

## Running Tests in IDE

### IntelliJ IDEA
1. Right-click on the `test` folder or any test class
2. Select "Run Tests" or "Run [ClassName]"
3. View test results in the Run window

### Eclipse
1. Right-click on the `test` folder or any test class
2. Select "Run As" → "JUnit Test"
3. View test results in the JUnit window

### VS Code
1. Install "Test Runner for Java" extension
2. Click the test icon in the left sidebar
3. Run individual tests or entire test suites

## Continuous Integration

These tests are designed to run in CI/CD pipelines:
- Fast execution with H2 in-memory database
- No external dependencies required
- Isolated and independent test execution
- Transactional rollback ensures clean state

## Best Practices Implemented

1. **AAA Pattern** - Arrange, Act, Assert structure in tests
2. **Test Isolation** - Each test is independent and can run in any order
3. **Descriptive Names** - Test method names clearly describe what is being tested
4. **Given-When-Then** - Clear test structure with comments
5. **Mock Verification** - Verify that mocked methods are called correctly
6. **Test Data Builders** - setUp methods create reusable test data
7. **Active Profiles** - Tests use `@ActiveProfiles("test")` for test-specific configuration

## Future Enhancements

Consider adding:
- Performance tests for database queries
- API documentation tests (SpringDoc/Swagger)
- End-to-end UI tests (Selenium/TestContainers)
- Code coverage reports (JaCoCo)
- Mutation testing (PIT)
- Load/stress testing
- Security vulnerability scanning

## Troubleshooting

### Common Issues:

**Issue**: Tests fail with "Unable to find a @SpringBootConfiguration"
**Solution**: Ensure test classes are in the same package or sub-package as `SchoolApplication.java`

**Issue**: Security tests fail with 401/403 errors
**Solution**: Add `@WithMockUser` annotation with appropriate roles

**Issue**: Database-related tests fail
**Solution**: Check that H2 dependency is in classpath and `application-test.yaml` is configured

**Issue**: Integration tests are slow
**Solution**: Consider using `@DataJpaTest` instead of `@SpringBootTest` where possible

## Contact & Support

For questions or issues with the test suite, please refer to the main application documentation or contact the development team.
