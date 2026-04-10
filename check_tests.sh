#!/bin/bash
echo "Starting Maven test execution..." > maven_execution.log
date >> maven_execution.log
echo "Current directory: $(pwd)" >> maven_execution.log

echo "Running: mvn clean test" >> maven_execution.log
mvn clean test >> maven_execution.log 2>&1
echo "Exit code: $?" >> maven_execution.log

echo "Checking results..." >> maven_execution.log
ls -la target/surefire-reports/ >> maven_execution.log 2>&1 2>/dev/null || echo "No surefire-reports found" >> maven_execution.log

echo "Test execution completed" >> maven_execution.log
date >> maven_execution.log
