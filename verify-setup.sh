#!/bin/bash
# CI/CD Setup Verification Script
# Run this script to verify all files were created correctly

echo "🔍 Verifying CI/CD Pipeline Setup..."
echo ""

# Color codes
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

CHECKED=0
PASSED=0
FAILED=0

check_file() {
    CHECKED=$((CHECKED + 1))
    if [ -f "$1" ]; then
        echo -e "${GREEN}✅${NC} $1"
        PASSED=$((PASSED + 1))
    else
        echo -e "${RED}❌${NC} $1"
        FAILED=$((FAILED + 1))
    fi
}

check_dir() {
    CHECKED=$((CHECKED + 1))
    if [ -d "$1" ]; then
        echo -e "${GREEN}✅${NC} $1/"
        PASSED=$((PASSED + 1))
    else
        echo -e "${RED}❌${NC} $1/"
        FAILED=$((FAILED + 1))
    fi
}

echo "📋 Checking GitHub Workflows..."
check_file ".github/workflows/ci.yml"
check_file ".github/workflows/deploy.yml"
check_file ".github/workflows/scheduled-checks.yml"

echo ""
echo "⚙️  Checking Configuration Files..."
check_file ".github/dependabot.yml"
check_file ".github/CODEOWNERS"
check_file ".github/pull_request_template.md"
check_file ".github/sonar.properties"

echo ""
echo "📝 Checking Issue Templates..."
check_file ".github/ISSUE_TEMPLATE/bug_report.yml"
check_file ".github/ISSUE_TEMPLATE/feature_request.yml"

echo ""
echo "🔧 Checking Build Configuration..."
check_file "pom.xml"
check_file "spotbugs-exclude.xml"
check_file "Dockerfile"

echo ""
echo "📚 Checking Documentation..."
check_file "CI_CD_SETUP.md"
check_file "CI_CD_QUICK_REFERENCE.md"
check_file "GITHUB_SETUP.md"
check_file "CI_CD_PIPELINE_SUMMARY.md"

echo ""
echo "🛠️  Checking Helper Scripts..."
check_file "scripts/setup.sh"
check_file "scripts/setup.bat"
check_file "scripts/ci-local.sh"
check_file "scripts/ci-local.bat"

echo ""
echo "📊 Summary"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo -e "${GREEN}Passed: $PASSED/$CHECKED${NC}"
echo -e "${RED}Failed: $FAILED/$CHECKED${NC}"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

if [ $FAILED -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✅ All files verified successfully!${NC}"
    echo ""
    echo "Next steps:"
    echo "  1. Update .github/CODEOWNERS with your GitHub username"
    echo "  2. Review CI_CD_PIPELINE_SUMMARY.md"
    echo "  3. Commit changes: git add . && git commit -m 'ci: add GitHub Actions pipeline'"
    echo "  4. Push to GitHub: git push origin test2"
    echo "  5. Create a test PR to verify workflows"
    echo ""
    exit 0
else
    echo ""
    echo -e "${RED}⚠️  Some files are missing!${NC}"
    echo "Please check the errors above and recreate missing files."
    echo ""
    exit 1
fi
