#!/bin/bash
#chmod +x compile.sh - enter this the compile the code
#./compile.sh - enter this + arguments to run the code

javac -cp .:./libs/* --source-path ./A2src ./A2src/A2main.java

cd A2src
java -cp .:../libs/* A2main $1 $2
