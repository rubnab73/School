@echo off
REM Local development setup script for Windows

setlocal enabledelayedexpansion

echo.
echo 🚀 Setting up School Management System...
echo.

REM Check if Docker is installed
docker --version >nul 2>&1
if errorlevel 1 (
    echo ❌ Docker is not installed. Please install Docker first.
    exit /b 1
)

REM Check if Docker Compose is installed
docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo ❌ Docker Compose is not installed. Please install Docker Compose first.
    exit /b 1
)

REM Start PostgreSQL container
echo.
echo 📦 Starting PostgreSQL container...
docker-compose up -d postgres

REM Wait for PostgreSQL to be ready
echo.
echo ⏳ Waiting for PostgreSQL to be ready...
setlocal
for /L %%i in (1,1,30) do (
    docker-compose exec -T postgres pg_isready -U schooluser >nul 2>&1
    if !errorlevel! == 0 (
        echo ✅ PostgreSQL is ready!
        goto postgres_ready
    )
    echo ⏳ Waiting... (%%i/30)
    timeout /t 1 /nobreak >nul
)

:postgres_ready
endlocal

REM Build the project
echo.
echo 🔨 Building the project...
call mvn clean install -DskipTests

if errorlevel 1 (
    echo ❌ Build failed!
    exit /b 1
)

echo.
echo ✅ Setup complete!
echo.
echo 📝 Next steps:
echo   1. Start the application: mvn spring-boot:run
echo   2. Access the application at: http://localhost:8080
echo   3. Stop PostgreSQL: docker-compose down
echo.
