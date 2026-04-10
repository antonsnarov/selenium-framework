#!/bin/bash

# Скрипт для локального запуска тестов в Docker

set -e

echo "🐳 Building Docker image..."
docker build -t selenium-tests:latest .

echo "✅ Running tests in Docker container..."
docker run --rm \
  -v "$(pwd)/allure-results:/app/allure-results" \
  -v "$(pwd)/target:/app/target" \
  -e HEADLESS=true \
  selenium-tests:latest

echo "📊 Generating Allure report..."
mvn allure:report

echo "✅ Tests completed! Open allure-report/index.html to see the report"

