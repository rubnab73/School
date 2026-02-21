# GitHub Actions Workflows

This directory contains GitHub Actions workflows for the School Management System.

## Available Workflows

### 1. **test.yml** - Simple Test Runner
A straightforward workflow that runs all tests on pull requests and merges.

**Triggers:**
- Pull requests to any branch
- Push/merge to `main` or `master` branches
- Manual workflow dispatch

**Features:**
- Runs all Maven tests
- Generates test reports
- Uploads test results as artifacts
- Comments on pull requests with results

**Usage:**
This workflow runs automatically on PRs and merges. No configuration needed.

---

### 2. **ci.yml** - Full CI/CD Pipeline
A comprehensive CI/CD pipeline with multiple jobs and quality checks.

**Triggers:**
- Pull requests to `main`, `master`, or `develop` branches
- Push/merge to `main` or `master` branches

**Jobs:**
1. **build-and-test** - Compiles code and runs all tests
   - Runs unit tests separately
   - Runs integration tests separately
   - Generates test coverage
   - Archives test results

2. **code-quality** - Performs code quality checks
   - Dependency analysis
   - Security vulnerability scanning

3. **notify** - Notifies about workflow results
   - Checks overall status
   - Fails if any tests failed

**Features:**
- Matrix builds (easily extendable to multiple Java versions)
- Parallel test execution (unit tests, then integration tests)
- Test result artifacts (retained for 14 days)
- Automatic PR comments with results
- Code quality and security checks
- Maven dependency caching for faster builds

---

## Quick Start

### Choose Your Workflow

- **For simple projects:** Use `test.yml`
- **For production projects:** Use `ci.yml` (or use both!)

### Setup Instructions

1. **No additional setup required!** Both workflows are ready to use.

2. **Optional:** Configure branch protection rules in GitHub:
   - Go to Settings → Branches
   - Add rule for `main` branch
   - Require status checks to pass before merging
   - Select the workflow checks you want to require

### Viewing Results

#### In Pull Requests:
- Check the "Checks" tab in your PR
- See detailed test results and logs
- Download test artifacts if needed

#### In Actions Tab:
- Go to the "Actions" tab in your repository
- Click on any workflow run to see details
- View logs for each step
- Download artifacts (test reports, coverage)

---

## Workflow Status Badges

Add these badges to your main README.md to show workflow status:

```markdown
![Tests](https://github.com/YOUR_USERNAME/YOUR_REPO/actions/workflows/test.yml/badge.svg)
![CI/CD](https://github.com/YOUR_USERNAME/YOUR_REPO/actions/workflows/ci.yml/badge.svg)
```

Replace `YOUR_USERNAME` and `YOUR_REPO` with your actual GitHub username and repository name.

---

## Customization

### Adjust Test Commands

Edit the test execution steps in the workflows:

```yaml
# Run specific test categories
- name: Run unit tests
  run: ./mvnw test -Dtest=!*IntegrationTest -B

# Run with custom profiles
- name: Run tests with profile
  run: ./mvnw test -Pci -B
```

### Change Trigger Branches

Modify the `on` section:

```yaml
on:
  pull_request:
    branches: [ "main", "develop", "feature/*" ]
  push:
    branches: [ "main", "staging", "production" ]
```

### Add More Java Versions

Extend the matrix in `ci.yml`:

```yaml
strategy:
  matrix:
    java-version: [17, 21]
```

---

## Troubleshooting

### Issue: "mvnw: Permission denied"
**Solution:** The workflow includes a step to make mvnw executable. If issues persist, ensure mvnw is committed with execute permissions:
```bash
git update-index --chmod=+x mvnw
git commit -m "Make mvnw executable"
```

### Issue: Tests fail in GitHub Actions but pass locally
**Possible causes:**
1. **Database differences:** Workflows use H2 in-memory database. Check `application-test.yaml`.
2. **Environment variables:** Add any required env vars to workflow files.
3. **Timezone issues:** Tests may depend on specific timezones.

**Solution:** Add environment variables to workflow:
```yaml
env:
  TZ: America/New_York
  SPRING_PROFILES_ACTIVE: test
```

### Issue: Workflow takes too long
**Solutions:**
1. Enable Maven caching (already configured)
2. Use parallel test execution
3. Split tests into multiple jobs
4. Reduce test data size

### Issue: Out of memory during tests
**Solution:** Increase Maven memory in workflow:
```yaml
env:
  MAVEN_OPTS: -Xmx1024m
```

---

## Advanced Features

### Running Tests on Schedule

Add scheduled runs to check for dependency issues:

```yaml
on:
  schedule:
    - cron: '0 0 * * 0'  # Run every Sunday at midnight
```

### Deploy After Successful Tests

Add a deploy job to `ci.yml`:

```yaml
deploy:
  name: Deploy to Staging
  runs-on: ubuntu-latest
  needs: build-and-test
  if: github.ref == 'refs/heads/main' && github.event_name == 'push'
  
  steps:
  - name: Deploy
    run: |
      echo "Deploying to staging..."
      # Add your deployment commands here
```

### Code Coverage Reporting

Add JaCoCo coverage to `pom.xml` and update workflow:

```yaml
- name: Generate coverage report
  run: ./mvnw jacoco:report -B

- name: Upload coverage to Codecov
  uses: codecov/codecov-action@v3
  with:
    files: ./target/site/jacoco/jacoco.xml
    fail_ci_if_error: true
```

---

## Best Practices

1. **Keep workflows fast:** Aim for < 5 minutes total execution time
2. **Cache dependencies:** Already configured for Maven
3. **Fail fast:** Use `continue-on-error: false` for critical steps
4. **Archive artifacts:** Keep test results for debugging
5. **Use meaningful names:** Name jobs and steps clearly
6. **Don't commit secrets:** Use GitHub Secrets for sensitive data
7. **Test workflows:** Use `workflow_dispatch` to test changes manually

---

## Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)
- [Spring Boot Testing Guide](https://spring.io/guides/gs/testing-web/)

---

## Support

For issues with workflows:
1. Check the Actions tab for detailed error logs
2. Review this documentation
3. Check the main project README
4. Create an issue in the repository

---

Last Updated: February 21, 2026
