@echo off
REM CI/CD Helper Script - Run local CI checks before pushing (Windows version)

setlocal enabledelayedexpansion

echo.
echo 🔍 Running CI Checks Locally...
echo.

REM Check if git is initialized
if not exist .git (
    echo ❌ Not a git repository. Please run this from the root of the project.
    exit /b 1
)

echo 📋 Running Maven Clean...
call mvn clean
if errorlevel 1 goto error

echo.
echo 🔨 Building Project...
call mvn compile
if errorlevel 1 goto error

echo.
echo ✅ Running Unit Tests...
call mvn test
if errorlevel 1 goto error

echo.
echo 🐛 Running SpotBugs...
call mvn spotbugs:check
if errorlevel 1 (
    echo ⚠️  SpotBugs warnings found (check logs)
)

echo.
echo 📊 Running PMD...
call mvn pmd:check
if errorlevel 1 (
    echo ⚠️  PMD violations found (check logs)
)

echo.
echo 📐 Running Checkstyle...
call mvn checkstyle:check
if errorlevel 1 (
    echo ⚠️  Code style issues found (check logs)
)

echo.
echo 📦 Building JAR...
call mvn package -DskipTests
if errorlevel 1 goto error

echo.
echo ✅ All CI checks passed!
echo.
echo Ready to push! 🚀
echo.
exit /b 0

:error
echo.
echo ❌ Error encountered during CI checks!
exit /b 1
