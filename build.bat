@echo off
REM Quick Start Script for fin-manager Backend (Windows)

echo === fin-manager Backend Setup ===
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo X Maven not found. Please install Maven first.
    exit /b 1
)

echo [OK] Maven found
echo.

REM Clean previous builds
echo Cleaning previous builds...
call mvn clean

REM Compile
echo Compiling...
call mvn compile
if %ERRORLEVEL% NEQ 0 (
    echo X Compilation failed
    exit /b 1
)
echo [OK] Compilation successful
echo.

REM Run tests
echo Running tests...
call mvn test
echo.

REM Package
echo Packaging JAR...
call mvn package -DskipTests

if exist "target\fin-manager.jar" (
    echo [OK] JAR created: target\fin-manager.jar
    echo.
    echo [RUN] To start the server:
    echo    java -jar target\fin-manager.jar
    echo.
    echo [API] Server will be available at: http://localhost:8080
    echo.
    echo [EXAMPLES] Example API calls:
    echo    curl http://localhost:8080/api/health
    echo    curl http://localhost:8080/api/categories
) else (
    echo X JAR creation failed
    exit /b 1
)
