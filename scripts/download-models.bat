@echo off
setlocal
set SCRIPT_DIR=%~dp0
powershell -ExecutionPolicy Bypass -File "%SCRIPT_DIR%download-models.ps1" %*
endlocal
