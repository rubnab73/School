# GitHub Actions CI/CD Pipeline - Quick Reference

## 📦 What Was Set Up

### Workflows (`.github/workflows/`)
✅ **ci.yml** - Main CI pipeline (build, test, code quality, security)
✅ **deploy.yml** - Deployment pipeline (to dev/staging/prod)
✅ **scheduled-checks.yml** - Scheduled tasks (dependencies, coverage, security)

### Configuration Files
✅ **dependabot.yml** - Automated dependency updates
✅ **CODEOWNERS** - Code review assignment
✅ **pull_request_template.md** - PR template
✅ **sonar.properties** - SonarQube configuration
✅ **spotbugs-exclude.xml** - SpotBugs filter rules

### Issue Templates
✅ **bug_report.yml** - Bug report form
✅ **feature_request.yml** - Feature request form

### Docker
✅ **Dockerfile** - Multi-stage production-ready build

### Build Configuration
✅ **pom.xml** - Added plugins:
   - JaCoCo (code coverage)
   - SonarQube (quality gates)
   - SpotBugs (bug detection)
   - PMD (code analysis)
   - Checkstyle (code style)
   - Surefire (test execution)
   - Versions (dependency updates)

### Helper Scripts
✅ **scripts/setup.sh** - Linux/macOS setup script
✅ **scripts/setup.bat** - Windows setup script
✅ **scripts/ci-local.sh** - Run CI checks locally (Linux/macOS)
✅ **scripts/ci-local.bat** - Run CI checks locally (Windows)

### Documentation
✅ **CI_CD_SETUP.md** - Comprehensive setup and usage guide

---

## 🚀 Quick Start

### 1. Push to GitHub
```bash
git add .
git commit -m "ci: add GitHub Actions CI/CD pipeline"
git push origin test2
```

### 2. Configure Optional Secrets
Go to **Settings → Secrets and variables → Actions** and add:
- `SONAR_HOST_URL` + `SONAR_TOKEN` (for SonarQube)
- `DOCKER_USERNAME` + `DOCKER_PASSWORD` (for Docker Hub)
- `DEPLOY_PRIVATE_KEY` + `DEPLOY_HOST` + `DEPLOY_USER` (for deployments)
- `SLACK_WEBHOOK` (for Slack notifications)

### 3. Create a Test PR
```bash
git checkout -b feature/test-ci
echo "# Test" >> README.md
git add - && git commit -m "test: ci pipeline"
git push origin feature/test-ci
```
Then create a PR on GitHub to see the pipeline in action!

### 4. Run Local Checks
```bash
# Before pushing
bash scripts/ci-local.sh        # Linux/macOS
scripts\ci-local.bat            # Windows
```

---

## 🔄 Workflow Triggers

| Workflow | Triggers | Branches |
|----------|----------|----------|
| **CI** | Push, PR, Schedule | main, develop, test2 |
| **Deploy** | Manual, CI Success | main, develop |
| **Scheduled** | Weekly/Daily | All branches |

---

## 📊 Pipeline Stages

```
Push to Repository
       ↓
CI Pipeline Starts
       ├── Build & Test (4 jobs)
       │   ├── Build (Maven compile, test, package)
       │   ├── Code Quality (SonarQube, SpotBugs, PMD)
       │   ├── Security Scan (Dependency Check, Trivy)
       │   └── Docker Build (optional, push to registry)
       │
       ├── Notifications
       │   ├── Comment on PR
       │   ├── Slack notification
       │   └── GitHub status checks
       │
       └── If main/develop branch
           └── Deployment Pipeline
               ├── Deploy to environment
               ├── Health checks
               ├── Rollback on failure
               └── Notification

```

---

## 🔍 Code Quality Standards

- **SpotBugs**: Medium severity and above
- **PMD**: Common errors and performance
- **Checkstyle**: Google style guide
- **Coverage**: Tracked via JaCoCo
- **Quality Gate**: Enforced via SonarQube

---

## 📱 PR Workflow

1. Create feature branch from `test2`
2. Push code and create pull request
3. **Automated checks run:**
   - ✅ Build & compile
   - ✅ Run all tests
   - ✅ Code quality analysis
   - ✅ Security scanning
   - ✅ Test results posted to PR
4. Review feedback and make changes
5. Merge to trigger deployment

---

## 🐳 Docker Commands

```bash
# Build locally
docker build -t school-app:latest .

# Run with database
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/schooldb \
  -e SPRING_DATASOURCE_USERNAME=schooluser \
  -e SPRING_DATASOURCE_PASSWORD=test \
  school-app:latest

# Tag for Docker Hub
docker tag school-app:latest username/school-app:latest
docker push username/school-app:latest
```

---

## 🔑 Environment Configuration

### Local Development
```bash
# Start database
docker-compose up -d postgres

# Application runs with:
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/schooldb
SPRING_DATASOURCE_USERNAME=schooluser
SPRING_DATASOURCE_PASSWORD=test
```

### CI/Pipeline
```bash
# GitHub Actions environment
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/schooldb
SPRING_DATASOURCE_USERNAME=schooluser
SPRING_DATASOURCE_PASSWORD=test
```

### Production (Configure as needed)
```bash
# Update deploy.yml job environment
# Configure external database credentials
```

---

## 📋 Checklist for Validation

- [ ] All workflow files are in `.github/workflows/`
- [ ] Plugins added to `pom.xml`
- [ ] SpotBugs exclude rules created
- [ ] Helper scripts are executable
- [ ] Local tests pass: `mvn clean test`
- [ ] Local build succeeds: `mvn clean package`
- [ ] Docker image builds: `docker build .`
- [ ] Code can be pushed to repository
- [ ] Workflows appear in **Actions** tab
- [ ] PR shows status checks and results

---

## 🆘 Troubleshooting

**Workflow not running:**
- Check branch name matches workflow trigger
- Verify `.github/workflows/*.yml` exists
- Check YAML syntax (GitHub will show errors)

**Tests fail in CI but pass locally:**
- Database not ready (check PostgreSQL service logs)
- Environment variables differ
- Maven cache issue: try `mvn clean test`

**Docker build fails:**
- Check Dockerfile syntax
- Ensure Maven build succeeds first
- Check base image availability

**Secrets not working:**
- Verify secret names match exactly
- Re-check secret values
- Use `${{ secrets.SECRET_NAME }}` syntax

---

## 📚 Next Steps

1. **Configure Branch Protection** (Settings → Branches)
   - Require status checks to pass
   - Require code reviews
   - Dismiss stale reviews

2. **Set up Code Owners** (already configured in `.github/CODEOWNERS`)
   - Automatically request reviews

3. **Configure Environments** (Settings → Environments)
   - Set approval requirements for production
   - Configure deployment branches

4. **Enable Security Features** (Settings → Security)
   - Code scanning
   - Secret scanning
   - Dependabot alerts

5. **Monitor Workflows**
   - Check **Actions** tab regularly
   - Review artifact reports
   - Track code quality metrics

---

## 📞 Support Resources

- [GitHub Actions Docs](https://docs.github.com/actions)
- [Maven CI/CD](https://maven.apache.org/)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)
- [SonarQube Docs](https://docs.sonarqube.org/)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)

---

**Pipeline Setup Completed!** ✅

Everything is ready. Push your changes and watch the magic happen! 🎉
