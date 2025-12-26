@echo off
echo ========================================
echo   Redstone Plus - LEVE (Light Build)
echo ========================================
echo.

echo Otimizando para PC fraco...
echo Pulando testes e daemon para economizar RAM.
echo.

echo Limpando...
call gradlew clean --no-daemon

echo.
echo Compilando (Modo Economico)...
call gradlew assemble --no-daemon -x test

echo.
if exist "build\libs\redstone_plus-1.0.0.jar" (
    echo ========================================
    echo   BUILD CONCLUIDO COM SUCESSO!
    echo ========================================
    echo.
    echo O mod foi compilado em:
    echo   build\libs\redstone_plus-1.0.0.jar
    echo.
) else (
    echo ========================================
    echo   ERRO NA COMPILACAO!
    echo ========================================
    echo Verifique os erros acima.
)

pause
