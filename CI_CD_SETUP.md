# GitHub Actions CI/CD Pipeline Documentation

## 📋 Overview

This project uses GitHub Actions to automate:
- **Building** the application (Maven)
- **Testing** with unit and integration tests
- **Code Quality** analysis (SonarQube, SpotBugs, PMD)
- **Security** scanning (OWASP Dependency Check, Trivy)
- **Docker** image building and pushing
- **Deployment** to various environments
- **Scheduled** checks (dependencies, performance, coverage)

---

## 🚀 Workflows

### 1. **CI Pipeline** (`ci.yml`)
Runs on every push and pull request to `main`, `develop`, and `test2` branches.

**Jobs:**
- `build`: Compile, run tests, build JAR
- `code-quality`: SonarQube, SpotBugs, PMD analysis
- `security-scan`: Dependency Check, Trivy vulnerability scanning
- `docker-build`: Build and push Docker image (optional)
- `notify`: Post results to PR comments

**Triggers:**
```yaml
- Push to main, develop, test2 branches
- Pull requests to main, develop, test2 branches
- Weekly schedule (Sunday 2 AM UTC)
```

### 2. **Deployment** (`deploy.yml`)
Deploys successful builds to different environments.

**Jobs:**
- `deploy`: Deploy to environment (development/staging/production)
- `rollback`: Automatic rollback on failure

**Environments:**
- `development`: Automatic on test2 branch
- `staging`: Automatic on develop branch
- `production`: Automatic on main branch (with approval)

### 3. **Scheduled Checks** (`scheduled-checks.yml`)
Runs on schedule for dependency updates, performance tests, and security audits.

**Jobs:**
- `dependency-updates`: Check for Maven and plugin updates
- `performance-tests`: Run performance test suite
- `security-audit`: OWASP Dependency Check
- `code-coverage`: Generate and report code coverage

**Schedule:**
- Weekly on Monday 9 AM UTC (dependency check)
- Daily at midnight UTC (code coverage)

---

## 🔐 Required Secrets

Add these secrets to your repository (Settings → Secrets and variables → Actions):

| Secret | Description | Required |
|--------|-------------|----------|
| `SONAR_HOST_URL` | SonarQube instance URL | No* |
| `SONAR_TOKEN` | SonarQube authentication token | No* |
| `DOCKER_USERNAME` | Docker Hub username | No** |
| `DOCKER_PASSWORD` | Docker Hub access token | No** |
| `DEPLOY_PRIVATE_KEY` | SSH private key for deployment | No*** |
| `DEPLOY_HOST` | Deployment server hostname | No*** |
| `DEPLOY_USER` | Deployment server username | No*** |
| `SLACK_WEBHOOK` | Slack webhook URL for notifications | No |

**Notes:**
- *Required only if using SonarQube
- **Required only if pushing Docker images
- ***Required only if deploying to external servers

---

## 📦 Environment Variables

### Build Environment Variables
```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/schooldb
SPRING_DATASOURCE_USERNAME=schooluser
SPRING_DATASOURCE_PASSWORD=test
```

### Application Configuration
See `src/main/resources/application.yaml` for application properties.

---

## 🛠️ Local Setup

### Prerequisites
- Java 21
- Maven 3.9+
- Docker & Docker Compose

### Setup Steps

1. **Initialize environment:**
   ```bash
   # Linux/macOS
   bash scripts/setup.sh
   
   # Windows
   scripts\setup.bat
   ```

2. **Start PostgreSQL:**
   ```bash
   docker-compose up -d postgres
   ```

3. **Build the project:**
   ```bash
   mvn clean install
   ```

4. **Run locally:**
   ```bash
   mvn spring-boot:run
   ```

---

## 🔍 Local CI Checks

Run the following before pushing to catch issues early:

```bash
# Linux/macOS
bash scripts/ci-local.sh

# Windows
scripts\ci-local.bat
```

This script runs:
- Maven clean
- Compilation
- Unit tests
- SpotBugs analysis
- PMD code analysis
- Checkstyle validation
- JAR build

---

## 📊 Code Quality Tools

### JaCoCo (Code Coverage)
- Reports: `target/site/jacoco/`
- Minimum coverage: Monitor via SonarQube
- Run: `mvn jacoco:report`

### SonarQube
- Quality gates and metrics
- Security vulnerabilities
- Code smells and technical debt
- Configuration: `.github/sonar.properties`

### SpotBugs
- Bug and defect detection
- Exclude rules: `spotbugs-exclude.xml`
- Run: `mvn spotbugs:check`

### PMD
- Code analysis and style checking
- Performance and best practices
- Duplicate code detection
- Run: `mvn pmd:check`

### Checkstyle
- Code style enforcement
- Configuration: Google checkstyle rules
- Run: `mvn checkstyle:check`

---

## 🐳 Docker

### Dockerfile
Multi-stage build creates optimized production image:
- Builder stage: Builds application with Maven
- Runtime stage: Eclipse Temurin 21 JRE Alpine (minimal)

### Build Docker Image Locally
```bash
docker build -t school-app:latest .
```

### Run Docker Container
```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/schooldb \
  -e SPRING_DATASOURCE_USERNAME=schooluser \
  -e SPRING_DATASOURCE_PASSWORD=test \
  school-app:latest
```

---

## 🔄 Dependabot

Configuration: `.github/dependabot.yml`

**Features:**
- Weekly dependency update checks
- Automatic pull requests for updates
- Separate PRs for Maven and GitHub Actions
- Commits prefixed with `chore(deps):` or `ci(deps):`

---

## 📋 Pull Request Template

`.github/pull_request_template.md` provides:
- Description section
- Issue linking
- Type of change checkbox
- Verification checklist
- Testing notes
- Security considerations

---

## 🏷️ Issue Templates

### Bug Report (`bug_report.yml`)
- Description and steps to reproduce
- Expected vs actual behavior
- Environment details
- Logs and error messages

### Feature Request (`feature_request.yml`)
- Problem statement
- Proposed solution
- Alternative approaches
- Additional context

---

## 📧 Notifications

### GitHub Notifications
- PR comments with test results
- Workflow run status
- Deployment status

### Slack Notifications (Optional)
Configure `SLACK_WEBHOOK` secret for:
- Build failures
- Deployment completions
- Rollback alerts

---

## 🔄 Git Workflow

### Recommended Branch Strategy

```
main (production)
├── v1.0.0 (tags)
develop (staging)
├── feature/...
├── bugfix/...
test2 (development)
```

### Commit Convention
```
type(scope): subject

<body>

<footer>
```

**Types:** feat, fix, docs, style, refactor, test, chore, ci

---

## 🚢 Deployment Process

### Manual Deployment
```bash
# Trigger workflow manually with specific environment
gh workflow run deploy.yml -f environment=staging
```

### Automatic Deployments
- `test2` → Development environment (auto)
- `develop` → Staging environment (auto)
- `main` → Production environment (requires approval)

---

## 📈 Monitoring

### Coverage Reports
- JaCoCo: `target/site/jacoco/index.html`
- Codecov: GitHub Actions integration

### Security Reports
- OWASP: `dependency-check-report.json`
- Trivy: `trivy-results.sarif`

### Code Quality
- SonarQube dashboard
- GitHub Security tab (for SARIF reports)

---

## 🐛 Troubleshooting

### Workflow Fails to Run
1. Check branch protection rules
2. Verify secrets are configured
3. Check workflow file syntax: `actions/setup-java@v4` syntax

### Tests Fail in CI but Pass Locally
1. Database connection issues → Check PostgreSQL service
2. Environment variables → Compare CI vs local
3. Port conflicts → Check available ports

### Docker Build Fails
1. Check Docker Hub authentication
2. Verify Dockerfile syntax
3. Ensure base images are available

### SonarQube Analysis Skipped
1. Check `SONAR_TOKEN` and `SONAR_HOST_URL`
2. Verify SonarQube instance is accessible
3. Check `sonar-project.properties`

---

## 📚 Additional Resources

- [GitHub Actions Documentation](https://docs.github.com/actions)
- [Maven CI/CD Best Practices](https://maven.apache.org/)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [SonarQube Quality Gates](https://docs.sonarqube.org/latest/user-guide/quality-gates/)

---

## ✅ Checklist for Setup Completion

- [ ] Clone repository with test2 branch
- [ ] Add GitHub secrets (optional ones as needed)
- [ ] Update `.github/CODEOWNERS` with your username
- [ ] Configure SonarQube (if using)
- [ ] Configure Docker Hub (if using)
- [ ] Test locally with `scripts/ci-local.sh`
- [ ] Create a test PR to verify workflows
- [ ] Review workflow runs and artifacts
- [ ] Document environment-specific configs
- [ ] Set up branch protection rules

---

**Last Updated:** February 2026
**Maintainer:** Your Name/Team
**Support:** Open an issue for questions or improvements
