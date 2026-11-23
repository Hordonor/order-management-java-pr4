@echo off
setlocal
chcp 65001 >nul
set "MAVEN_OPTS=-Dfile.encoding=UTF-8"
call mvnw.cmd clean package
set "RESULT=%ERRORLEVEL%"
if not "%RESULT%"=="0" goto finish
java -Dfile.encoding=UTF-8 --module-path "order-processing\target\order-processing-1.0.0.jar;order-storage\target\order-storage-1.0.0.jar;order-threading\target\order-threading-1.0.0.jar;order-threading\target\dependency" --module order.threading/ua.edu.dut.order.threading.Main
set "RESULT=%ERRORLEVEL%"

:finish
echo.
echo Press any key to close this window...
pause >nul
endlocal & exit /b %RESULT%
