echo off
echo NUL>_.class&&del /s /f /q *.class
cls
javac test.java
javac com/krzem/java_class_decompiler/Main.java&&java com/krzem/java_class_decompiler/Main -bd ../../../ test.class
start /min cmd /c "echo NUL>_.class&&del /s /f /q *.class"