@echo off
echo ========================================
echo  Room Booking System - Console Version
echo ========================================
echo.

echo [1/2] Compiling Java files...
javac -cp "lib/sqlite-jdbc.jar;." -d bin src/*.java

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo [2/2] Running application...
echo.
java -cp "bin;lib/sqlite-jdbc.jar" App

pause
