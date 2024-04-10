@ECHO OFF

javac -d target/classes src/main/java/*
ECHO ---
ECHO Compiled to target/classes
ECHO ---
PAUSE
java -cp target/classes Experiment1
java -cp target/classes Experiment2
java -cp target/classes Experiment3

