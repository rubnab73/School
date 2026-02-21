# GitHub Workflows Setup Guide

## 🎯 What Was Created

I've set up a complete GitHub Actions CI/CD pipeline for your School Management System with the following workflows:

### Core Workflows

1. **[test.yml](.github/workflows/test.yml)** - Simple Test Runner
   - Runs all tests on PRs and merges
   - Generates test reports
   - Uploads artifacts
   - Perfect for quick feedback

2. **[ci.yml](.github/workflows/ci.yml)** - Full CI/CD Pipeline  
   - Comprehensive build and test process
   - Separate unit and integration tests
   - Code quality checks
   - Security scanning
   - Result notifications

### Helper Workflows

3. **[pr-check.yml](.github/workflows/pr-check.yml)** - PR Title Validation
   - Enforces conventional commit format
   - Ensures PRs follow best practices

4. **[labeler.yml](.github/workflows/labeler.yml)** - Auto Labeling
   - Automatically labels PRs based on changed files
   - Uses [labeler.yml](.github/labeler.yml) configuration

### Configuration Files

5. **[dependabot.yml](.github/dependabot.yml)** - Dependency Updates
   - Automatic Maven dependency updates
   - GitHub Actions version updates
   - Weekly schedule

6. **[labeler.yml](.github/labeler.yml)** - Label Configuration
   - Defines which files trigger which labels
   - Covers: java, tests, docs, frontend, etc.

---

## 🚀 Quick Start

### Step 1: Push to GitHub

If you haven't already, push your code to GitHub:

```bash
git add .
git commit -m "Add GitHub Actions workflows"
git push origin main
```

### Step 2: Verify Workflows

1. Go to your repository on GitHub
2. Click the **Actions** tab
3. You should see all workflows listed

### Step 3: Enable Required Features

#### Enable GitHub Actions (if not already enabled):
1. Go to **Settings** → **Actions** → **General**
2. Enable "Allow all actions and reusable workflows"

#### Set Workflow Permissions:
1. Go to **Settings** → **Actions** → **General**
2. Scroll to "Workflow permissions"
3. Select "Read and write permissions"
4. Check "Allow GitHub Actions to create and approve pull requests"

### Step 4: Configure Branch Protection (Recommended)

1. Go to **Settings** → **Branches**
2. Add rule for `main` branch
3. Check:
   - ✅ Require a pull request before merging
   - ✅ Require status checks to pass before merging
   - ✅ Require branches to be up to date before merging
4. Select status checks:
   - `Run Unit and Integration Tests`
   - `Build and Test`

---

## 📋 How The Workflows Run

### On Pull Request Creation:
```
1. test.yml runs → Executes all tests
2. ci.yml runs → Full build, test, quality checks
3. pr-check.yml runs → Validates PR title
4. labeler.yml runs → Auto-labels the PR
```

### On Push/Merge to Main:
```
1. test.yml runs → Validates the merge
2. ci.yml runs → Ensures code quality
```

### Weekly (Dependabot):
```
- Checks for dependency updates
- Creates PRs for outdated dependencies
```

---

## 🎨 Add Status Badges

Add these to your `README.md` to show workflow status:

```markdown
# School Management System

![Tests](https://github.com/YOUR_USERNAME/School/actions/workflows/test.yml/badge.svg)
![CI/CD](https://github.com/YOUR_USERNAME/School/actions/workflows/ci.yml/badge.svg)

<!-- rest of your README -->
```

Replace `YOUR_USERNAME` with your GitHub username.

---

## 🧪 Testing the Workflows

### Test Locally First:
```bash
# Run tests locally to ensure they pass
./mvnw clean test

# Make mvnw executable if needed
git update-index --chmod=+x mvnw
```

### Trigger Workflows Manually:
1. Go to **Actions** tab
2. Select a workflow (e.g., "Run Tests")
3. Click "Run workflow" dropdown
4. Click "Run workflow" button

### Create a Test PR:
```bash
git checkout -b test-workflow
echo "# Test" >> TEST.md
git add TEST.md
git commit -m "test: verify workflow execution"
git push origin test-workflow
```

Then create a PR on GitHub and watch the workflows run!

---

## 📊 Viewing Results

### In Pull Requests:
- **Checks tab**: See all workflow runs
- **Conversation tab**: See status checks at the bottom
- **Files changed tab**: See inline code quality feedback

### In Actions Tab:
- **Summary**: Overall workflow status
- **Jobs**: Individual job results
- **Artifacts**: Download test reports
- **Logs**: Detailed execution logs

---

## ⚙️ Customization Options

### Adjust Test Execution:

Edit `.github/workflows/test.yml` or `.github/workflows/ci.yml`:

```yaml
# Run specific test categories
- name: Run repository tests only
  run: ./mvnw test -Dtest=*RepositoryTest -B

# Skip integration tests
- name: Run unit tests
  run: ./mvnw test -Dtest=!*IntegrationTest -B
```

### Change Branch Triggers:

```yaml
on:
  pull_request:
    branches: [ "main", "develop", "staging" ]
  push:
    branches: [ "main", "production" ]
```

### Add More Java Versions:

In `ci.yml`:
```yaml
strategy:
  matrix:
    java-version: [17, 21]  # Test on multiple versions
```

### Customize PR Labels:

Edit `.github/labeler.yml` to add/remove/modify labels.

### Adjust Dependabot:

Edit `.github/dependabot.yml`:
```yaml
schedule:
  interval: "daily"  # Change from weekly to daily
```

---

## 🔧 Troubleshooting

### Workflow Not Running?

**Check:**
1. Workflows are enabled in Settings → Actions
2. `.github/workflows/` files are committed and pushed
3. YAML syntax is valid (use [YAML Lint](https://www.yamllint.com/))
4. Branch protection rules aren't blocking execution

### Tests Failing in CI but Pass Locally?

**Common Issues:**
1. **Database**: Ensure `application-test.yaml` is configured correctly
2. **Dependencies**: Check all test dependencies are in `pom.xml`
3. **Environment**: Add required environment variables to workflow
4. **Permissions**: Ensure `mvnw` has execute permissions

**Solutions:**
```bash
# Make mvnw executable
chmod +x mvnw
git add mvnw
git commit -m "Make mvnw executable"
git push
```

### Permission Errors?

Update workflow permissions in Settings → Actions → General → Workflow permissions.

### Workflow Takes Too Long?

**Optimizations:**
1. Maven caching is already enabled
2. Consider splitting tests into parallel jobs
3. Use `continue-on-error: true` for non-critical steps
4. Reduce artifact retention days

---

## 📚 Additional Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Workflow Syntax](https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)
- See [.github/workflows/README.md](.github/workflows/README.md) for detailed workflow documentation

---

## ✅ Next Steps

1. ✅ Push workflows to GitHub
2. ✅ Configure branch protection rules
3. ✅ Create a test PR to verify workflows
4. ✅ Add status badges to README.md
5. ✅ Update `YOUR_USERNAME` in dependabot.yml
6. ✅ Review and customize labeler.yml for your needs

---

## 🎉 You're All Set!

Your CI/CD pipeline is ready to:
- ✅ Run tests automatically on every PR
- ✅ Validate code quality
- ✅ Check for security vulnerabilities  
- ✅ Keep dependencies up to date
- ✅ Auto-label PRs
- ✅ Enforce PR title conventions

Happy coding! 🚀

---

**Created:** February 21, 2026  
**Last Updated:** February 21, 2026
