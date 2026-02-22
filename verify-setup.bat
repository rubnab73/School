@echo off
REM CI/CD Setup Verification Script for Windows
REM Run this script to verify all files were created correctly

echo.
echo 🔍 Verifying CI/CD Pipeline Setup...
echo.

setlocal enabledelayedexpansion
set CHECKED=0
set PASSED=0
set FAILED=0

REM Check GitHub Workflows
echo 📋 Checking GitHub Workflows...
if exist ".github\workflows\ci.yml" (
    echo ✅ .github\workflows\ci.yml
    set /a PASSED+=1
) else (
    echo ❌ .github\workflows\ci.yml
    set /a FAILED+=1
)
set /a CHECKED+=1

if exist ".github\workflows\deploy.yml" (
    echo ✅ .github\workflows\deploy.yml
    set /a PASSED+=1
) else (
    echo ❌ .github\workflows\deploy.yml
    set /a FAILED+=1
)
set /a CHECKED+=1

if exist ".github\workflows\scheduled-checks.yml" (
    echo ✅ .github\workflows\scheduled-checks.yml
    set /a PASSED+=1
) else (
    echo ❌ .github\workflows\scheduled-checks.yml
    set /a FAILED+=1
)
set /a CHECKED+=1

echo.
echo ⚙️  Checking Configuration Files...
if exist ".github\dependabot.yml" (
    echo ✅ .github\dependabot.yml
    set /a PASSED+=1
) else (
    echo ❌ .github\dependabot.yml
    set /a FAILED+=1
)
set /a CHECKED+=1

if exist ".github\CODEOWNERS" (
    echo ✅ .github\CODEOWNERS
    set /a PASSED+=1
) else (
    echo ❌ .github\CODEOWNERS
    set /a FAILED+=1
)
set /a CHECKED+=1

if exist ".github\pull_request_template.md" (
    echo ✅ .github\pull_request_template.md
    set /a PASSED+=1
) else (
    echo ❌ .github\pull_request_template.md
    set /a FAILED+=1
)
set /a CHECKED+=1

if exist ".github\sonar.properties" (
    echo ✅ .github\sonar.properties
    set /a PASSED+=1
) else (
    echo ❌ .github\sonar.properties
    set /a FAILED+=1
)
set /a CHECKED+=1

echo.
echo 📝 Checking Issue Templates...
if exist ".github\ISSUE_TEMPLATE\bug_report.yml" (
    echo ✅ .github\ISSUE_TEMPLATE\bug_report.yml
    set /a PASSED+=1
) else (
    echo ❌ .github\ISSUE_TEMPLATE\bug_report.yml
    set /a FAILED+=1
)
set /a CHECKED+=1

if exist ".github\ISSUE_TEMPLATE\feature_request.yml" (
    echo ✅ .github\ISSUE_TEMPLATE\feature_request.yml
    set /a PASSED+=1
) else (
    echo ❌ .github\ISSUE_TEMPLATE\feature_request.yml
    set /a FAILED+=1
)
set /a CHECKED+=1

echo.
echo 🔧 Checking Build Configuration...
if exist "pom.xml" (
    echo ✅ pom.xml
    set /a PASSED+=1
) else (
    echo ❌ pom.xml
    set /a FAILED+=1
)
set /a CHECKED+=1

if exist "spotbugs-exclude.xml" (
    echo ✅ spotbugs-exclude.xml
    set /a PASSED+=1
) else (
    echo ❌ spotbugs-exclude.xml
    set /a FAILED+=1
)
set /a CHECKED+=1

if exist "Dockerfile" (
    echo ✅ Dockerfile
    set /a PASSED+=1
) else (
    echo ❌ Dockerfile
    set /a FAILED+=1
)
set /a CHECKED+=1

echo.
echo 📚 Checking Documentation...
if exist "CI_CD_SETUP.md" (
    echo ✅ CI_CD_SETUP.md
    set /a PASSED+=1
) else (
    echo ❌ CI_CD_SETUP.md
    set /a FAILED+=1
)
set /a CHECKED+=1

if exist "CI_CD_QUICK_REFERENCE.md" (
    echo ✅ CI_CD_QUICK_REFERENCE.md
    set /a PASSED+=1
) else (
    echo ❌ CI_CD_QUICK_REFERENCE.md
    set /a FAILED+=1
)
set /a CHECKED+=1

if exist "GITHUB_SETUP.md" (
    echo ✅ GITHUB_SETUP.md
    set /a PASSED+=1
) else (
    echo ❌ GITHUB_SETUP.md
    set /a FAILED+=1
)
set /a CHECKED+=1

if exist "CI_CD_PIPELINE_SUMMARY.md" (
    echo ✅ CI_CD_PIPELINE_SUMMARY.md
    set /a PASSED+=1
) else (
    echo ❌ CI_CD_PIPELINE_SUMMARY.md
    set /a FAILED+=1
)
set /a CHECKED+=1

echo.
echo 🛠️  Checking Helper Scripts...
if exist "scripts\setup.sh" (
    echo ✅ scripts\setup.sh
    set /a PASSED+=1
) else (
    echo ❌ scripts\setup.sh
    set /a FAILED+=1
)
set /a CHECKED+=1

if exist "scripts\setup.bat" (
    echo ✅ scripts\setup.bat
    set /a PASSED+=1
) else (
    echo ❌ scripts\setup.bat
    set /a FAILED+=1
)
set /a CHECKED+=1

if exist "scripts\ci-local.sh" (
    echo ✅ scripts\ci-local.sh
    set /a PASSED+=1
) else (
    echo ❌ scripts\ci-local.sh
    set /a FAILED+=1
)
set /a CHECKED+=1

if exist "scripts\ci-local.bat" (
    echo ✅ scripts\ci-local.bat
    set /a PASSED+=1
) else (
    echo ❌ scripts\ci-local.bat
    set /a FAILED+=1
)
set /a CHECKED+=1

echo.
echo 📊 Summary
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
echo Passed: %PASSED%/%CHECKED%
echo Failed: %FAILED%/%CHECKED%
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

if %FAILED% equ 0 (
    echo.
    echo ✅ All files verified successfully!
    echo.
    echo Next steps:
    echo   1. Update .github\CODEOWNERS with your GitHub username
    echo   2. Review CI_CD_PIPELINE_SUMMARY.md
    echo   3. Commit changes: git add . ^&^& git commit -m "ci: add GitHub Actions pipeline"
    echo   4. Push to GitHub: git push origin test2
    echo   5. Create a test PR to verify workflows
    echo.
) else (
    echo.
    echo ⚠️  Some files are missing!
    echo Please check the errors above and recreate missing files.
    echo.
)

endlocal
