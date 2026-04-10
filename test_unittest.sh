#!/bin/bash
echo "=== Testing UnitTest ===" > unittest_result.log
echo "Start time: $(date)" >> unittest_result.log

echo "Running: mvn test -Dtest=UnitTest" >> unittest_result.log
mvn test -Dtest=UnitTest >> unittest_result.log 2>&1
exit_code=$?

echo "Exit code: $exit_code" >> unittest_result.log
echo "End time: $(date)" >> unittest_result.log

if [ $exit_code -eq 0 ]; then
    echo "SUCCESS: UnitTest passed!" >> unittest_result.log
else
    echo "FAILED: UnitTest failed!" >> unittest_result.log
fi

echo "Check unittest_result.log for details"
