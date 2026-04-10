#!/bin/bash

echo "=== Starting Maven Test Execution ===" > maven_test.log
date >> maven_test.log
echo "Current directory: $(pwd)" >> maven_test.log
echo "Java version:" >> maven_test.log
java -version >> maven_test.log 2>&1
echo "Maven version:" >> maven_test.log
mvn --version >> maven_test.log 2>&1
echo "" >> maven_test.log

echo "=== Running Maven Clean ===" >> maven_test.log
mvn clean >> maven_test.log 2>&1
echo "Clean result: $?" >> maven_test.log

echo "=== Running Maven Compile ===" >> maven_test.log
mvn compile >> maven_test.log 2>&1
echo "Compile result: $?" >> maven_test.log

echo "=== Running Simple Test ===" >> maven_test.log
mvn test -Dtest=SimpleTest >> maven_test.log 2>&1
echo "Test result: $?" >> maven_test.log

echo "=== Checking Results ===" >> maven_test.log
ls -la target/surefire-reports/ >> maven_test.log 2>&1
echo "Test reports exist: $?" >> maven_test.log

echo "=== Test Execution Complete ===" >> maven_test.log
date >> maven_test.log

echo "Log written to maven_test.log"
