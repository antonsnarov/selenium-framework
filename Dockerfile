# Multi-stage build для оптимизации размера образа

# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Копируем pom.xml для кэширования зависимостей
COPY pom.xml .

# Загружаем зависимости
RUN mvn dependency:go-offline -B

# Копируем исходный код
COPY src ./src

# Собираем проект
RUN mvn clean compile test-compile -DskipTests

# Stage 2: Runtime
FROM maven:3.9.6-eclipse-temurin-17

# Устанавливаем Chrome и Chromium для Selenium
RUN apt-get update && apt-get install -y \
    chromium-browser \
    chromium-driver \
    firefox-esr \
    xvfb \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Копируем собранный проект из builder stage
COPY --from=builder /app .

# Копируем allure.properties для интеграции с Allure
COPY src/test/resources/allure.properties ./src/test/resources/

# Переменные окружения для headless-режима
ENV DISPLAY=:99
ENV HEADLESS=true
ENV BROWSER=chrome

# Выполняем тесты при запуске контейнера
CMD ["sh", "-c", "Xvfb :99 -screen 0 1920x1080x24 &  && mvn clean test -Dheadless=true"]

