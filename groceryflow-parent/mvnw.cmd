@REM ----------------------------------------------------------------------------
@REM GroceryFlow Maven Wrapper for Windows (targets Java 17)
@REM ----------------------------------------------------------------------------
@echo off
setlocal

if not defined JAVA_HOME (
    if exist "C:\Program Files\Java\jdk-17" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-17"
    )
)

set "MAVEN_CMD=%~dp0.tools\apache-maven-3.9.9\bin\mvn.cmd"

if not exist "%MAVEN_CMD%" (
    echo [ERROR] Maven not found at %MAVEN_CMD%
    exit /b 1
)

call "%MAVEN_CMD%" %*
exit /b %ERRORLEVEL%
