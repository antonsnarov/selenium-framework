#!/bin/bash
echo "=== Testing Maven Dependencies Fix ==="
echo "Current directory: $(pwd)"
echo "Testing Allure 2.15.0 + TestNG 7.8.0 compatibility..."

# Test Maven dependency resolution
echo "Step 1: Testing dependency resolution..."
docker run --rm -v "$(pwd)":/app -w /app maven:3.9.6-eclipse-temurin-17 \
  mvn dependency:resolve -q -Dmaven.test.skip=true

if [ $? -eq 0 ]; then
    echo "✅ Dependencies resolved successfully!"
else
    echo "❌ Dependency resolution failed!"
    exit 1
fi

# Test compilation
echo "Step 2: Testing compilation..."
docker run --rm -v "$(pwd)":/app -w /app maven:3.9.6-eclipse-temurin-17 \
  mvn compile -q -Dmaven.test.skip=true

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
else
    echo "❌ Compilation failed!"
    exit 1
fi

# Test unit tests
echo "Step 3: Testing unit tests..."
docker run --rm -v "$(pwd)":/app -w /app maven:3.9.6-eclipse-temurin-17 \
  mvn test -Dtest=UnitTest -q

if [ $? -eq 0 ]; then
    echo "✅ Unit tests passed!"
else
    echo "❌ Unit tests failed!"
    exit 1
fi

echo "🎉 All tests passed! Maven dependencies are working correctly."
