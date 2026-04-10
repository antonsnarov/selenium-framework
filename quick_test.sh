#!/bin/bash
echo "=== Quick Maven Test Check ==="
echo "Testing UnitTest locally..."

# Clean and compile first
mvn clean compile -q

# Run unit test
mvn test -Dtest=UnitTest -q

if [ $? -eq 0 ]; then
    echo "✅ SUCCESS: UnitTest passed!"
    echo "Maven dependencies are working correctly."
else
    echo "❌ FAILED: UnitTest failed!"
    echo "Check the error messages above."
    exit 1
fi
