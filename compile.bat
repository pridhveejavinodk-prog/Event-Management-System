@echo off
echo ==============================================
echo Compiling College Event Management System...
echo ==============================================
if not exist "bin" mkdir bin
javac -cp "lib/*;src" -d bin src/*.java
if %ERRORLEVEL% equ 0 (
    echo Compilation successful!
) else (
    echo Compilation failed!
)
pause
