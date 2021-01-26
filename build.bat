@echo off
cls
if exist build rmdir /s /q build
mkdir build
cd src
javac -d ../build com/krzem/java_class_decompiler/Main.java&&jar cvmf ../manifest.mf ../build/java_class_decompiler.jar -C ../build *&&goto run
cd ..
goto end
:run
cd ..
pushd "build"
for /D %%D in ("*") do (
	rd /S /Q "%%~D"
)
for %%F in ("*") do (
	if /I not "%%~nxF"=="java_class_decompiler.jar" del "%%~F"
)
popd
javac test.java
cls
java -jar build/java_class_decompiler.jar
:end
