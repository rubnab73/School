# GitHub Workflows Quick Reference

## Files Created

```
.github/
├── workflows/
│   ├── test.yml          # Simple test runner
│   ├── ci.yml            # Full CI/CD pipeline
│   ├── pr-check.yml      # PR title validation
│   ├── labeler.yml       # Auto-label PRs
│   └── README.md         # Detailed workflow docs
├── dependabot.yml        # Dependency updates
└── labeler.yml          # Label configuration
```

## Workflow Triggers

| Workflow | PR | Push to Main | Manual | Schedule |
|----------|----|--------------| -------|----------|
| test.yml | ✅ | ✅ | ✅ | ❌ |
| ci.yml   | ✅ | ✅ | ❌ | ❌ |
| pr-check.yml | ✅ | ❌ | ❌ | ❌ |
| labeler.yml | ✅ | ❌ | ❌ | ❌ |
| dependabot | ❌ | ❌ | ❌ | ✅ Weekly |

## What Each Workflow Does

### test.yml - Simple & Fast
```
✓ Checkout code
✓ Setup Java 21
✓ Cache Maven dependencies
✓ Build project
✓ Run ALL tests
✓ Generate test reports
✓ Upload artifacts
```

### ci.yml - Comprehensive
```
BUILD & TEST JOB:
✓ Compile project
✓ Run unit tests
✓ Run integration tests
✓ Run all tests with coverage
✓ Verify build
✓ Comment on PR

CODE QUALITY JOB:
✓ Dependency analysis
✓ Security vulnerability scan

NOTIFY JOB:
✓ Check overall status
✓ Report results
```

### pr-check.yml
```
✓ Validates PR title format
✓ Enforces: feat|fix|docs|style|refactor|test|chore
✓ Example: "feat: Add new feature"
```

### labeler.yml
```
✓ Auto-labels based on changed files:
  - java code → 'java'
  - tests → 'tests'
  - docs → 'documentation'
  - templates → 'frontend'
  - controllers → 'controller'
  - etc.
```

### dependabot.yml
```
✓ Weekly Maven dependency checks
✓ Weekly GitHub Actions checks
✓ Auto-creates PRs for updates
```

## Command Quick Reference

```bash
# Make mvnw executable (if needed)
git update-index --chmod=+x mvnw

# Test locally before pushing
./mvnw clean test

# Commit and push workflows
git add .github/
git commit -m "ci: Add GitHub Actions workflows"
git push origin main

# Create test PR
git checkout -b test-workflows
git push origin test-workflows
# Then create PR on GitHub
```

## Status Badge Code

Add to README.md:
```markdown
![Tests](https://github.com/USERNAME/REPO/actions/workflows/test.yml/badge.svg)
![CI/CD](https://github.com/USERNAME/REPO/actions/workflows/ci.yml/badge.svg)
```

## Viewing Results

| Location | What You'll See |
|----------|----------------|
| PR Checks Tab | All workflow statuses |
| PR Conversation | Status checks at bottom |
| Actions Tab | All workflow runs & logs |
| Artifacts | Downloaded test reports |

## Common Customizations

```yaml
# Run on different branches
on:
  push:
    branches: [ "main", "develop", "staging" ]

# Add environment variables
env:
  SPRING_PROFILES_ACTIVE: test
  TZ: UTC

# Increase Maven memory
env:
  MAVEN_OPTS: -Xmx1024m

# Run on schedule (cron)
on:
  schedule:
    - cron: '0 0 * * 0'  # Every Sunday
```

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Workflow not running | Check Settings → Actions enabled |
| Permission denied on mvnw | Run `git update-index --chmod=+x mvnw` |
| Tests fail in CI only | Check `application-test.yaml` config |
| Slow workflows | Maven caching already enabled |
| PR comments not working | Enable write permissions in Settings |

## Next Steps

1. ✅ Push to GitHub
2. ✅ Configure branch protection
3. ✅ Create test PR
4. ✅ Add status badges
5. ✅ Customize as needed

---

**Full Documentation:** See [WORKFLOWS_SETUP.md](WORKFLOWS_SETUP.md)
