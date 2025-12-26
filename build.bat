@echo off
echo ========================================
echo   Redstone Plus - Build Script
echo ========================================
echo.

echo Limpando build anterior...
call gradlew clean

echo.
echo Compilando o mod...
call gradlew build

echo.
if exist "build\libs\redstone_plus-1.0.0.jar" (
    echo ========================================
    echo   BUILD CONCLUIDO COM SUCESSO!
    echo ========================================
    echo.
    echo O mod foi compilado em:
    echo   build\libs\redstone_plus-1.0.0.jar
    echo.
    echo Copie este arquivo para sua pasta:
    echo   .minecraft\mods\
    echo.
) else (
    echo ========================================
    echo   ERRO NA COMPILACAO!
    echo ========================================
    echo Verifique os erros acima.
)

pause
