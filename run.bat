@echo off
cd /d "%~dp0"
java -cp "bin;lib/sqlite-jdbc.jar" App
pause
