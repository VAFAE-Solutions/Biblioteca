@echo off
chcp 65001 >nul
echo ========================================
echo   Library Digital -- Deploy Local
echo ========================================

echo.
echo [1/4] Gerando WAR...
"C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.2.1\plugins\maven\lib\maven3\bin\mvn.cmd" clean package -f pom.xml

if errorlevel 1 (
    echo.
    echo ERRO: Build falhou! Verifique os erros acima.
    pause
    exit /b 1
)

echo.
echo [2/4] Parando Tomcat...
call "C:\Users\milar\Downloads\apache-tomcat-10.1.54\bin\shutdown.bat" 2>nul
timeout /t 3 /nobreak >nul

echo.
echo [3/4] Copiando WAR...
copy /Y "C:\Users\milar\Downloads\Projeto\target\demo-1.0-SNAPSHOT.war" "C:\Users\milar\Downloads\apache-tomcat-10.1.54\webapps\demo.war"

if errorlevel 1 (
    echo ERRO: Falha ao copiar o WAR!
    pause
    exit /b 1
)

echo.
echo [4/4] Iniciando Tomcat...
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.5.11-hotspot
call "C:\Users\milar\Downloads\apache-tomcat-10.1.54\bin\startup.bat"

echo.
echo ========================================
echo   Deploy concluido com sucesso!
echo   Acesse: http://localhost:8080/demo/home
echo ========================================
echo.
pause