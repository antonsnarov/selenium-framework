#!/bin/bash

# Скрипт для запуска тестов с Selenium Grid через Docker Compose

set -e

echo "🐳 Starting Selenium Grid with Docker Compose..."
docker-compose up -d

echo "⏳ Waiting for Grid to be ready..."
sleep 10

echo "✅ Running tests against Selenium Grid..."
mvn clean test -Dremote=true -Dgrid.url=http://localhost:4444

echo "📊 Generating Allure report..."
mvn allure:report

echo "✅ Tests completed!"
echo "📈 View report: mvn allure:serve"
echo "🛑 Stop containers: docker-compose down"

