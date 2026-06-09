@echo off
chcp 65001 >nul
echo ========================================
echo   Library Digital -- Deploy Local
echo ========================================

echo.
echo [0/4] Configurando Java...
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.5.11-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

echo.
echo [1/4] Gerando WAR...
"C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.2.1\plugins\maven\lib\maven3\bin\mvn.cmd" clean package -f "C:\Users\milar\Downloads\Projeto\pom.xml"

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERRO: Build falhou!
    pause
    exit /b 1
)

echo.
echo [2/4] Parando Tomcat se estiver rodando...
taskkill /F /IM "tomcat10.exe" >nul 2>&1
taskkill /F /IM "java.exe" >nul 2>&1
timeout /t 3 /nobreak >nul

echo.
echo [3/4] Copiando WAR...
copy /Y "C:\Users\milar\Downloads\Projeto\target\demo-1.0-SNAPSHOT.war" "C:\Users\milar\Downloads\apache-tomcat-10.1.54\webapps\demo.war"
echo WAR copiado com sucesso!

echo.
echo [4/4] Iniciando Tomcat...
set CATALINA_HOME=C:\Users\milar\Downloads\apache-tomcat-10.1.54
set CATALINA_BASE=C:\Users\milar\Downloads\apache-tomcat-10.1.54
start "Tomcat Library Digital" "C:\Users\milar\Downloads\apache-tomcat-10.1.54\bin\startup.bat"

timeout /t 5 /nobreak >nul

echo.
echo ========================================
echo   Deploy concluido!
echo   Acesse: http://localhost:8080/demo/home
echo ========================================
echo.
pause