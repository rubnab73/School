# GitHub Repository Setup Guide

Complete these steps after pushing the CI/CD pipeline to your repository.

## 1️⃣ Repository Settings

### General Settings
**Settings → General**

- [x] Default branch: `main`
- [x] Require branches to be up to date before merging: ✓
- [x] Allow auto-merge: ✓ (optional)
- [x] Allow force pushes: ✗
- [x] Automatically delete head branches: ✓

### Description & Topics
**Settings → General (at top)**

```
School Management System
A full-featured school management application built with Spring Boot 4.0 and PostgreSQL.

Topics: java, spring-boot, maven, ci-cd, github-actions, docker, postgresql
```

---

## 2️⃣ Branch Protection Rules

**Settings → Branches → Add rule**

### Main Branch (`main`)
```
Branch name pattern: main

Require a pull request before merging:
  ✓ Require approvals: 1
  ✓ Require status checks to pass: 
    - build (Build and Test)
    - code-quality (Code Quality Analysis)
    - security-scan (Security Scanning)
  ✓ Include administrators: ✓
  ✓ Require branches to be up to date: ✓
  ✓ Require conversation resolution: ✓

Restrict who can push to matching branches:
  ✓ Allow specified actors to bypass: (Optional)
```

### Develop Branch (`develop`)
```
Branch name pattern: develop

Require a pull request before merging:
  ✓ Require approvals: 1
  ✓ Require status checks to pass: (same as main)
  ✓ Require branches to be up to date: ✓
```

### Test2 Branch (`test2`)
```
Branch name pattern: test2

Require a pull request before merging:
  ✓ Require approvals: 0 (allows direct pushes for development)
  ✓ Require status checks to pass: (optional)
```

---

## 3️⃣ Secrets & Variables

**Settings → Secrets and variables → Actions**

### Required Secrets

Add these if you plan to use the features:

```
# SonarQube (optional)
SONAR_HOST_URL=https://your-sonarqube-instance.com
SONAR_TOKEN=<your-sonarqube-token>

# Docker Hub (optional)
DOCKER_USERNAME=<your-username>
DOCKER_PASSWORD=<your-access-token>

# Deployment (optional)
DEPLOY_PRIVATE_KEY=<contents-of-id_rsa>
DEPLOY_HOST=deploy.example.com
DEPLOY_USER=deploy-user

# Slack (optional)
SLACK_WEBHOOK=https://hooks.slack.com/services/T.../B.../...
```

### Variables (Non-sensitive)

```
JAVA_VERSION=21
MAVEN_VERSION=3.9
```

---

## 4️⃣ Environments Configuration

**Settings → Environments**

### Development Environment
```
Name: development
Deployment branches and tags:
  ✓ Selected branches
    - test2

Protection rules:
  - No approval required
  - Deployment branch policy: strict
```

### Staging Environment
```
Name: staging
Deployment branches and tags:
  ✓ Selected branches
    - develop

Protection rules:
  - No approval required (optional: require approval)
  - Deployment branch policy: strict
```

### Production Environment
```
Name: production
Deployment branches and tags:
  ✓ Selected branches
    - main

Protection rules:
  ✓ Require all reviewers to approve: 1
  ✓ Require review from code owners: ✓
  ✓ Dismiss stale pull request approvals: ✓
  ✓ Require status checks to pass: ✓
  ✓ Lock environment: ✗
```

---

## 5️⃣ Code Security

**Settings → Code security and analysis**

Enable:
- [x] Dependabot alerts
- [x] Dependabot security updates
- [x] Secret scanning
- [x] Push protection
- [x] Code scanning (if using CodeQL)

---

## 6️⃣ Pages (Optional - for documentation)

**Settings → Pages**

```
Source:
  Branch: main
  Folder: /docs (or /)

Theme: None (or select a Jekyll theme)
```

---

## 7️⃣ Collaborators & Access

**Settings → Collaborators and teams**

```
Manage access:
  - Public: open for issues/discussions
  - Private: restricted access

Roles:
  Admin: Repository owners only
  Maintain: Senior developers
  Write: Developers (can merge)
  Triage: Junior developers (no merge)
  Read: Documentation/viewers (read-only)
```

---

## 8️⃣ Webhooks (Optional)

**Settings → Webhooks**

### Slack Notifications
```
Payload URL: https://hooks.slack.com/services/T.../B.../...
Content type: application/json
Events:
  - Push
  - Pull request
  - Workflow runs
```

### External CI (if not using GitHub Actions)
```
Configure as needed for your provider
```

---

## 9️⃣ Actions Permissions

**Settings → Actions → General**

```
Actions permissions:
  ✓ Allow all actions and reusable workflows

Fork pull request workflows from outside collaborators:
  ✓ Require approval for all outside collaborators

Workflow permissions:
  ✓ Read and write permissions
  ✓ Allow GitHub Actions to create and approve pull requests
```

---

## 🔟 Discussion & Issue Configuration

**Settings → Features**

Enable:
- [x] Discussions
  - Welcome message: "Have questions or need help?"
  - Categories:
    - 💬 General (Q&A)
    - 🚀 Ideas (Feature suggestions)
    - 📣 Announcements
    - 💭 Show and tell
- [x] Issues
- [x] Projects (optional)
- [x] Wiki (optional)

---

## First Repository Actions

### 1. Update Code Owners
Edit `.github/CODEOWNERS` and add your username:
```bash
# Only you can approve changes to critical areas
* @your-username
src/main/java/com/rubayet/school/security/ @your-username
```

### 2. Create Initial PR
```bash
git checkout -b init/ci-setup
git add .
git commit -m "ci: add GitHub Actions CI/CD pipeline"
git push origin init/ci-setup
# Create PR on GitHub website
```

### 3. Monitor First Workflow
- Go to **Actions** tab
- Wait for workflow to complete
- Check for any issues
- Fix and re-push

### 4. Merge & Tag
```bash
# After PR is merged to main
git tag -a v0.0.1 -m "Initial CI/CD setup"
git push origin v0.0.1
```

---

## Workflow Verification Checklist

- [ ] **Actions tab shows workflows**
  - ci.yml appears
  - deploy.yml visible
  - scheduled-checks.yml ready

- [ ] **CI pipeline runs on push**
  - Build job completes
  - Tests execute
  - Artifacts uploaded

- [ ] **Status checks appear on PRs**
  - Green checkmarks when passing
  - Red X when failing
  - Details link to workflow

- [ ] **Test results posted to PR**
  - Comment with test summary
  - Coverage information (if configured)
  - Links to artifacts

- [ ] **Code coverage tracked**
  - JaCoCo reports generated
  - Codecov integration (if configured)
  - Trend charts available

- [ ] **Code quality analyzed**
  - SonarQube results posted (if configured)
  - SpotBugs warnings logged
  - PMD issues reported

- [ ] **Security scanning active**
  - Dependency Check running weekly
  - Trivy vulnerability scan active
  - SARIF reports uploaded to GitHub

---

## Monitoring Dashboard Commands

View workflow status:
```bash
# List all workflows
gh workflow list

# View recent runs
gh run list --workflow=ci.yml

# View specific run details
gh run view <run-id>

# Download artifacts
gh run download <run-id> --name test-reports
```

---

## Troubleshooting Setup

### Workflows not appearing
1. Verify `.github/workflows/*.yml` files exist
2. Check YAML syntax is correct
3. Refresh GitHub page (hard refresh)
4. Check branch name matches trigger

### Status checks not showing
1. Workflow must have `jobs` defined
2. Job names must match branch protection rule
3. Workflow must complete at least once
4. Make a new PR to see status checks

### Secrets not accessible
1. Verify secret names match exactly in workflows
2. Check spelling in `${{ secrets.VARIABLE_NAME }}`
3. Secrets are case-sensitive
4. Test with echo (but never push credentials)

### Deployment not triggering
1. Check environment name is correct
2. Verify branch matches trigger condition
3. Check approval is not required (if unexpected)
4. Review workflow logs for error messages

---

## Post-Setup Checklist

- [ ] All workflows visible in Actions tab
- [ ] Branch protection rules configured
- [ ] Secrets added for enabled features
- [ ] Environments created (dev/staging/prod)
- [ ] Code owners file updated
- [ ] Pull request template in place
- [ ] Issue templates available
- [ ] GitHub Pages configured (optional)
- [ ] Team members have appropriate access
- [ ] Documentation links updated in README
- [ ] First PR tested and merged
- [ ] Production tag created

---

## Continuous Improvement

### Regular Maintenance
- Monthly: Review workflow runs and artifacts
- Quarterly: Update GitHub Actions and plugins
- Quarterly: Review and update security scanning
- Annually: Audit access control and permissions

### Metrics to Monitor
- Build success rate
- Test pass rate
- Code coverage trend
- Security vulnerability count
- Deployment frequency
- Mean time to recovery (MTTR)

### Resources for Learning
- [GitHub Actions Best Practices](https://docs.github.com/en/actions/guides)
- [Advanced Workflow Techniques](https://docs.github.com/en/actions/learn-github-actions/workflow-syntax-for-github-actions)
- [Workflow Security](https://docs.github.com/en/actions/security-guides)
- [Maven CI Best Practices](https://maven.apache.org/)

---

**Setup complete! Your CI/CD pipeline is ready for production use.** 🎉
