@echo off
echo ==============================================
echo Starting College Event Management System...
echo ==============================================
if not exist "bin\Main.class" (
    echo Binaries not found, compiling first...
    if not exist "bin" mkdir bin
    javac -cp "lib/*;src" -d bin src/*.java
)
java -cp "bin;lib/*" Main
pause
